package ch.bztf.vsolarterraform;

import lombok.Data;

@Data
public class VirtualMachine {
    private String vsphereUser;
    private String vspherePassword;
    private String vsphereServer;
    private String vsphereDatacenter;
    private String vsphereHost;
    private String vmDatastore;
    private String vmCluster;
    private String vmTemplate;
    private String vmNetwork;
    private String vmName;
    private String vmMemory;
    private String vmCpu;
}
