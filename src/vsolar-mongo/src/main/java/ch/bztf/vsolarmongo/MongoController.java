package ch.bztf.vsolarmongo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mongo")
public class MongoController {

    private final Logger logger = LoggerFactory.getLogger(MongoController.class);

    @Autowired
    private VSolarService vSolarService;

    /**
     * Setzt die VSolar-Umgebung auf und speichert die Daten in der MongoDB.
     * 
     * @param payload
     * @return erstellte VSolarENV-Instanz
     */
    @PostMapping("/setup")
    public ResponseEntity<VSolarENV> setup(@RequestBody Map<String, String> payload) {
        logger.info("Received setup request. Payload: {}", payload);
        return new ResponseEntity<VSolarENV>(vSolarService.setupEnvironment(payload.get("user"),
                payload.get("password"), payload.get("vsphereServer"), payload.get("apiToken"),
                payload.get("terraformService"), payload.get("ansibleService")), HttpStatus.CREATED);
    }

    /**
     * Aktualisiert den API-Token der VSolar-Umgebung.
     * 
     * @param apiToken
     * @return aktualisierte API Token
     */
    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody String apiToken) {
        logger.info("Received update request. API Token: {}", apiToken);
        return new ResponseEntity<String>(vSolarService.updateApiToken(apiToken), HttpStatus.CREATED);
    }

    /**
     * Gibt die Werte der gesamten VSolar-Umgebung zurück.
     * 
     * @return JSON Objekt vSolar-Umgebung
     */
    @GetMapping("/values")
    public ResponseEntity<VSolarENV> value() {
        logger.info("Received value request.");
        return new ResponseEntity<VSolarENV>(vSolarService.all(), HttpStatus.OK);
    }
}
