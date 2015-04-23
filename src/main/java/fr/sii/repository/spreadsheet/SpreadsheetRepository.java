package fr.sii.repository.spreadsheet;

import com.google.gdata.util.ServiceException;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.config.spreadsheet.SpreadsheetSettings;

import java.io.IOException;
import java.util.List;

/**
 * Created by tmaugin on 02/04/2015.
 */
public interface SpreadsheetRepository {
    void login(SpreadsheetSettings s) throws ServiceException, IOException;
    Row addRow(Row row) throws IOException, ServiceException;
    List<Row> getRows() throws IOException, ServiceException;
    List<Row> deleteRows() throws IOException, ServiceException;
}