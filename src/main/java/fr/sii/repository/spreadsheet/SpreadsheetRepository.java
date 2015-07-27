package fr.sii.repository.spreadsheet;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gdata.util.ServiceException;
import fr.sii.domain.exception.ForbiddenException;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.domain.user.UserProfil;

import java.io.IOException;
import java.util.List;

/**
 * Created by tmaugin on 02/04/2015.
 */
public interface SpreadsheetRepository {
    void login() throws EntityNotFoundException, ServiceException, IOException;

    void login(String refreshToken) throws IOException, ServiceException;

    Row addRow(Row row) throws IOException, ServiceException, EntityNotFoundException;

    List<Row> getRows() throws IOException, ServiceException, EntityNotFoundException;

    Row getRow(String added) throws IOException, ServiceException, NotFoundException, EntityNotFoundException;

    Row getRow(String added, Long userId) throws IOException, ServiceException, ForbiddenException, NotFoundException, EntityNotFoundException;

    List<Row> getRowsSession() throws IOException, ServiceException, EntityNotFoundException;

    List<Row> getRowsSession(Long userId) throws IOException, ServiceException, EntityNotFoundException;

    List<Row> getRowsDraft() throws IOException, ServiceException, EntityNotFoundException;

    List<Row> getRowsDraft(Long userId) throws IOException, ServiceException, EntityNotFoundException;

    List<Row> deleteRows() throws IOException, ServiceException, EntityNotFoundException;

    void deleteRowDraft(String added, Long userId) throws IOException, ServiceException, ForbiddenException, NotFoundException, EntityNotFoundException;

    void deleteRow(String added) throws IOException, ServiceException, NotFoundException, EntityNotFoundException;

    Row putRowDraft(Row rowToPut, Long userId, Long added) throws IOException, ServiceException, ForbiddenException, NotFoundException, EntityNotFoundException;

    Row putRowDraftToSession(Row rowToPut, Long userId, Long added) throws IOException, ServiceException, ForbiddenException, NotFoundException, EntityNotFoundException;

    void updateProfilSessions(Row rowContainingProfil, Long userId) throws EntityNotFoundException, ServiceException, IOException;

    void updateProfilSessions(UserProfil userProfil, Long userId) throws ServiceException, IOException, EntityNotFoundException;

    Row changeRowTrack(Long added, String track) throws EntityNotFoundException, ServiceException, IOException, NotFoundException;
}