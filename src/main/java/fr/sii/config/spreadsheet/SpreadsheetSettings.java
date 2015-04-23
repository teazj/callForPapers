package fr.sii.config.spreadsheet;

/**
 * Created by tmaugin on 02/04/2015.
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Configuration
public class SpreadsheetSettings {
    @Value("${google.login}")
    @NotNull
    private String login;
    @Value("${google.password}")
    @NotNull
    private String password;
    @Value("${google.spreadsheetName}")
    @NotNull
    private String spreadsheetName;
    @Value("${google.worksheetName}")
    @NotNull
    private String worksheetName;

    public SpreadsheetSettings() {}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpreadsheetName() {
        return spreadsheetName;
    }

    public void setSpreadsheetName(String spreadsheetName) {
        this.spreadsheetName = spreadsheetName;
    }

    public String getWorksheetName() {
        return worksheetName;
    }

    public void setWorksheetName(String worksheetName) {
        this.worksheetName = worksheetName;
    }

    @Override
    public String toString() {
        return "SpreadsheetSettings{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", spreadsheetName='" + spreadsheetName + '\'' +
                ", worksheetName='" + worksheetName + '\'' +
                '}';
    }
}