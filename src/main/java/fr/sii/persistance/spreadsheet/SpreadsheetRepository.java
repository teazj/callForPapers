package fr.sii.persistance.spreadsheet;

import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.util.List;

/**
 * Created by tmaugin on 02/04/2015.
 */
public interface SpreadsheetRepository {
    void login(SpreadsheetSettings s) throws ServiceException, IOException;
    RowModel addRow(RowModel row) throws IOException, ServiceException;
    List<RowModel> getRows() throws IOException, ServiceException;
    List<RowModel> deleteRows() throws IOException, ServiceException;
}