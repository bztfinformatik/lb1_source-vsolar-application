package ch.bztf.vsolarterraform;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.bertramlabs.plugins.hcl4j.HCLParserException;

/**
 * Wichtig! Damit die Tests korrekt funktionieren, muss die Infrstruktur laufen!
 */
public class TerraformTest {

    @Test
    public void testSetConfiguration() {
        VirtualMachine vm = new VirtualMachine();
        vm.setVsphereDatacenter("datacenter");

        // MongoDB must be running
        Terraform terraform = Terraform.getInstance();

        try {
            terraform.setConfiguration(vm);

        } catch (HCLParserException | IOException e) {
            Assertions.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testExecute() throws IOException {
        String command = "terraform plan";

        Terraform terraform = Terraform.getInstance();

        SseEmitter emitterMock = Mockito.mock(SseEmitter.class);

        terraform.execute(emitterMock, command);

        Mockito.verify(emitterMock).send(Mockito.any());
        Mockito.verify(emitterMock).complete();
    }
}
