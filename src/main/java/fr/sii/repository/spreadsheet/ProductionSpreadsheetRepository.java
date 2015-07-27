package fr.sii.repository.spreadsheet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.RefreshTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.appengine.api.datastore.*;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;
import fr.sii.config.application.ApplicationSettings;
import fr.sii.config.global.GlobalSettings;
import fr.sii.config.google.GoogleSettings;
import fr.sii.config.spreadsheet.SpreadsheetSettings;
import fr.sii.domain.exception.ForbiddenException;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.spreadsheet.GoogleSpreadsheetCellAddress;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.domain.user.UserProfil;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tmaugin on 02/04/2015.
 */
public class ProductionSpreadsheetRepository implements SpreadsheetRepository {

    private static Logger logger = Logger.getLogger(ProductionSpreadsheetRepository.class.getName());

    /**
     * Our view of Google Spreadsheets as an authenticated Google user.
     */
    private SpreadsheetService service;
    private Drive driveService;
    private SpreadsheetConnector spreadsheetConnector;

    private TokenResponse accessToken;

    private SpreadsheetEntry spreadsheet;
    private WorksheetEntry worksheet;

    /**
     * A factory that generates the appropriate feed URLs.
     */
    private FeedURLFactory factory;

    private GlobalSettings globalSettings;

    private SpreadsheetSettings spreadsheetSettings;

    private GoogleSettings googleSettings;

    private ApplicationSettings applicationSettings;

    public void setApplicationSettings(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }

    public void setGoogleSettings(GoogleSettings googleSettings) {
        this.googleSettings = googleSettings;
    }

    public void setSpreadsheetSettings(SpreadsheetSettings spreadsheetSettings) {
        this.spreadsheetSettings = spreadsheetSettings;
    }

    public void setGlobalSettings(GlobalSettings globalSettings) {
        this.globalSettings = globalSettings;
    }

