package ch.bztf.vsolarmongo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "env")
public class VSolarENV {
    @Id
    private ObjectId id;
    private String user;
    private String password;
    private String vsphereServer;
    private String apiToken;
    private String terraformService;
    private String ansibleService;

    /**
     * Konstruktur um die vSolar-Enität zu erstellen
     */
    public VSolarENV() {
        this.user = user;
        this.password = password;
        this.vsphereServer = vsphereServer;
        this.apiToken = apiToken;
        this.terraformService = terraformService;
        this.ansibleService = ansibleService;
    }
}