package Entities;

import java.awt.*;
import java.util.ArrayList;

public class Data {
    public static ArrayList<FormValue> tablas = new ArrayList<>();
    public static ArrayList<FormValue> tablasGeneradas = new ArrayList<>();

    public static void fillList(){

        ArrayList<Form> formValue1 = new ArrayList<>();
        formValue1.add(new Form("Test Persona 1", "String", true, false, true, "", ""));
        formValue1.add(new Form("Test Persona 2", "Integer", true, true, true, "", ""));
        formValue1.add(new Form("Test Persona 3", "Double", true, false, false, "", ""));
        tablas.add(new FormValue("Persona", false,  formValue1));

        ArrayList<Form> formValue2 = new ArrayList<>();
        formValue2.add(new Form("Test Animal 1", "String", true, false, true, "", ""));
        formValue2.add(new Form("Test Animal 2", "Integer", false, true, true, "", ""));
        formValue2.add(new Form("Test Animal 3", "Boolean", true, false, false, "", ""));
        tablas.add(new FormValue("Animal", true, formValue2));

        ArrayList<Form> formValue3 = new ArrayList<>();
        formValue3.add(new Form("Test Producto 1", "Boolean", true, false, true, "", ""));
        formValue3.add(new Form("Test Producto 2", "Integer", false, true, true, "", ""));
        formValue3.add(new Form("Test Producto 3", "String", true, false, false, "", ""));
        tablas.add(new FormValue("Producto", false, formValue3));
    }

    public static ArrayList<String> obtenerAtributos() {
        ArrayList<String> tipoAtributos = new ArrayList<>();
        tipoAtributos.add("String");
        tipoAtributos.add("Integer");
        tipoAtributos.add("Boolean");
        tipoAtributos.add("Double");
        tipoAtributos.add("Date");
        tipoAtributos.add("Enum");

        return tipoAtributos;
    }

    public static ArrayList<String> TablasCreadas() {
        ArrayList<String> TablasCreadas = new ArrayList<>();
        TablasCreadas.add("Primer Tabla");
        TablasCreadas.add("Segunda Tabla");

        return TablasCreadas;
    }
}