    private Map<String,String> getTokenInfo(TokenResponse token)
    {
        RestTemplate restTemplate = new RestTemplate();
        String tokenInfo;
        try
        {
            tokenInfo = restTemplate.getForObject("https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=" + token.getAccessToken(),String.class);
        }catch (HttpClientErrorException e)
        {
            return new HashMap<String,String>();
        }
        Map<String,String> map = new HashMap<String,String>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            //convert JSON string to Map
            map = mapper.readValue(tokenInfo, new TypeReference<HashMap<String,String>>(){});
        } catch (Exception e) {
        }
        return map;
    }

    private boolean isTokenExpired(TokenResponse token)
    {
        if(token == null)
            return true;
        Map<String,String> tokenInfo = getTokenInfo(token);
        String expiration = tokenInfo.get("expires_in");
        if(expiration != null)
        {
            Integer expirationParsed = Integer.valueOf(expiration);
            return !(expirationParsed > 10);
        }
        return true;
    }

    private void checkExpiredAccessToken() throws EntityNotFoundException, IOException, ServiceException {
        if(isTokenExpired(accessToken))
        {
            login();
        }
    }

    /**
     * Login to google services (set applicationSettings configured option according to the connexion state)
     * @throws EntityNotFoundException
     * @throws ServiceException
     * @throws IOException
     */
    @Override
    public void login() throws EntityNotFoundException, ServiceException, IOException {
        try {
            if (globalSettings.getDatabaseLoaded().equals("false"))
                return;
            String accessTokenUrl = "https://accounts.google.com/o/oauth2/token";
            // Authenticate
            HttpTransport httpTransport = new UrlFetchTransport();
            JsonFactory jsonFactory = new JacksonFactory();
            Key spreadsheetTokenKey = KeyFactory.createKey("Token", "Spreadsheet");
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Entity refreshToken = datastore.get(spreadsheetTokenKey);
            String parsedRefreshToken = (String) refreshToken.getProperty("refresh_token");

            TokenResponse response =
                    new RefreshTokenRequest(
                            new NetHttpTransport(),
                            new JacksonFactory(),
                            new GenericUrl(accessTokenUrl), parsedRefreshToken)
                            .setGrantType("refresh_token")
                            .setRefreshToken(parsedRefreshToken)
                            .setClientAuthentication(
                                    new BasicAuthentication(googleSettings.getClientId(), googleSettings.getClientSecret()))
                            .execute();

            accessToken = response;

            GoogleCredential credential = new GoogleCredential.Builder()
                    .setJsonFactory(jsonFactory)
                    .setTransport(httpTransport)
                    .setClientSecrets(googleSettings.getClientId(), googleSettings.getClientSecret()).build()
                    .setFromTokenResponse(response);

            service.setOAuth2Credentials(credential);

            // Check if token valid
            if(!isTokenExpired(response)){
                try {
                    driveService = getDriveService(credential);
                    spreadsheetConnector = new SpreadsheetConnector(service, driveService);
                    setWorksheet(spreadsheetSettings.getSpreadsheetName(),spreadsheetSettings.getWorksheetName());
                    applicationSettings.setConfigured(true);
                } catch (IOException | ServiceException | EntityNotFoundException e) {
                    logger.log(Level.WARNING, "Google Drive account not linked");
                    logger.log(Level.WARNING, e.getMessage());
                    applicationSettings.setConfigured(false);
                    e.printStackTrace();
                }
            }
            else
            {
                logger.log(Level.WARNING, "Google Drive account not linked");
                applicationSettings.setConfigured(false);
            }
        } catch(EntityNotFoundException | IOException e){
            logger.log(Level.WARNING, "Google Drive account not linked");
            logger.log(Level.WARNING, e.getMessage());
            applicationSettings.setConfigured(false);
            throw e;
        }
    }

    private Drive getDriveService(GoogleCredential credential)
    {
        return new Drive.Builder(new NetHttpTransport(), new com.google.api.client.json.jackson2.JacksonFactory(), credential)
                .setApplicationName("CallForPaper-v3")
                .build();
    }

    /**
     * Login to google services unsing given refresh token (set applicationSettings configured option according to the connexion state)
     * @param refreshToken
     * @throws IOException
     * @throws ServiceException
     */
    @Override
    public void login(String refreshToken) throws IOException, ServiceException {
        try{
            String accessTokenUrl = "https://accounts.google.com/o/oauth2/token";
            // Authenticate
            HttpTransport httpTransport = new UrlFetchTransport();
            JsonFactory jsonFactory = new JacksonFactory();
            TokenResponse response =
                    new RefreshTokenRequest(
                            new NetHttpTransport(),
                            new JacksonFactory(),
                            new GenericUrl(accessTokenUrl),refreshToken)
                            .setGrantType("refresh_token")
                            .setRefreshToken(refreshToken)
                            .setClientAuthentication(
                                    new BasicAuthentication(googleSettings.getClientId(), googleSettings.getClientSecret()))
                            .execute();

            accessToken = response;

            GoogleCredential credential = new GoogleCredential.Builder()
                    .setJsonFactory(jsonFactory)
                    .setTransport(httpTransport)
                    .setClientSecrets(googleSettings.getClientId(), googleSettings.getClientSecret()).build()
                    .setFromTokenResponse(response);

            service.setOAuth2Credentials(credential);

            // Check if token valid
            if(!isTokenExpired(response)){
                try {
                    driveService = getDriveService(credential);
                    spreadsheetConnector = new SpreadsheetConnector(service, driveService);
                    setWorksheet(spreadsheetSettings.getSpreadsheetName(), spreadsheetSettings.getWorksheetName());
                    applicationSettings.setConfigured(true);
                } catch (IOException | ServiceException | EntityNotFoundException e) {
                    logger.log(Level.WARNING, "Google Drive account not linked");
                    logger.log(Level.WARNING, e.getMessage());
                    applicationSettings.setConfigured(false);
                    e.printStackTrace();
                }
            }
            else
            {
                logger.log(Level.WARNING, "Google Drive account not linked");
                applicationSettings.setConfigured(false);
            }
        } catch(IOException e){
            logger.log(Level.WARNING, "Google Drive account not linked");
            logger.log(Level.WARNING, e.getMessage());
            applicationSettings.setConfigured(false);
            throw e;
        }
    }

    public ProductionSpreadsheetRepository() {
        this.service = new SpreadsheetService("CallForPaper-v3");
        service.setConnectTimeout(120000);
        this.factory = FeedURLFactory.getDefault();
    }

    public void setWorksheet(String spreadsheetName,String worksheetName) throws IOException, ServiceException, EntityNotFoundException {
        checkExpiredAccessToken();
        // Get the spreadsheet to load
        SpreadsheetFeed feed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();
        for (int i = 0; i < spreadsheets.size(); i++) {
            BaseEntry entry = (BaseEntry) spreadsheets.get(i);
            if (spreadsheetName.equals(entry.getTitle().getPlainText())) {
                spreadsheet = spreadsheets.get(i);
            }
        }

        // Not existing, creating it
        if(spreadsheet == null)
        {
            File newSpreadsheet = spreadsheetConnector.insertFile(spreadsheetName,"Call For Paper spreadsheet",null,"application/vnd.google-apps.spreadsheet");
            String spreadsheetURL = "https://spreadsheets.google.com/feeds/spreadsheets/" + newSpreadsheet.getId();
            spreadsheet = service.getEntry(new URL(spreadsheetURL), SpreadsheetEntry.class);
        }

        // Get worksheet by given name.
        WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
        for (int i = 0; i < worksheets.size(); i++) {
            BaseEntry entry = (BaseEntry) worksheets.get(i);
            if (worksheetName.equals(entry.getTitle().getPlainText())) {
                worksheet = worksheets.get(i);
            }
        }

        // Not existing, creating it
        if(worksheet == null)
        {
            worksheet = spreadsheetConnector.createWorksheet(spreadsheet,worksheetName, 100, 100);
        }


        // No/Bad header existing, creating it
        if(spreadsheetConnector.getRange(worksheet,1,1,1,Row.fields.size()).size() != Row.fields.size())
        {
            List<GoogleSpreadsheetCellAddress> cellAddrs = new ArrayList<GoogleSpreadsheetCellAddress>();
            for (int i = 1; i <= Row.fields.size(); i++) {
                cellAddrs.add(new GoogleSpreadsheetCellAddress(1, i, Row.fields.get(i-1)));
            }
            spreadsheetConnector.updateBatch(worksheet,cellAddrs);
        }
    }

    @Override
    public Row addRow(Row postedRow) throws IOException, ServiceException, EntityNotFoundException {
        checkExpiredAccessToken();
      // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

        if(postedRow.getAdded() == null)
        {
            postedRow.setAdded(new Date());
        }
        // Create a local representation of the new row.
        ListEntry row;
        row = spreadsheetConnector.rowToListEntry(postedRow);
        // Send the new row to the API for insertion.
        service.insert(listFeedUrl, row);
        return postedRow;
    }

    @Override
    public List<Row> getRows() throws IOException, ServiceException, EntityNotFoundException {
        checkExpiredAccessToken();
        if(spreadsheet == null)
        {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        List<Row> rows = new ArrayList<>();

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            Row rowModel = spreadsheetConnector.listEntryToRow(row);
            rows.add(rowModel);
        }
        return rows;
    }

    @Override
    public Row getRow(String added) throws IOException, ServiceException, NotFoundException, EntityNotFoundException {
        checkExpiredAccessToken();
        if(spreadsheet == null)
        {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        List<Row> rows = new ArrayList<>();

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            // Iterate over the remaining columns, and print each cell value
            Row rowModel = spreadsheetConnector.listEntryToRow(row);
            if(rowModel.getAdded().toString().equals(added))
                rows.add(rowModel);
        }
        if(rows.size() > 0)
            return rows.get(0);
        else
            throw new NotFoundException("Session not found");
    }

    @Override
    public Row getRow(String added, Long userId) throws IOException, ServiceException, ForbiddenException, NotFoundException, EntityNotFoundException {
        checkExpiredAccessToken();
        if(spreadsheet == null)
        {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        List<Row> rows = new ArrayList<>();

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            // Iterate over the remaining columns, and print each cell value
            Row rowModel = spreadsheetConnector.listEntryToRow(row);
            if(rowModel.getAdded().toString().equals(added))
            {
                if(rowModel.getUserId().toString().equals(userId.toString()))
                    rows.add(rowModel);
                else
                    throw new ForbiddenException("Forbidden");
            }

        }
        if(rows.size() > 0)
            return rows.get(0);
        else
            throw new NotFoundException("Session not found");
    }

    @Override
    public List<Row> getRowsSession() throws IOException, ServiceException, EntityNotFoundException {
        checkExpiredAccessToken();
        if(spreadsheet == null)
        {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        List<Row> rows = new ArrayList<>();

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            // Iterate over the remaining columns, and print each cell value
            Row rowModel = spreadsheetConnector.listEntryToRow(row);
            if(!rowModel.getDraft())
                rows.add(rowModel);
        }
        return rows;
    }

    @Override
    public List<Row> getRowsSession(Long userId) throws IOException, ServiceException, EntityNotFoundException {
        checkExpiredAccessToken();
        if(spreadsheet == null) {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        List<Row> rows = new ArrayList<>();

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            // Iterate over the remaining columns, and print each cell value
            Row rowModel = spreadsheetConnector.listEntryToRow(row);
            if(!rowModel.getDraft() && rowModel.getUserId().toString().equals(userId.toString()))
                rows.add(rowModel);
        }
        return rows;
    }

    @Override
    public List<Row> getRowsDraft() throws IOException, ServiceException, EntityNotFoundException {
        checkExpiredAccessToken();
        if(spreadsheet == null) {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        List<Row> rows = new ArrayList<>();

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            // Iterate over the remaining columns, and print each cell value
            Row rowModel = spreadsheetConnector.listEntryToRow(row);
            if(rowModel.getDraft())
                rows.add(rowModel);
        }
        return rows;
    }

    @Override
    public List<Row> getRowsDraft(Long userId) throws IOException, ServiceException, EntityNotFoundException {
        checkExpiredAccessToken();
        if(spreadsheet == null) {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        List<Row> rows = new ArrayList<>();

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            // Iterate over the remaining columns, and print each cell value
            Row rowModel = spreadsheetConnector.listEntryToRow(row);
            if(rowModel.getDraft() && rowModel.getUserId().toString().equals(userId.toString()))
                rows.add(rowModel);
        }
        return rows;
    }

    @Override
    public List<Row> deleteRows() throws IOException, ServiceException, EntityNotFoundException {
        checkExpiredAccessToken();
        if(spreadsheet == null)
        {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Delete the rows using the API.
        List<ListEntry> rows = listFeed.getEntries();
        for (int i = rows.size()-1; i >= 0; i--) {
            rows.get(i).delete();
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteRowDraft(String added, Long userId) throws IOException, ServiceException, ForbiddenException, NotFoundException, EntityNotFoundException {
        checkExpiredAccessToken();
        if(spreadsheet == null) {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        List<Row> rows = new ArrayList<>();

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            // Iterate over the remaining columns, and print each cell value
            Row rowModel = spreadsheetConnector.listEntryToRow(row);
            if(rowModel.getAdded().toString().equals(added) && rowModel.getDraft())
            {
                if(rowModel.getUserId().toString().equals(userId.toString()))
                {
                    row.delete();
                    return;
                }
                else
                    throw new ForbiddenException("Forbidden");
            }

        }
        throw new NotFoundException("Draft not found");
    }

    @Override
    public void deleteRow(String added) throws IOException, ServiceException, NotFoundException, EntityNotFoundException {
        checkExpiredAccessToken();
        if(spreadsheet == null) {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        List<Row> rows = new ArrayList<>();

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            // Iterate over the remaining columns, and print each cell value
            Row rowModel = spreadsheetConnector.listEntryToRow(row);
            if(rowModel.getAdded().toString().equals(added))
            {
                row.delete();
                return;
            }

        }
        throw new NotFoundException("Session not found");
    }

    @Override
    public Row putRowDraft(Row rowToPut, Long userId, Long added) throws IOException, ServiceException, ForbiddenException, NotFoundException, EntityNotFoundException {
        checkExpiredAccessToken();
        if(spreadsheet == null) {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        List<Row> rows = new ArrayList<>();

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            // Iterate over the remaining columns, and print each cell value
            Row rowModel = spreadsheetConnector.listEntryToRow(row);
            if(rowModel.getAdded().toString().equals(added.toString()) && rowModel.getDraft())
            {
                if(rowModel.getUserId().toString().equals(userId.toString()))
                {
                    // updating row
                    java.lang.reflect.Field[] fields = Row.class.getDeclaredFields();
                    for (java.lang.reflect.Field field : fields) {
                        try {
                            Method method = Row.class.getMethod("get" + field.getName().toString().substring(0, 1).toUpperCase() + field.getName().toString().substring(1));
                            String key = field.getName().toString();
                            Object value = method.invoke(rowToPut);
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
                    row.update();
                    return rowToPut;
                }
                else
                    throw new ForbiddenException("Forbidden");
            }
        }
        throw new NotFoundException("Draft not found");
    }

    @Override
    public Row putRowDraftToSession(Row rowToPut, Long userId, Long added) throws IOException, ServiceException, ForbiddenException, NotFoundException, EntityNotFoundException {
        checkExpiredAccessToken();
        if(spreadsheet == null) {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        List<Row> rows = new ArrayList<>();

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            Row rowModel = spreadsheetConnector.listEntryToRow(row);
            if(rowModel.getAdded().toString().equals(added.toString()) && rowModel.getDraft())
            {
                if(rowModel.getUserId().toString().equals(userId.toString()))
                {
                    // change draft to session
                    rowToPut.setDraft(false);
                    // updating row
                    java.lang.reflect.Field[] fields = Row.class.getDeclaredFields();
                    for (java.lang.reflect.Field field : fields) {
                        try {
                            Method method = Row.class.getMethod("get" + field.getName().toString().substring(0, 1).toUpperCase() + field.getName().toString().substring(1));
                            String key = field.getName().toString();
                            Object value = method.invoke(rowToPut);
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
                    row.update();
                    return rowToPut;
                }
                else
                    throw new ForbiddenException("Forbidden");
            }
        }
        throw new NotFoundException("Draft not found");
    }

    @Override
    public void updateProfilSessions(Row rowContainingProfil, Long userId) throws EntityNotFoundException, ServiceException, IOException {
        checkExpiredAccessToken();
        if(spreadsheet == null) {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            // Iterate over the remaining columns, and print each cell value
            Row rowModel = spreadsheetConnector.listEntryToRow(row);
            if(!rowModel.getDraft() && rowModel.getUserId().toString().equals(userId.toString())) {
                // update profile
                boolean modified = false;
                if(rowContainingProfil.getName() != rowModel.getName()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("name", rowContainingProfil.getName() != null ? rowContainingProfil.getName() : "");
                }
                if(rowContainingProfil.getFirstname() != rowModel.getFirstname()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("firstName", rowContainingProfil.getFirstname() != null ? rowContainingProfil.getFirstname() : "");
                }
                if(rowContainingProfil.getPhone() != rowModel.getPhone()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("phone", rowContainingProfil.getPhone() != null ? rowContainingProfil.getPhone() : "");
                }
                if(rowContainingProfil.getCompany() != rowModel.getCompany()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("company", rowContainingProfil.getCompany() != null ? rowContainingProfil.getCompany() : "");
                }
                if(rowContainingProfil.getBio() != rowModel.getBio()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("bio", rowContainingProfil.getBio() != null ? rowContainingProfil.getBio() : "");
                }
                if(rowContainingProfil.getTwitter() != rowModel.getTwitter()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("twitter", rowContainingProfil.getTwitter() != null ? rowContainingProfil.getTwitter() : "");
                }
                if(rowContainingProfil.getGooglePlus() != rowModel.getGooglePlus()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("googlePlus", rowContainingProfil.getGooglePlus() != null ? rowContainingProfil.getGooglePlus() : "");
                }
                if(rowContainingProfil.getGithub() != rowModel.getGithub()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("github", rowContainingProfil.getGithub() != null ? rowContainingProfil.getGithub() : "");
                }
                if(rowContainingProfil.getSocial() != rowModel.getSocial()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("social", rowContainingProfil.getSocial() != null ? rowContainingProfil.getSocial() : "");
                }
                if(modified) row.update();
            }
        }
        return;
    }

    @Override
    public void updateProfilSessions(UserProfil userProfil, Long userId) throws ServiceException, IOException, EntityNotFoundException {
        checkExpiredAccessToken();
        if(spreadsheet == null) {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            // Iterate over the remaining columns, and print each cell value
            Row rowModel = spreadsheetConnector.listEntryToRow(row);
            if(!rowModel.getDraft() && rowModel.getUserId().toString().equals(userId.toString())) {
                // update profile
                boolean modified = false;
                if(rowModel.getName() != userProfil.getName()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("name", userProfil.getName() != null ? userProfil.getName() : "");
                }
                if(rowModel.getFirstname() != userProfil.getFirstname()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("firstName", userProfil.getFirstname() != null ? userProfil.getFirstname() : "");
                }
                if(rowModel.getPhone() != userProfil.getPhone()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("phone", userProfil.getPhone() != null ? userProfil.getPhone() : "");
                }
                if(rowModel.getCompany() != userProfil.getCompany()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("company", userProfil.getCompany() != null ? userProfil.getCompany() : "");
                }
                if(rowModel.getBio() != userProfil.getBio()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("bio", userProfil.getBio() != null ? userProfil.getBio() : "");
                }
                if(rowModel.getTwitter() != userProfil.getTwitter()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("twitter", userProfil.getTwitter() != null ? userProfil.getTwitter() : "");
                }
                if(rowModel.getGooglePlus() != userProfil.getGooglePlus()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("googlePlus", userProfil.getGooglePlus() != null ? userProfil.getGooglePlus() : "");
                }
                if(rowModel.getGithub() != userProfil.getGithub()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("github", userProfil.getGithub() != null ? userProfil.getGithub() : "");
                }
                if(rowModel.getSocial() != userProfil.getSocial()) {
                    modified = true;
                    row.getCustomElements().setValueLocal("social", userProfil.getSocial() != null ? userProfil.getSocial() : "");
                }

                if(modified) row.update();
            }
        }
        return;
    }

    public Row changeRowTrack(Long added, String track) throws EntityNotFoundException, ServiceException, IOException, NotFoundException {
        checkExpiredAccessToken();
        if(spreadsheet == null) {
            throw new ServiceException("Spreadsheet doesn't exists");
        }
        if(worksheet == null)
        {
            throw new ServiceException("Worksheet doesn't exists");
        }

        List<Row> rows = new ArrayList<>();

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
        if(listFeed == null)
        {
            throw new ServiceException("ListFeed doesn't exists");
        }

        // Iterate through each row, printing its cell values.
        for (ListEntry row : listFeed.getEntries()) {
            Row rowModel = spreadsheetConnector.listEntryToRow(row);
            if(rowModel.getAdded().toString().equals(added.toString()))
            {
                    // change draft to session
                    rowModel.setTrack(track);
                    // updating row
                    java.lang.reflect.Field[] fields = Row.class.getDeclaredFields();
                    for (java.lang.reflect.Field field : fields) {
                        try {
                            Method method = Row.class.getMethod("get" + field.getName().toString().substring(0, 1).toUpperCase() + field.getName().toString().substring(1));
                            String key = field.getName().toString();
                            Object value = method.invoke(rowModel);
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
                    row.update();
                    return rowModel;
            }
        }
        throw new NotFoundException("Row not found");
    }
}
