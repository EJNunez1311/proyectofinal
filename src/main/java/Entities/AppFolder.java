package Entities;


public class AppFolder {

    public String nombreFolder;
    public String ruta;
    public boolean microservicioCheckbox;
    public boolean seguridadCheckbox;

    public String getNombreFolder() {
        return nombreFolder;
    }

    public void setNombreFolder(String nombreFolder) {
        this.nombreFolder = nombreFolder;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public boolean isMicroservicioCheckbox() {
        return microservicioCheckbox;
    }

    public void setMicroservicioCheckbox(boolean microservicioCheckbox) {
        this.microservicioCheckbox = microservicioCheckbox;
    }

    public boolean isSeguridadCheckbox() {
        return seguridadCheckbox;
    }

    public void setSeguridadCheckbox(boolean seguridadCheckbox) {
        this.seguridadCheckbox = seguridadCheckbox;
    }
}
