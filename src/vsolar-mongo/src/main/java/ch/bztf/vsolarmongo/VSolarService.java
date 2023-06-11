package ch.bztf.vsolarmongo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VSolarService {

    private final Logger logger = LoggerFactory.getLogger(MongoController.class);

    @Autowired
    VSolarRepository vSolarRepository;

    /**
     * Erstellt in der Datenbank mit den erhaltenen Angaben eine vSOlar Enitität
     * ein, weche für die Lauzeitumgebung vonnöten sind.
     * 
     * @param user
     * @param password
     * @param vsphereServer
     * @param apiToken
     * @param terraformService
     * @param ansibleService
     * @return vSolarENV
     */
    public VSolarENV setupEnvironment(String user, String password, String vsphereServer, String apiToken,
            String terraformService,
            String ansibleService) {

        List<VSolarENV> vSolarENVEntries = vSolarRepository.findAll();

        // Dadurch soll verhindert werden, dass mehrere Einträge erstellt werden.
        if (vSolarENVEntries.isEmpty()) {
            VSolarENV vSolarENV = vSolarRepository
                    .insert(new VSolarENV());
            logger.info("Created vSolarENV entry: {}", vSolarENV);
            return vSolarENV;
        }

        return null;

    }

    /**
     * Der Api Token wird neu gesetzt
     * 
     * @param apiToken
     * @return
     */
    public String updateApiToken(String apiToken) {
        // Es soll nur ein Eintrag relevant sein
        VSolarENV env = vSolarRepository.findAll().get(0);

        env.setApiToken(apiToken);
        vSolarRepository.save(env);

        logger.info("Updated API Token: {}", apiToken);

        return apiToken;
    }

    /**
     * Gibt die erste Enität zurück
     * 
     * @return vSolarRepository
     */
    public VSolarENV all() {
        return vSolarRepository.findAll().get(0);
    }

    public void setVSolarRepository(VSolarRepository vSolarRepositoryMock) {
    }
}