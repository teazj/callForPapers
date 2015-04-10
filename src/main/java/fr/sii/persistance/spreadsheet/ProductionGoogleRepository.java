package fr.sii.persistance.spreadsheet;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tmaugin on 02/04/2015.
 */
public class ProductionGoogleRepository implements SpreadsheetRepository {
    /**
     * Our view of Google Spreadsheets as an authenticated Google user.
     */
    private SpreadsheetService service;

    /**
     * A factory that generates the appropriate feed URLs.
     */
    private FeedURLFactory factory;

    @Override
    public void login(String username, String password)
            throws AuthenticationException {
        // Authenticate
        service.setUserCredentials(username, password);
    }

    public ProductionGoogleRepository() {
        this.service = new SpreadsheetService("Google Spreadsheet");
        this.factory = FeedURLFactory.getDefault();
    }

    @Override
    public RowModel addRow(String spreadsheetName, String worksheetName, RowModel postedRow) throws IOException, ServiceException {
        // Get the spreadsheet to load
        SpreadsheetEntry spreadsheet = new SpreadsheetEntry();

        SpreadsheetFeed feed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();
        for (int i = 0; i < spreadsheets.size(); i++) {
            BaseEntry entry = (BaseEntry) spreadsheets.get(i);
            if (spreadsheetName.equals(entry.getTitle().getPlainText())) {
                spreadsheet = spreadsheets.get(i);
            }
        }
        // Get the first worksheet of the spreadsheet.
        WorksheetEntry worksheet = new WorksheetEntry();

        WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
        for (int i = 0; i < worksheets.size(); i++) {
            BaseEntry entry = (BaseEntry) worksheets.get(i);
            if (worksheetName.equals(entry.getTitle().getPlainText())) {
                worksheet = worksheets.get(i);
            }
        }

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

        if(postedRow.getAdded() == null)
        {
            postedRow.setAdded(new Date());
        }

        // Create a local representation of the new row.
        ListEntry row = new ListEntry();

        java.lang.reflect.Field[] fields = RowModel.class.getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            try {
                Method method = RowModel.class.getMethod("get" + WordUtils.capitalize(field.getName().toString()));
                String key = field.getName().toString();
                Object value = method.invoke(postedRow);
                if (value != null) {
                    row.getCustomElements().setValueLocal(key, value.toString());
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // Send the new row to the API for insertion.
        row = service.insert(listFeedUrl, row);
        return postedRow;
    }

    public List<RowModel> getRows(String spreadsheetName, String worksheetName) throws IOException, ServiceException {
        // Get the spreadsheet to load
        SpreadsheetEntry spreadsheet = new SpreadsheetEntry();

        SpreadsheetFeed feed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();
        for (int i = 0; i < spreadsheets.size(); i++) {
            BaseEntry entry = (BaseEntry) spreadsheets.get(i);
            if (spreadsheetName.equals(entry.getTitle().getPlainText())) {
                spreadsheet = spreadsheets.get(i);
            }
        }
        // Get the first worksheet of the spreadsheet.
        WorksheetEntry worksheet = new WorksheetEntry();

        WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
        for (int i = 0; i < worksheets.size(); i++) {
            BaseEntry entry = (BaseEntry) worksheets.get(i);
            if (worksheetName.equals(entry.getTitle().getPlainText())) {
                worksheet = worksheets.get(i);
            }
        }

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

        List<RowModel> rows = new ArrayList<>();

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            // Iterate over the remaining columns, and print each cell value
            RowModel rowModel = new RowModel();
            for (String tag : row.getCustomElements().getTags()) {
                java.lang.reflect.Field[] fields = RowModel.class.getDeclaredFields();
                try {
                    Method method = RowModel.class.getMethod("set" + WordUtils.capitalize(tag), String.class);
                    method.invoke(rowModel, row.getCustomElements().getValue(tag));
                } catch (NoSuchMethodException e) {
                    //e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            rows.add(rowModel);
        }
        return rows;
    }

    public List<RowModel> deleteRows(String spreadsheetName, String worksheetName) throws IOException, ServiceException {
        // Get the spreadsheet to load
        SpreadsheetEntry spreadsheet = new SpreadsheetEntry();

        SpreadsheetFeed feed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();
        for (int i = 0; i < spreadsheets.size(); i++) {
            BaseEntry entry = (BaseEntry) spreadsheets.get(i);
            if (spreadsheetName.equals(entry.getTitle().getPlainText())) {
                spreadsheet = spreadsheets.get(i);
            }
        }
        // Get the first worksheet of the spreadsheet.
        WorksheetEntry worksheet = new WorksheetEntry();

        WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
        for (int i = 0; i < worksheets.size(); i++) {
            BaseEntry entry = (BaseEntry) worksheets.get(i);
            if (worksheetName.equals(entry.getTitle().getPlainText())) {
                worksheet = worksheets.get(i);
            }
        }
        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

        // Delete the rows using the API.
        List<ListEntry> rows = listFeed.getEntries();
        for (int i = rows.size()-1; i >= 0; i--) {
            rows.get(i).delete();
        }
        return new ArrayList<>();
    }
}
