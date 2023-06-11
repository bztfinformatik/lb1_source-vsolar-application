package ch.bztf.vsolarterraform;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.bertramlabs.plugins.hcl4j.HCLParser;
import com.bertramlabs.plugins.hcl4j.HCLParserException;

public class Terraform {

    private static Terraform terraformSingleton;
    DatabaseHandler dbhandler = new DatabaseHandler();
    private Process process;
    private File terraformENV = new File(Terraform.class.getClassLoader().getResource("terraform").getFile());
    File terraformAutoVarFile = new File(
            Terraform.class.getClassLoader().getResource("terraform/terraform.auto.tfvars").getFile());
    private final Logger logger = LoggerFactory.getLogger(Terraform.class);

    private Terraform() {
        // Singleton
    }

    /**
     * Gibt die Singleton Instanz des Terraform Objekts zurück
     * 
     * @return terraformSingleton
     */
    public static Terraform getInstance() {
        if (terraformSingleton == null) {
            terraformSingleton = new Terraform();
        }
        return terraformSingleton;
    }

    /**
     * Übergebene Werte werden in das Terraform.Auto.Vars File abgespeichert
     * 
     * @param vm
     * @throws HCLParserException
     * @throws IOException
     */
    public void setConfiguration(VirtualMachine vm) throws HCLParserException, IOException {
        Map<String, Object> tfVars = new HCLParser().parse(terraformAutoVarFile, "UTF-8");

        // File wird vorher geleert
        Files.write(terraformAutoVarFile.toPath(), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);

        tfVars.put("vsphereUser", dbhandler.getCredentials().get("user"));
        tfVars.put("vspherePassword", dbhandler.getCredentials().get("password"));
        tfVars.put("vsphereServer", dbhandler.getCredentials().get("vsphereServer"));
        tfVars.put("vsphereDatacenter", vm.getVsphereDatacenter());
        tfVars.put("vsphereHost", vm.getVsphereHost());
        tfVars.put("vmDatastore", vm.getVmDatastore());
        tfVars.put("vmCluster", vm.getVmCluster());
        tfVars.put("vmTemplate", vm.getVmTemplate());
        tfVars.put("vmNetwork", vm.getVmNetwork());
        tfVars.put("vmName", vm.getVmName());
        tfVars.put("vmMemory", vm.getVmMemory());
        tfVars.put("vmCpu", vm.getVmCpu());

        BufferedWriter writer = new BufferedWriter(new FileWriter(terraformAutoVarFile, true));

        // Bildung von: key = value
        for (Map.Entry<String, Object> entry : tfVars.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            writer.write(key + " = \"" + value + "\"\n");
        }

        // logging
        logger.info("Setting Terraform configuration...");
        logger.debug("tfVars: {}", tfVars);

        writer.close();
    }

    /**
     * to-do
     * Rückgabe der Konfiguration
     */
    public void getConfiguration() {

    }

    /**
     * Ausführung eines Terraformscommands in der Ressourceumgebung
     * 
     * @param emitter
     * @param command
     * @throws IOException
     */
    public void execute(SseEmitter emitter, String command) throws IOException {
        // Ausführung Command
        process = Runtime.getRuntime().exec(command, null, terraformENV);
        // logging
        logger.info("Executing Terraform command: {}", command);

        // Emitter empfängt den KonsolenOutputStream
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.isEmpty()) {
                emitter.send(SseEmitter.event().data(line));
            }
        }

        emitter.complete();
    }

}
