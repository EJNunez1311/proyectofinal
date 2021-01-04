package Entities;

import java.util.ArrayList;

public class ProyectoValue {
    public String nombreProyecto;
    public ArrayList<FormValue> tablas;

    public ProyectoValue(String nombreProyecto, ArrayList<FormValue> tablas) {
        this.nombreProyecto = nombreProyecto;
        this.tablas = tablas;
    }
    
    public ProyectoValue(){}

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public ArrayList<FormValue> getTablas() {
        return tablas;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public void setTablas(ArrayList<FormValue> tablas) {
        this.tablas = tablas;
    }
}