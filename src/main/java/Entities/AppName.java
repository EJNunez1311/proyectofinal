package Entities;


public class AppName {

    public String name;
    public boolean securityCheckbox;
    public boolean microserviceCheckbox;

    public AppName(String name, boolean securityCheckbox, boolean microserviceCheckbox) {
        this.name = name;
        this.securityCheckbox = securityCheckbox;
        this.microserviceCheckbox = microserviceCheckbox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSecurityCheckbox() {
        return securityCheckbox;
    }

    public void setSecurityCheckbox(boolean securityCheckbox) {
        this.securityCheckbox = securityCheckbox;
    }

    public boolean isMicroserviceCheckbox() {
        return microserviceCheckbox;
    }

    public void setMicroserviceCheckbox(boolean microserviceCheckbox) {
        this.microserviceCheckbox = microserviceCheckbox;
    }
}
