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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseHandler {

    private File databaseConnectionFile = new File(
            Terraform.class.getClassLoader().getResource("database.connection").getFile());
    private final Logger logger = LoggerFactory.getLogger(DatabaseHandler.class);

    /**
     * Speicherung IP-Adresse des ongoDB-Java-Services in eine Datei
     * 
     * @param host
     * @throws IOException
     */
    public void setupConnectFile(String host) throws IOException {
        // File wird vorher geleert
        Files.write(databaseConnectionFile.toPath(), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);

        // logging
        logger.info("Setting up database connection file...");

        // Erstellung des Writers
        BufferedWriter writer = new BufferedWriter(new FileWriter(databaseConnectionFile, true));

        // Entfernung der Anführungszeichen
        host = host.replaceAll("\"", "");

        writer.write(host);
        writer.close();
    }

    /**
     * Gibt die IP-Adresse des MongoDB-Java-Services zurück
     * 
     * @return host
     * @throws IOException
     */
    public String getHostConnection() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(databaseConnectionFile));
        String host = reader.readLine();
        reader.close();

        // logging
        logger.info("Retrieving host connection from file: {}", host);

        return host;
    }

    /**
     * Filtert aus dem Request den Usernamen, Passwort und Ip heraus
     * 
     * @return output
     * @throws MalformedURLException
     * @throws IOException
     */
    public Map<String, String> getCredentials() throws MalformedURLException, IOException {
        URL url = new URL("http://" + getHostConnection() + "/api/mongo/values");

        // Aufbauung der Verbindung
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Antwort wird gestreamt
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();

        String line;

        // Jede Zeile des Streams wird eingelesen und abgespeichert
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        // Schliessung des Streams
        reader.close();

        // Response wird in JSON Objekt formatiert
        JSONObject jsonObject = new JSONObject(response.toString());

        // Speicherung Daten in Collection für Rückgabe
        Map<String, String> output = new HashMap<>();
        output.put("user", jsonObject.getString("user"));
        output.put("password", jsonObject.getString("password"));
        output.put("vsphereServer", jsonObject.getString("vsphereServer"));

        // logging
        logger.info("Fetching credentials from MongoDB-Java-Service...");

        // Auflösungd er Verbindung
        connection.disconnect();

        return output;
    }
}
