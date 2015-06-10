package fr.sii.repository.spreadsheet;

/**
 * Created by tmaugin on 16/04/2015.
 */

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Link;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;
import fr.sii.domain.spreadsheet.GoogleSpreadsheetCellAddress;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.domain.spreadsheet.RowDraft;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 *
 * Class implemented for accessing google spreadsheet application programming interface (API) features related to cell data, worksheet and spreadsheet
 *
 */

public class SpreadsheetConnector {

    private SpreadsheetService service;
    private Drive driveService;
    private static Log log = LogFactory
            .getLog(SpreadsheetConnector.class);

    public SpreadsheetConnector(SpreadsheetService service, Drive driveService) {
        this.service = service;
        this.driveService = driveService;
    }

    /**
     * Insert new file.
     *
     * @param title Title of the file to insert, including the extension.
     * @param description Description of the file to insert.
     * @param parentId Optional parent folder's ID.
     * @param mimeType MIME type of the file to insert.
     * @return Inserted file metadata if successful, throw @throws otherwise.
     * @throws IOException
     */
    public File insertFile(String title, String description,
                                   String parentId, String mimeType) throws IOException {
        // File's metadata.
        File body = new File();
        body.setTitle(title);
        body.setDescription(description);
        body.setMimeType(mimeType);

        // Set the parent folder.
        if (parentId != null && parentId.length() > 0) {
            body.setParents(
                    Arrays.asList(new ParentReference().setId(parentId)));
        }

        // File's content.
        File file = driveService.files().insert(body).execute();
        // Uncomment the following line to print the File ID.
        // System.out.println("File ID: " + file.getId());
        return file;
    }

    /**
     * Create new spreadsheet with the given name
     *
     * @param spreadsheet the spreadsheet object
     * @param worksheetName the worksheet name
     * @param colCount worksheet column count
     * @param rowCount worksheet row count
     * @throws ServiceException when the request causes an error in the Google
     *         Spreadsheets service.
     * @throws IOException when an error occurs in communication with the Google
     *         Spreadsheets service.
     */
    public WorksheetEntry createWorksheet(SpreadsheetEntry spreadsheet, String worksheetName, int colCount, int rowCount) throws IOException, ServiceException {
        WorksheetEntry worksheet = new WorksheetEntry();
        worksheet.setTitle(new PlainTextConstruct(worksheetName));
        worksheet.setColCount(colCount);
        worksheet.setRowCount(rowCount);
        URL worksheetFeedUrl = spreadsheet.getWorksheetFeedUrl();
        return service.insert(worksheetFeedUrl, worksheet);
    }

    /**
     * Sets the particular cell at row, col to the specified formula or value.
     *
     * @param row the row number, starting with 1
     * @param col the column number, starting with 1
     * @param formulaOrValue the value if it doesn't start with an '=' sign; if it
     *        is a formula, be careful that cells are specified in R1C1 format
     *        instead of A1 format.
     * @throws ServiceException when the request causes an error in the Google
     *         Spreadsheets service.
     * @throws IOException when an error occurs in communication with the Google
     *         Spreadsheets service.
     */
    public void setCell(WorksheetEntry worksheet,int row, int col, String formulaOrValue)
            throws IOException, ServiceException {
        CellEntry newEntry = new CellEntry(row, col, formulaOrValue);
        service.insert(worksheet.getCellFeedUrl(), newEntry);
    }


    /**
     * Performs a full-text search on cells.
     *
     * @param fullTextSearchString a full text search string, with space-separated
     *        keywords
     * @throws ServiceException when the request causes an error in the Google
     *         Spreadsheets service.
     * @throws IOException when an error occurs in communication with the Google
     *         Spreadsheets service.
     */
    public List<String> search(WorksheetEntry worksheet, String fullTextSearchString) throws IOException,
            ServiceException {

        List<String> result = new ArrayList<String>();
        CellQuery query = new CellQuery(worksheet.getCellFeedUrl());
        query.setFullTextQuery(fullTextSearchString);
        CellFeed feed = service.query(query, CellFeed.class);

        log.info("Results for [" + fullTextSearchString + "]");

        for (CellEntry entry : feed.getEntries()) {
            result.add(entry.getTitle().getPlainText());
        }

        return result;
    }

    /**
     * Get all cells that are in the spreadsheet.
     * @param worksheet worksheet to be queried
     * @throws ServiceException when the request causes an error in the Google
     *         Spreadsheets service.
     * @throws IOException when an error occurs in communication with the Google
     *         Spreadsheets service.
     */
    public List<CellEntry> getAllCells(WorksheetEntry worksheet) throws IOException, ServiceException {
        CellFeed feed = service.getFeed(worksheet.getCellFeedUrl(), CellFeed.class);
        List<CellEntry> result = new ArrayList<CellEntry>();
        for (CellEntry entry : feed.getEntries()) {
            if(entry.getCell().getValue() != null && (!"".equalsIgnoreCase(entry.getCell().getValue().trim()))) {
                result.add(entry);
            }
        }

        return result;
    }

