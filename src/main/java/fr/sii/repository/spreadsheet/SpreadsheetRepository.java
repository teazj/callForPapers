package fr.sii.repository.spreadsheet;

import com.google.gdata.util.ServiceException;
import fr.sii.config.spreadsheet.SpreadsheetSettings;
import fr.sii.domain.spreadsheet.Row;

import java.io.IOException;
import java.util.List;

/**
 * Created by tmaugin on 02/04/2015.
 */
public interface SpreadsheetRepository {
    void login(SpreadsheetSettings s) throws ServiceException, IOException;
    Row addRow(Row row) throws IOException, ServiceException;
    List<Row> getRows() throws IOException, ServiceException;
    Row getRow(String added) throws IOException, ServiceException;

    Row getRow(String added, Long userId) throws IOException, ServiceException;

    List<Row> getRowsSession() throws IOException, ServiceException;

    List<Row> getRowsSession(Long userId) throws IOException, ServiceException;

    List<Row> getRowsDraft() throws IOException, ServiceException;

    List<Row> getRowsDraft(Long userId) throws IOException, ServiceException;

    List<Row> deleteRows() throws IOException, ServiceException;

    void deleteRowDraft(String added, Long userId) throws IOException, ServiceException;

    Row putRowDraft(Row rowToPut, Long userId, Long added) throws IOException, ServiceException;

    Row putRowDraftToSession(Row rowToPut, Long userId, Long added) throws IOException, ServiceException;
}