package org.projecthusky.cda.elga.models.radiology;

public class ServiceEntry {
    private String serviceCode;
    private String serviceDescription;

    public ServiceEntry(String serviceCode, String serviceDescription) {
        this.serviceCode = serviceCode;
        this.serviceDescription = serviceDescription;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }
}