    /**
     * Get a particular range of cells, limited by minimum/maximum rows and
     * columns.
     *
     * @param worksheet worksheet to be queried
     * @param minRow the minimum row, inclusive, 1-based
     * @param maxRow the maximum row, inclusive, 1-based
     * @param minCol the minimum column, inclusive, 1-based
     * @param maxCol the maximum column, inclusive, 1-based
     * @throws ServiceException when the request causes an error in the Google
     *         Spreadsheets service.
     * @throws IOException when an error occurs in communication with the Google
     *         Spreadsheets service.
     */
    public List<CellEntry> getRange(WorksheetEntry worksheet,int minRow, int maxRow, int minCol, int maxCol)
            throws IOException, ServiceException {
        List<CellEntry> result = new ArrayList<CellEntry>();
        CellQuery query = new CellQuery(worksheet.getCellFeedUrl());
        query.setMinimumRow(minRow);
        query.setMaximumRow(maxRow);
        query.setMinimumCol(minCol);
        query.setMaximumCol(maxCol);
        CellFeed feed = service.query(query, CellFeed.class);
        for (CellEntry entry : feed.getEntries()) {
            if(entry.getCell().getValue() != null && (!"".equalsIgnoreCase(entry.getCell().getValue().trim()))) {
                result.add(entry);
            }
        }
        return result;
    }

    public void updateBatch(WorksheetEntry worksheet, List<GoogleSpreadsheetCellAddress> cellAddrs) throws IOException, ServiceException {


        long startTime = System.currentTimeMillis();

        URL cellFeedUrl = worksheet.getCellFeedUrl();
        CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);

        CellFeed batchRequest = new CellFeed();

        for (GoogleSpreadsheetCellAddress cellAddr : cellAddrs) {
            CellEntry batchEntry = new CellEntry(cellAddr.row, cellAddr.col, cellAddr.idString);
            batchEntry.setId(String.format("%s/%s", cellFeedUrl.toString(), String.format("R%sC%s", cellAddr.row, cellAddr.col)));
            BatchUtils.setBatchId(batchEntry, cellAddr.idString);
            BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE);
            batchRequest.getEntries().add(batchEntry);
        }

        // Submit the update
        Link batchLink = cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
        service.setHeader("If-Match", "*");
        CellFeed batchResponse = service.batch(new URL(batchLink.getHref()), batchRequest);
        service.setHeader("If-Match", null);

        // Check the results
        boolean isSuccess = true;
        for (CellEntry entry : batchResponse.getEntries()) {
            String batchId = BatchUtils.getBatchId(entry);
            if (!BatchUtils.isSuccess(entry)) {
                isSuccess = false;
                BatchStatus status = BatchUtils.getBatchStatus(entry);
                log.info(String.format("%s failed (%s) %s", batchId, status.getReason(), status.getContent()));
            }
        }

        if(!isSuccess)
        {
            throw new ServiceException("CellFeed batch request error");
        }
    }

    public ListEntry rowToListEntry(Row row)
    {
        ListEntry listEntry = new ListEntry();
        java.lang.reflect.Field[] fields = Row.class.getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            try {
                Method method = Row.class.getMethod("get" + field.getName().toString().substring(0, 1).toUpperCase() + field.getName().toString().substring(1));
                String key = field.getName().toString();
                Object value = method.invoke(row);
                if (value != null) {
                    listEntry.getCustomElements().setValueLocal(key, value.toString());
                }
            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
            } catch (InvocationTargetException e) {
                //e.printStackTrace();
            } catch (IllegalAccessException e) {
                //e.printStackTrace();
            }
        }
        return listEntry;
    }

    public Row listEntryToRow(ListEntry row)
    {
        Row rowModel = new RowDraft();
        for (String tag : row.getCustomElements().getTags()) {
            java.lang.reflect.Field[] fields = Row.class.getDeclaredFields();
            try {
                Method method = Row.class.getMethod("set" + tag.substring(0, 1).toUpperCase() + tag.substring(1), String.class);
                method.invoke(rowModel, row.getCustomElements().getValue(tag));
            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
            } catch (InvocationTargetException e) {
                //e.printStackTrace();
            } catch (IllegalAccessException e) {
                //e.printStackTrace();
            }
        }
        return rowModel;
    }
}