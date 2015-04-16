package fr.sii.persistance.spreadsheet;

/**
 * Created by tmaugin on 16/04/2015.
 */
        import java.io.IOException;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.List;

        import com.google.gdata.client.docs.DocsService;
        import com.google.gdata.data.PlainTextConstruct;
        import com.google.gdata.data.docs.DocumentListEntry;
        import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
        import org.apache.commons.logging.Log;
        import org.apache.commons.logging.LogFactory;

        import com.google.gdata.client.spreadsheet.CellQuery;
        import com.google.gdata.client.spreadsheet.SpreadsheetService;
        import com.google.gdata.data.spreadsheet.CellEntry;
        import com.google.gdata.data.spreadsheet.CellFeed;
        import com.google.gdata.data.spreadsheet.WorksheetEntry;
        import com.google.gdata.util.ServiceException;

/**
 *
 *
 * Class implemented for accessing google spreadsheet application programming interface (API) features related to cell data, worksheet and spreadsheet
 *
 */

public class SpreadsheetConnector {

    private SpreadsheetService service;
    private DocsService docsService;
    private static Log log = LogFactory
            .getLog(SpreadsheetConnector.class);

    public SpreadsheetConnector(SpreadsheetService service, DocsService docsService) {
        this.service = service;
        this.docsService = docsService;
    }


    /**
     * Create new spreadsheet with the given name
     *
     * @param spreadsheetName the spreadsheet name
     * @throws ServiceException when the request causes an error in the Google
     *         Spreadsheets service.
     * @throws IOException when an error occurs in communication with the Google
     *         Spreadsheets service.
     */
    public DocumentListEntry createSpreadsheet(String spreadsheetName) throws IOException, ServiceException {
        URL GOOGLE_DRIVE_FEED_URL = new URL("https://docs.google.com/feeds/default/private/full/");
        DocumentListEntry documentListEntry = new com.google.gdata.data.docs.SpreadsheetEntry();
        documentListEntry.setTitle(new PlainTextConstruct(spreadsheetName));
        documentListEntry = docsService.insert(GOOGLE_DRIVE_FEED_URL, documentListEntry);
        log.info(String.format("Spreadsheet %s created",spreadsheetName));
        return documentListEntry;
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
}