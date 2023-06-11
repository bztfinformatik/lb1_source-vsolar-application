package ch.bztf.vsolarterraform;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.bertramlabs.plugins.hcl4j.HCLParserException;

@RestController
@RequestMapping("/api/terraform")
public class TerraformController {

    private Terraform terraform = Terraform.getInstance();
    private DatabaseHandler dbHandler = new DatabaseHandler();
    private final Logger logger = LoggerFactory.getLogger(TerraformController.class);

    /**
     * Post-Anweisung um MongoDB-Java-Service IP ins Config File zu schreiben.
     */
    @PostMapping("/setup")
    public ResponseEntity<Void> setup(@RequestBody String host) throws IOException {
        logger.info("Received setup request. Host: {}", host);
        dbHandler.setupConnectFile(host);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Get-Anweisung um Ausgabe von "terraform plan" zu erhalten
     * 
     * @return ResponseEntity
     * @throws IOException
     */
    @GetMapping("/plan")
    public ResponseEntity<SseEmitter> plan() throws IOException {
        SseEmitter emitter = new SseEmitter();

        logger.info("Received plan request.");

        terraform.execute(emitter, "terraform plan");

        return ResponseEntity.status(HttpStatus.OK).body(emitter);
    }

    /**
     * Put-Anweisung um die Configuration/Variablen anzupassen
     * 
     * @param vm
     * @return ResponseEntity
     * @throws HCLParserException
     * @throws IOException
     */
    @PutMapping("/configure")
    public ResponseEntity<Void> setConfiguration(@RequestBody VirtualMachine vm)
            throws HCLParserException, IOException {
        logger.info("Received configure request. VirtualMachine: {}", vm);

        terraform.setConfiguration(vm);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Post-Anweisung um TerraformConfig auszuführen
     * 
     * @return ResponseEntity
     * @throws IOException
     */
    @PostMapping("/apply")
    public ResponseEntity<SseEmitter> apply() throws IOException {
        SseEmitter emitter = new SseEmitter();

        logger.info("Received apply request.");

        terraform.execute(emitter, "terraform apply -auto-approve");

        return ResponseEntity.status(HttpStatus.CREATED).body(emitter);
    }

}
