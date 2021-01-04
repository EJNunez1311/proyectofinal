package Entities;

import java.awt.*;
import java.util.ArrayList;

public class Data {
    public static ArrayList<FormValue> tablas = new ArrayList<>();
    public static ArrayList<FormValue> tablasProyecto = new ArrayList<>();
    public static ArrayList<FormValue> tablasGeneradas = new ArrayList<>();
    public static ArrayList<ProyectoValue> proyectosGenerados = new ArrayList<>();

    public static final String panel1 = "   public static ArrayList<String> obtenerAtributos() {\n" +
            "        ArrayList<String> tipoAtributos = new ArrayList<>();\n" +
            "        tipoAtributos.add(\"String\");\n" +
            "        tipoAtributos.add(\"Integer\");\n" +
            "        tipoAtributos.add(\"Boolean\");\n" +
            "        tipoAtributos.add(\"Double\");\n" +
            "        tipoAtributos.add(\"Date\");\n" +
            "        tipoAtributos.add(\"Enum\");\n" +
            "        tipoAtributos.add(\"Long\");\n" +
            "\n" +
            "        return tipoAtributos;\n" +
            "    }";
    public static final String panel2 = "    public static ArrayList<TableFk> obtenerFk() {\n" +
            "        ArrayList<TableFk> listaFk = new ArrayList<>();\n" +
            "//        listaFk.add(new TableFk(\"employees\", \"dept_emp\", \"emp_no\", \"employees\", \"emp_no\"));\n" +
            "//        listaFk.add(new TableFk(\"employees\", \"dept_emp\", \"dept_no\", \"departments\", \"dept_no\"));\n" +
            "//        listaFk.add(new TableFk(\"employees\", \"dept_manager\", \"emp_no\", \"employees\", \"emp_no\"));\n" +
            "//        listaFk.add(new TableFk(\"employees\", \"dept_manager\", \"dept_no\", \"departments\", \"dept_no\"));\n" +
            "//        listaFk.add(new TableFk(\"employees\", \"titles\", \"emp_no\", \"employees\", \"emp_no\"));\n" +
            "        return  listaFk;\n" +
            "    }\n";
    public static final String panel3 = " public static ArrayList<String> obtenerBasesDeDatos() {\n" +
            "        ArrayList<String> basesDeDatos = new ArrayList<>();\n" +
            "        basesDeDatos.add(\"Prueba #1\");\n" +
            "        basesDeDatos.add(\"Prueba #2\");\n" +
            "        basesDeDatos.add(\"Prueba #3\");\n" +
            "        basesDeDatos.add(\"Prueba #4\");\n" +
            "        return  basesDeDatos;\n" +
            "    }\n";

    public static void fillList(){

        ArrayList<Form> formValue1 = new ArrayList<>();
        formValue1.add(new Form("Test Persona 1", "String", true, false, true, "", ""));
        formValue1.add(new Form("Test Persona 2", "Integer", true, true, true, "", ""));
        formValue1.add(new Form("Test Persona 3", "Double", true, false, false, "", ""));
        tablas.add(new FormValue("Persona", false, false,  formValue1));

        ArrayList<Form> formValue2 = new ArrayList<>();
        formValue2.add(new Form("Test Animal 1", "String", true, false, true, "", ""));
        formValue2.add(new Form("Test Animal 2", "Integer", false, true, true, "", ""));
        formValue2.add(new Form("Test Animal 3", "Boolean", true, false, false, "", ""));
        tablas.add(new FormValue("Animal", true, false, formValue2));

        ArrayList<Form> formValue3 = new ArrayList<>();
        formValue3.add(new Form("Test Producto 1", "Boolean", true, false, true, "", ""));
        formValue3.add(new Form("Test Producto 2", "Integer", false, true, true, "", ""));
        formValue3.add(new Form("Test Producto 3", "String", true, false, false, "", ""));
        tablas.add(new FormValue("Producto", false, true, formValue3));
    }

    public static ArrayList<String> obtenerAtributos() {
        ArrayList<String> tipoAtributos = new ArrayList<>();
        tipoAtributos.add("String");
        tipoAtributos.add("Integer");
        tipoAtributos.add("Boolean");
        tipoAtributos.add("Double");
        tipoAtributos.add("Date");
        tipoAtributos.add("Enum");
        tipoAtributos.add("Long");

        return tipoAtributos;
    }

    public static ArrayList<Relacion> obtenerRelaciones() {
        ArrayList<Relacion> relaciones = new ArrayList<>();
        relaciones.add(new Relacion("One to One", "OneToOne"));
        relaciones.add(new Relacion("One to Many", "OneToMany"));
        relaciones.add(new Relacion("Many to One", "ManyToOne"));
        relaciones.add(new Relacion("Many to Many", "ManyToMany"));
        return relaciones;
    }

    public static ArrayList<TableFk> obtenerFk() {
        ArrayList<TableFk> listaFk = new ArrayList<>();
//        listaFk.add(new TableFk("employees", "dept_emp", "emp_no", "employees", "emp_no"));
//        listaFk.add(new TableFk("employees", "dept_emp", "dept_no", "departments", "dept_no"));
//        listaFk.add(new TableFk("employees", "dept_manager", "emp_no", "employees", "emp_no"));
//        listaFk.add(new TableFk("employees", "dept_manager", "dept_no", "departments", "dept_no"));
//        listaFk.add(new TableFk("employees", "titles", "emp_no", "employees", "emp_no"));
        return  listaFk;
    }

    public static ArrayList<String> obtenerBasesDeDatos() {
        ArrayList<String> basesDeDatos = new ArrayList<>();
        basesDeDatos.add("Prueba #1");
        basesDeDatos.add("Prueba #2");
        basesDeDatos.add("Prueba #3");
        basesDeDatos.add("Prueba #4");
        return  basesDeDatos;
    }


    public static ArrayList<String> TablasCreadas() {
        ArrayList<String> TablasCreadas = new ArrayList<>();
        TablasCreadas.add("Primer Tabla");
        TablasCreadas.add("Segunda Tabla");

        return TablasCreadas;
    }

}
