package ch.bztf.vsolarmongo;

import java.util.List;
import java.util.Map;

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

    // docker run -d -p 27017:27017 --name mongodb mongo

    @Autowired
    private VSolarService vSolarService;

    @PostMapping("/setup")
    public ResponseEntity<VSolarENV> setup(@RequestBody Map<String, String> payload) {
        return new ResponseEntity<VSolarENV>(vSolarService.setupEnvironment(payload.get("user"), payload.get("password"), payload.get("vsphereServer"), payload.get("apiToken"), payload.get("terraformService"), payload.get("ansibleService")), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody String apiToken) {
        return new ResponseEntity<String>(vSolarService.updateApiToken(apiToken), HttpStatus.CREATED);
    }

    @GetMapping("/values")
    public ResponseEntity<VSolarENV> value() {
        return new ResponseEntity<VSolarENV>(vSolarService.all(), HttpStatus.OK);
    }
}
