package Entities;

import java.awt.*;
import java.util.ArrayList;

public class Data {
    public static ArrayList<FormValue> tablas = new ArrayList<>();
    public static ArrayList<FormValue> tablasProyecto = new ArrayList<>();
    public static ArrayList<FormValue> tablasGeneradas = new ArrayList<>();

    public static final String panel1 = "@Entity(name=\"titlesApi\")\n" +
            "public class departments extends PanacheEntityBase implements Serializable{\n" +
            "    @Id \n" +
            "    @Column(nullable = false) \n" +
            "    public String dept_no;\n" +
            "\n" +
            "    @Column(unique=true, nullable=false) \n" +
            "    public String dept_name;\n" +
            "\n" +
            "    public String getDept_no() {\n" +
            "        return dept_no;\n" +
            "    }\n" +
            "\n" +
            "    public void setDept_no(String dept_no) {\n" +
            "        this.dept_no = dept_no;\n" +
            "    }\n" +
            "    public String getDept_name() {\n" +
            "        return dept_name;\n" +
            "    }\n" +
            "\n" +
            "    public void setDept_name(String dept_name) {\n" +
            "        this.dept_name = dept_name;\n" +
            "    }\n" +
            "}";
    public static final String panel2 = "@Path(\"/api/departments\")\n" +
            "@Produces(MediaType.APPLICATION_JSON)\n" +
            "@Consumes(MediaType.APPLICATION_JSON)\n" +
            "@Tag(name = \"departments\" ,description = \"Here is all the information about departments. \")\n" +
            "@Authenticated\n" +
            "@SecurityRequirement(name = \"apiKey\")\n" +
            "public class departmentsApi {\n" +
            "\n" +
            "    @Inject\n" +
            "    EntityManager entityManager;\n" +
            "\n" +
            "\n" +
            "    @POST\n" +
            "    @RolesAllowed(\"user\")\n" +
            "    @Transactional\n" +
            "    public void add(departments departments) {\n" +
            "        departments.persist(departments);\n" +
            "    }\n" +
            "\n" +
            "    @GET\n" +
            "    @RolesAllowed(\"user\")\n" +
            "    public List<departments> getdepartments(){\n" +
            "        return departments.listAll();\n" +
            "    }\n" +
            "\n" +
            "    @PUT\n" +
            "    @RolesAllowed(\"user\")\n" +
            "    @Transactional\n" +
            "    @Path(\"/{id}\")\n" +
            "    public departments update(@PathParam(\"id\") String id, departments departments){\n" +
            "        if (departments.findById(id) == null) {\n" +
            "            throw new WebApplicationException(\"Id no fue enviado en la peticion.\", 422);\n" +
            "        }\n" +
            "\n" +
            "        departments entity = entityManager.find(departments.class,id);\n" +
            "\n" +
            "        if (entity == null) {\n" +
            "            throw new WebApplicationException(\" departments con el id: \" + id + \" no existe.\", 404);\n" +
            "        }\n" +
            "\n" +
            "\n" +
            "        entity.setDept_no(departments.getDept_no());\n" +
            "        entity.setDept_name(departments.getDept_name());\n" +
            "        return entity;\n" +
            "    }\n" +
            "\n" +
            "    @DELETE\n" +
            "    @RolesAllowed(\"user\")\n" +
            "    @Transactional\n" +
            "    @Path(\"/{id}\")\n" +
            "    public void deletedepartments(@PathParam(\"id\") String id){\n" +
            "        departments.deleteById(id);\n" +
            "    }\n" +
            "}";
    public static final String panel3 = "#Datasource Config\n" +
            "quarkus.datasource.db-kind=mysql\n" +
            "quarkus.datasource.driver=com.mysql.cj.jdbc.Driver\n" +
            "quarkus.datasource.username=ej\n" +
            "quarkus.datasource.password=1234\n" +
            "quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/employees\n" +
            "quarkus.hibernate-orm.log.sql=true\n" +
            "# drop and create the database at startup (use `update` to only update the schema)\n" +
            "quarkus.hibernate-orm.database.generation=none\n" +
            "quarkus.smallrye-openapi.path=/swagger\n" +
            "quarkus.swagger-ui.always-include=true\n" +
            "quarkus.swagger-ui.path=/explorer\n" +
            "mp.openapi.extensions.smallrye.operationIdStrategy=METHOD\n";

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
