package ch.bztf.vsolarmongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VSolarService {

    @Autowired
    private VSolarRepository vSolarRepository;

    public VSolarENV setupEnvironment(String user, String password, String vsphereServer, String apiToken, String terraformService,
            String ansibleService) {

        List<VSolarENV> vSolarENVEntries = vSolarRepository.findAll();

        if (vSolarENVEntries.isEmpty()) {
            VSolarENV vSolarENV = vSolarRepository
                    .insert(new VSolarENV(user, password, vsphereServer, apiToken, terraformService, ansibleService));
            return vSolarENV;
        }

        return null;

    }

    public String updateApiToken(String apiToken) {
        VSolarENV env = vSolarRepository.findAll().get(0);

        env.setApiToken(apiToken);
        vSolarRepository.save(env);

        return apiToken;
    }

    public VSolarENV all() {
        return vSolarRepository.findAll().get(0);
    }
}