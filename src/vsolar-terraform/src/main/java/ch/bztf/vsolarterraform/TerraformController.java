package ch.bztf.vsolarterraform;

import java.io.IOException;

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

    @PostMapping("/setup")
    public ResponseEntity<Void> setup(@RequestBody String host) throws IOException {
        dbHandler.setupConnectFile(host);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/plan")
    public ResponseEntity<SseEmitter> plan() throws IOException {
        SseEmitter emitter = new SseEmitter();

        terraform.execute(emitter, "terraform plan");

        return ResponseEntity.status(HttpStatus.OK).body(emitter);
    }

     @PutMapping("/configure")
     public ResponseEntity<Void> setConfiguration(@RequestBody VirtualMachine vm) throws HCLParserException, IOException {
        
        terraform.setConfiguration(vm);

        return ResponseEntity.status(HttpStatus.OK).build();
     }

    @PostMapping("/apply")
    public ResponseEntity<SseEmitter> apply() throws IOException {
        SseEmitter emitter = new SseEmitter();

        terraform.execute(emitter, "terraform apply -auto-approve");

        return ResponseEntity.status(HttpStatus.CREATED).body(emitter);
    }

}
