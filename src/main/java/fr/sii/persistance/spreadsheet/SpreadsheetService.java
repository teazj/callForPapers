package fr.sii.persistance.spreadsheet;

/**
 * Created by tmaugin on 02/04/2015.
 */
import com.google.gdata.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Scope("prototype")
public class SpreadsheetService {

    @Autowired
    private SpreadsheetSettings googleSettings;

    @Autowired
    private SpreadsheetRepository googleRepository;

    @Autowired
    public void login() throws ServiceException, IOException {
        googleRepository.login(googleSettings);
    }

    public RowModel addRow(RowModel row) throws ServiceException, IOException {
        return googleRepository.addRow(row);
    }

    public List<RowModel> getRows() throws IOException, ServiceException
    {
        return googleRepository.getRows();
    }

    public List<RowModel> deleteRows() throws IOException, ServiceException
    {
        return googleRepository.deleteRows();
    }
}