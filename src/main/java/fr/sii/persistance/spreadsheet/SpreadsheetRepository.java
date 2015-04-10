package fr.sii.persistance.spreadsheet;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.util.List;

/**
 * Created by tmaugin on 02/04/2015.
 */
public interface SpreadsheetRepository {
    void login(String username, String password)
            throws AuthenticationException;

    RowModel addRow(String spreadsheetName, String worksheetName, RowModel row) throws IOException, ServiceException;
    List<RowModel> getRows(String spreadsheetName, String worksheetName) throws IOException, ServiceException;
    List<RowModel> deleteRows(String spreadsheetName, String worksheetName) throws IOException, ServiceException;
}