package fr.sii.service.spreadsheet;

/**
 * Created by tmaugin on 02/04/2015.
 */
import com.google.gdata.util.ServiceException;
import fr.sii.config.spreadsheet.SpreadsheetSettings;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.repository.spreadsheet.SpreadsheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SpreadsheetService {

    @Autowired
    private SpreadsheetSettings googleSettings;

    @Autowired
    private SpreadsheetRepository googleRepository;

    @Autowired
    public void login() throws ServiceException, IOException {
        googleRepository.login(googleSettings);
    }

    public Row addRow(Row row) throws ServiceException, IOException {
        return googleRepository.addRow(row);
    }

    public List<Row> getRows() throws IOException, ServiceException
    {
        return googleRepository.getRows();
    }

    public List<Row> deleteRows() throws IOException, ServiceException
    {
        return googleRepository.deleteRows();
    }
}