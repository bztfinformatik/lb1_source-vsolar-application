package ch.bztf.vsolarterraform;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class DatabaseHandler {

    private File databaseConnectionFile = new File(
            Terraform.class.getClassLoader().getResource("database.connection").getFile());

    public void setupConnectFile(String host) throws IOException {
        Files.write(databaseConnectionFile.toPath(), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
        BufferedWriter writer = new BufferedWriter(new FileWriter(databaseConnectionFile, true));

        host = host.replaceAll("\"", "");

        writer.write(host);
        writer.close();
    }

    public String getHostConnection() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(databaseConnectionFile));
        String host = reader.readLine();
        reader.close();

        return host;
    }

    public Map<String, String> getCredentials() throws MalformedURLException, IOException {
        URL url = new URL("http://" + getHostConnection() + "/api/mongo/values");

        System.out.println(url.toString());

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        JSONObject jsonObject = new JSONObject(response.toString());

        Map<String, String> output = new HashMap<>();
        output.put("user", jsonObject.getString("user"));
        output.put("password", jsonObject.getString("password"));
        output.put("vsphereServer", jsonObject.getString("vsphereServer"));

        connection.disconnect();

        return output;
    }
}
