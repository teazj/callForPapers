package fr.sii.service.github;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sii.domain.CustomException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by tmaugin on 13/05/2015.
 */
@Service
public class GithubService {
    public String getEmail(String access_token) throws IOException, CustomException {
        String url = "https://api.github.com/user/emails";
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("Authorization", "Bearer " + access_token);
        connection.setRequestProperty("User-Agent", "Call For Paper");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String jsonString = "";

        String inputLine;
        while ((inputLine = in.readLine()) != null)
        {
            jsonString+=inputLine;
        }
        in.close();
        jsonString = jsonString.replaceAll("\r", "").replaceAll("\n", "");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseObject = mapper.readTree(jsonString);

        String email = "";
        if(responseObject.get("message") != null)
        {
            throw new CustomException(responseObject.get("message").asText());
        }
        if(responseObject.get("message") == null)
        {
            for(JsonNode node : responseObject)
            {
                if(node.get("primary").asBoolean())
                {
                    email = node.get("email").asText();
                    break;
                }
            }
        }
        return email;
    }

    public String getUserId(String access_token) throws IOException, CustomException {
        String url = "https://api.github.com/user";
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("Authorization", "Bearer " + access_token);
        connection.setRequestProperty("User-Agent", "Call For Paper");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String jsonString = "";

        String inputLine;
        while ((inputLine = in.readLine()) != null)
        {
            jsonString+=inputLine;
        }
        in.close();
        jsonString = jsonString.replaceAll("\r", "").replaceAll("\n", "");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseObject = mapper.readTree(jsonString);

        String id = "";
        if(responseObject.get("message") != null)
        {
            throw new CustomException(responseObject.get("message").asText());
        }
        if(responseObject.get("message") == null)
        {
            id = responseObject.get("id").asText();
        }
        return id;
    }
}
