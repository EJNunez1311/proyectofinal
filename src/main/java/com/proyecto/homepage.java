package com.proyecto;

import Entities.Data;
import Entities.Form;
import Entities.FormValue;
//import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import org.jboss.resteasy.plugins.server.servlet.HttpServletResponseWrapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.management.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import java.io.*;
import java.lang.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.jboss.logging.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;


@ApplicationScoped
@Startup
@Path("api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class homepage {
    @Inject
    Template homepage;
    @Inject
    Template Form;
    @Inject
    Template FormUpdate;
    @Inject
    Template ApplicationName;
    @Inject
    Template DBName;
    @Inject
    Template Tablesname;
    @Inject
    Template TablasVer;
    @Inject
    Template FormPregeneradaUpdate;

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        Data.fillList();
    }

    String nombre = "";
    String databasename_g = "prueba";
    int importado = 0;
    List<FormValue> formValuesList = new ArrayList<FormValue>();

    @GET
    public TemplateInstance Homepage() {
        return homepage.data("title", "API Creation");
    }


    @GET
    @Path("/create")
    // Vista para el input del nombre de la app
    public TemplateInstance CreateApp() {
        return ApplicationName.data("title", "Name of Application");
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    //Metodo para recibir el nombre de la app y generar los primero parametros de la app!
    public Response GetAppName(@FormParam("name") String name) throws IOException {

        nombre = name;
//        System.out.println(nombre);

        Runnable r = new Create(nombre);
        new Thread(r).start();

        return Response.ok().build();

    }

    @GET
    @Path("/db")
    //Vista para el input para introducir el nombre de la DB!
    public TemplateInstance DBNameView() {

        return DBName.data("title", "Database Name");
    }

    @POST
    @Path("/db")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    //Funcion para leer el nombre de la base de datos!
    public boolean GetDataBaseName(@FormParam("name") String databasename) throws IOException {
        //Solo para tomar o leer el nombre de la base de datos.
        databasename_g = databasename;
        importado = 1;

        //Cambia el nombre de la DB en Application Properties.
        return true;
    }

    @GET
    @Path("/db/table")
    //Aqui muestro todas las tablas para mandarla a la vista.
    public TemplateInstance ShowallTables() {

//      Databasename_g; Variable Global para guardar el nombre de la base de datos!!
        ArrayList<FormValue> nombres = new ArrayList<>();
        try {
//            Get Connection to DB
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databasename_g, "root", "12345678");

            //Create a Statement
            Statement dictoStatement = myconnection.createStatement();
            System.out.println("Conectado correctamente a la Base de Datos antes de show all tables");
            String queryalltables = "SELECT table_name\n" +
                    "FROM information_schema.tables\n" +
                    "WHERE table_schema ='" + databasename_g + "'" +
                    "\nORDER BY table_name;";


            //Execute SQL query
//        System.out.println(queryalltables);
            ResultSet myRs = dictoStatement.executeQuery(queryalltables);
//             nombres = myRs.getArray("table_name").;
//            ArrayList<String> nombres = new ArrayList<>();
            //Process the result set
            while (myRs.next()) {
                String path = System.getProperty("user.dir");
                String formValue = myRs.getString("table_name");
                String clase = formValue.substring(0, 1).toUpperCase() + formValue.substring(1).toLowerCase();
                File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");
                boolean creado = false;
                if (myObj.exists()) {
                    creado = true;
                }

                nombres.add(new FormValue(myRs.getString("table_name"), creado, null));
                System.out.println(myRs.getString("table_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        String path = System.getProperty("user.dir");

//        for (FormValue formValue : Data.tablas) {
//            String clase = formValue.getNombreTabla().substring(0, 1).toUpperCase() + formValue.getNombreTabla().substring(1).toLowerCase();
//            File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");
//            if (myObj.exists()){
//                formValue.creado = true;
//            }
//        }

//        return Tablesname.data("tablas", Data.tablas);
        return Tablesname.data("tablas", nombres);
//        return Tablesname.data("title", "table list");
    }

    @GET
    @Path("/form")
    public TemplateInstance TableCreation() {
//        ArrayList<String> tablacreadas = new ArrayList<>();
//        String path = System.getProperty("user.dir");
//        File theDir = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/");
//        if (theDir.exists()) {
//            File[] listOfFiles = theDir.listFiles();
//            for (int i = 0; i < listOfFiles.length; i++) {
//                if (listOfFiles[i].isFile()) {
//                    int lastPeriodPos = listOfFiles[i].getName().lastIndexOf('.');
//                    tablacreadas.add(listOfFiles[i].getName().substring(0, lastPeriodPos));
//                }
//            }
//        }

        return Form.data("title", "Table Creation")
                .data("tipoAtributos", Data.obtenerAtributos())
                .data("tablasCreadas", Data.tablasGeneradas)
                .data("relaciones", Data.obtenerRelaciones());
//                .data("tablasCreadas", Data.TablasCreadas());
    }

    @GET
    @Path("/form/actualizar/{index}")
    public TemplateInstance TableUpdate(@PathParam("index") int index) {
        FormValue formValue = new FormValue();
        if (index <= Data.tablasGeneradas.size()) {
            formValue = Data.tablasGeneradas.get(index - 1);
        }

        return FormPregeneradaUpdate.data("title", "Table Update")
                .data("tablaDetalle", formValue)
                .data("index", index)
                .data("tipoAtributos", Data.obtenerAtributos())
                .data("tablasCreadas", Data.tablasGeneradas)
                .data("relaciones", Data.obtenerRelaciones());
    }

    @POST
    @Path("/form/actualizar/{index}")
    public boolean ActualizarTable(@PathParam("index") int index, FormValue formValue) {
        for (Form form : formValue.getFilas()) {
            System.out.println("nombre " + form.getNombre() + " -- tipo " + form.getTipoAtributo() + " -- pkchekbox " + form.isPkCheckcbox()
                    + " -- not null " + form.isNotNullCheckbox() + " -- Unique" + form.isCheckBoxUnique() + "---Tabla FK: " + form.getFkTablaRelacionada() + " Tipo de relacion: "
                    + form.getFkRelacion());
//            + form.isFkCheckbox()
        }
        if (index <= Data.tablasGeneradas.size()) {
            FormValue formValueActual = Data.tablasGeneradas.get(index - 1);
            if (formValueActual != null) {
                formValueActual.nombreTabla = formValue.nombreTabla;
                formValueActual.creado = formValue.creado;
                formValueActual.filas = formValue.filas;
            }
        }
        return true;
    }


    @GET
    @Path("/form/ver")
    public TemplateInstance TableView() {
        return TablasVer.data("title", "Table View")
                .data("tablasGeneradas", Data.tablasGeneradas);
    }

    @GET
    @Path("/form/eliminar/{index}")
    public TemplateInstance TableDelete(@PathParam("index") int index) {
        if (index <= Data.tablasGeneradas.size()) {
            Data.tablasGeneradas.remove(index - 1);
        }

        return TablasVer.data("title", "Table View")
                .data("tablasGeneradas", Data.tablasGeneradas);
    }

    @GET
    @Path("/form/update/{nombre}")
    public TemplateInstance TableUpdate(@PathParam("nombre") String name) {
        FormValue form = Data.tablas.stream().filter(o -> o.nombreTabla.equals(nombre)).findFirst().orElse(null);
        FormValue detalle = new FormValue();
        try {
            //Get Connection to DB
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databasename_g, "root", "12345678");

            //Create a Statement
            Statement dictoStatement = myconnection.createStatement();
            System.out.println("Conectado correctamente a la Base de Datos Tabla Details");

            String QueryDic =
                    "SELECT\n" +
                            "tb.COLUMN_NAME AS Field_Name,\n" +
                            "tb.COLUMN_TYPE AS Data_Type,\n" +
                            "tb.IS_NULLABLE AS Allow_Empty,\n" +
                            "tb.COLUMN_KEY AS PK,\n" +
                            "tb.EXTRA AS Extra,\n" +
                            "tb.COLUMN_COMMENT AS Field_Description \n" +
                            "FROM\n" +
                            "`INFORMATION_SCHEMA`.`COLUMNS` as tb\n" +
                            "WHERE\n" +
                            "TABLE_NAME = '" + name + "'" +
                            "AND table_schema ='" + databasename_g + "'";
//            System.out.println(QueryDic);

            //Execute SQL query
            ResultSet myRs = dictoStatement.executeQuery(QueryDic);
            //Process the result set
            ArrayList<Form> detalles = new ArrayList<>();
            while (myRs.next()) {
                System.out.println(myRs.getString("Field_Name") + "," + myRs.getString("Data_Type") + "," + myRs.getString("Allow_Empty") + "," + myRs.getString("PK") + "," + myRs.getString("Extra"));
                Form fila = new Form();
                fila.nombre = myRs.getString("Field_Name");
                if (myRs.getString("Data_Type").startsWith("tinyint")) {
                    fila.tipoAtributo = "Boolean";
                } else if (myRs.getString("Data_Type").startsWith("int")) {
                    fila.tipoAtributo = "Integer";
                } else if (myRs.getString("Data_Type").startsWith("varchar")) {
                    fila.tipoAtributo = "String";
                } else if (myRs.getString("Data_Type").startsWith("date")) {
                    fila.tipoAtributo = "Date";
                } else if (myRs.getString("Data_Type").startsWith("enum")) {
                    fila.tipoAtributo = "Enum";
                } else if (myRs.getString("Data_Type").startsWith("char")) {
                    fila.tipoAtributo = "String";
                }
                fila.valortipoAtributo = myRs.getString("Data_Type");
                fila.notNullCheckbox = myRs.getString("Allow_Empty").equals("NO");
                fila.CheckBoxUnique = fila.pkCheckcbox = myRs.getString("PK").equals("PRI");
                if (!fila.CheckBoxUnique) {
                    fila.CheckBoxUnique = myRs.getString("PK").equals("UNI");
                }
                detalles.add(fila);
            }
            detalle = new FormValue(name, false, detalles);

        } catch (Exception e) {
            e.printStackTrace();
        }


//        FormValue tabla = null;
//        for (FormValue formvalue : Data.tablas) {
//            if (formvalue.nombreTabla.equals(name)) {
//                tabla = formvalue;
//            }
//        }

        return FormUpdate.data("tablaDetalle", detalle).data("tipoAtributos", Data.obtenerAtributos());
//        return FormUpdate.data("tablaDetalle", tabla).data("tipoAtributos", Data.obtenerAtributos());
    }

    @POST
    @Path("/create/group")
    public boolean CrearGroupTable(ArrayList<String> nombreTablas) {

        for (String nomb : nombreTablas) {
            System.out.println(nomb);

            String clase;
            String atributo;
            String tipo;
            String modelos = "";
            String getset = "";
            String entidad = "";
            String modelaje;
            String tipopk = "long";
            int haypk = 0;

            String path = System.getProperty("user.dir");
            String userHome = System.getProperty("user.home");

            File theDir = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/");
            if (!theDir.exists()) theDir.mkdirs();

            /////////////////////////
            clase = nomb.substring(0, 1).toUpperCase() + nomb.substring(1).toLowerCase();
            String claseminus = nomb.toLowerCase();
            /////////////////////////
            try {
                //Get Connection to DB
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databasename_g, "root", "12345678");

                //Create a Statement
                Statement dictoStatement = myconnection.createStatement();
                System.out.println("Conectado correctamente a la Base de Datos");

                String QueryDic =
                        "SELECT\n" +
                                "tb.COLUMN_NAME AS Field_Name,\n" +
                                "tb.COLUMN_TYPE AS Data_Type,\n" +
                                "tb.IS_NULLABLE AS Allow_Empty,\n" +
                                "tb.COLUMN_KEY AS PK,\n" +
                                "tb.EXTRA AS Extra,\n" +
                                "tb.COLUMN_COMMENT AS Field_Description \n" +
                                "FROM\n" +
                                "`INFORMATION_SCHEMA`.`COLUMNS` as tb\n" +
                                "WHERE\n" +
                                "TABLE_NAME = '" + nomb + "'" +
                                "AND table_schema ='" + databasename_g + "'";


                String NewQuery = "Show COLUMNS from " + nomb;
                //Execute SQL query
                System.out.println(NewQuery);
                ResultSet myRs = dictoStatement.executeQuery(QueryDic);
                //Process the result set
                System.out.println(nomb);
                while (myRs.next()) {
                    System.out.println(myRs.getString("Field_Name") + "," + myRs.getString("Data_Type") + "," + myRs.getString("Allow_Empty") + "," + myRs.getString("PK") + "," + myRs.getString("Extra"));

                    atributo = myRs.getString("Field_Name");
                    String aux;

                    aux = atributo.substring(0, 1).toUpperCase() + atributo.substring(1).toLowerCase();

                    if (myRs.getString("Data_type").toLowerCase().startsWith("enum".toLowerCase())) {
                        String dentroEnum = myRs.getString("Data_type");
                        tipo = "enum";
                        dentroEnum = dentroEnum.substring(dentroEnum.indexOf("(") + 1);
                        dentroEnum = dentroEnum.substring(0, dentroEnum.indexOf(")"));
                        dentroEnum = dentroEnum.replaceAll("\'", "");
//                        System.out.println(dentroEnum.replaceAll("\'",""));


                        String archivojava = "package org.proyecto.Entity;\n" +
                                "public enum " + aux + " {\n" +
                                "    " + dentroEnum + ";\n" +
                                "}";


                        modelos = modelos +
                                "    @Enumerated(EnumType.ORDINAL)\n";

                        if (myRs.getString("Allow_Empty").toLowerCase().contains("no".toLowerCase())) {
                            modelos = modelos +
                                    "    @Column(nullable = false) \n";
                        }

                        modelos = modelos + "    public " + aux + " " + atributo + ";\n" + "\n";


                        getset = getset +
                                "    public " + aux + " get" + aux + "() {\n" +
                                "        return " + atributo.toLowerCase() + ";\n" +
                                "    }\n" +
                                "\n" +
                                "    public void set" + aux + "(" + aux + " " + atributo.toLowerCase() + ") {\n" +
                                "        this." + atributo.toLowerCase() + " = " + atributo.toLowerCase() + ";\n" +
                                "    }\n";


                        entidad = entidad +
                                "        entity.set" + aux + "(" + claseminus + ".get" + aux + "());\n";


                        try {
                            File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + aux + ".java");
                            if (myObj.createNewFile()) {
                                //   System.out.println("File created: " + myObj.getName());
                            } else {
                                //  System.out.println("Archivo ya existe.");
                            }
                        } catch (IOException e) {
                            System.out.println("Se produjo un error.");
                            e.printStackTrace();
                        }

                        try {
                            FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + aux + ".java");
                            myWriter.write(archivojava
                            );
                            myWriter.close();
                            //  System.out.println("Modelo generado");
                        } catch (IOException e) {
                            System.out.println("Se produjo un error.");
                            e.printStackTrace();
                        }

                        continue;

                    }
                    if (myRs.getString("Data_Type").toLowerCase().startsWith("varchar".toLowerCase()))
                        tipo = "String";
                    else if (myRs.getString("Data_Type").toLowerCase().startsWith("int".toLowerCase()))
                        tipo = "int";
                    else if (myRs.getString("Data_Type").toLowerCase().startsWith("date".toLowerCase()))
                        tipo = "Date";
                    else if (myRs.getString("Data_Type").toLowerCase().startsWith("float".toLowerCase()))
                        tipo = "float";
                    else if (myRs.getString("Data_Type").toLowerCase().startsWith("double".toLowerCase()))
                        tipo = "double";
                    else if (myRs.getString("Data_Type").toLowerCase().startsWith("boolean".toLowerCase()))
                        tipo = "boolean";
                    else if (myRs.getString("Data_Type").toLowerCase().startsWith("char".toLowerCase()))
                        tipo = "String";
                    else tipo = "/*ERROR AL TOMAR TIPO DESDE LA BD*/";

                    atributo = atributo.toLowerCase();
                    if (myRs.getString("PK").toLowerCase().contains("pri".toLowerCase())) {

                        modelos = modelos +
                                "    @Id \n";
                        if (haypk == 0) {
                            tipopk = tipo;
                        }
                        haypk++;

                    }
                    if (myRs.getString("Allow_Empty").toLowerCase().contains("no".toLowerCase()) && !myRs.getString("PK").toLowerCase().contains("uni".toLowerCase())) {
                        modelos = modelos +
                                "    @Column(nullable = false) \n";
                    }
                    if (!myRs.getString("Allow_Empty").toLowerCase().contains("no".toLowerCase()) && myRs.getString("PK").toLowerCase().contains("uni".toLowerCase())) {
                        modelos = modelos +
                                "    @Column(unique = true) \n";
                    }
                    if (myRs.getString("Allow_Empty").toLowerCase().contains("no".toLowerCase()) && myRs.getString("PK").toLowerCase().contains("uni".toLowerCase())) {
                        modelos = modelos +
                                "    @Column(unique=true, nullable=false) \n";
                    }

                    modelos = modelos + "    public " + tipo + " " + atributo + ";\n" + "\n";


                    getset = getset +
                            "    public " + tipo + " get" + aux + "() {\n" +
                            "        return " + atributo.toLowerCase() + ";\n" +
                            "    }\n" +
                            "\n" +
                            "    public void set" + aux + "(" + tipo + " " + atributo.toLowerCase() + ") {\n" +
                            "        this." + atributo.toLowerCase() + " = " + atributo.toLowerCase() + ";\n" +
                            "    }\n";


                    entidad = entidad +
                            "        entity.set" + aux + "(" + claseminus + ".get" + aux + "());\n";

                    ///////////////////////////////////
                }

                //String path = System.getProperty("user.dir");
                String archivojava = "package org.proyecto.Entity;\n" +
                        "import io.quarkus.hibernate.orm.panache.PanacheEntity;\n" +
                        "import io.quarkus.hibernate.orm.panache.PanacheEntityBase;\n" +
                        "import javax.persistence.*;\n" +
                        "import java.sql.Date;\n" +
                        "import java.io.Serializable;\n";


                if (haypk >= 1) {
                    //String path = System.getProperty("user.dir");
                    archivojava = archivojava + "@Entity\n" + "public class " + clase + " extends PanacheEntityBase implements Serializable{\n" + modelos + getset + "}"
                    ;
                } else {
                    archivojava = archivojava + "@Entity\n" + "public class " + clase + " extends PanacheEntity {\n" + modelos + getset + "}";
                }


                try {
                    File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");
                    if (myObj.createNewFile()) {
                        //   System.out.println("File created: " + myObj.getName());
                    } else {
                        //  System.out.println("Archivo ya existe.");
                    }
                } catch (IOException e) {
                    System.out.println("Se produjo un error.");
                    e.printStackTrace();
                }

                try {
                    FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");
                    myWriter.write(archivojava
                    );
                    myWriter.close();
                    //  System.out.println("Modelo generado");
                } catch (IOException e) {
                    System.out.println("Se produjo un error.");
                    e.printStackTrace();
                }


                String archivoapi =
                        "package org.proyecto;\n" +
                                "\n" +
                                "import org.proyecto.Entity.*;\n" +
                                "import javax.inject.Inject;\n" +
                                "import javax.persistence.EntityManager;\n" +
                                "import javax.transaction.Transactional;\n" +
                                "import javax.ws.rs.*;\n" +
                                "import javax.ws.rs.core.MediaType;\n" +
                                "import java.util.List;\n" +
                                "import org.eclipse.microprofile.openapi.annotations.tags.Tag;\n"+
                                "\n" +
                                "@Path(\"/api/" + nomb + "\")\n" +
                                "@Produces(MediaType.APPLICATION_JSON)\n" +
                                "@Consumes(MediaType.APPLICATION_JSON)\n" +
                                "@Tag(name = \""+ clase +"\" ,description = \"Here is all the information about "+ clase + ". \")"+
                                "public class " + clase + "Api {\n" +
                                "\n" +
                                "    @Inject\n" +
                                "    EntityManager entityManager;\n" +
                                "\n" +
                                "\n" +
                                "    @POST\n" +
                                "    @Transactional\n" +
                                "    public void add(" + clase + " " + claseminus + ") {\n" +
                                "        " + clase + ".persist(" + claseminus + ");\n" +
                                "    }\n" +
                                "\n" +
                                "    @GET\n" +
                                "    public List<" + clase + "> get" + clase + "(){\n" +
                                "        return " + clase + ".listAll();\n" +
                                "    }\n" +
                                "\n" +
                                "    @PUT\n" +
                                "    @Transactional\n" +
                                "    @Path(\"/{id}\")\n" +
                                "    public " + clase + " update(@PathParam(\"id\") " + tipopk + " id, " + clase + " " + claseminus + "){\n" + "        if (" + claseminus + ".findById(id) == null) {\n" +
                                "            throw new WebApplicationException(\"Id no fue enviado en la peticion.\", 422);\n" +
                                "        }\n" +
                                "\n" +
                                "        " + clase + " entity = entityManager.find(" + clase + ".class,id);\n" +
                                "\n" +
                                "        if (entity == null) {\n" +
                                "            throw new WebApplicationException(\" " + clase + " con el id: \" + id + \" no existe.\", 404);\n" +
                                "        }\n" +
                                "\n" +
                                "\n" +
                                entidad +
                                "        return entity;\n" +
                                "    }\n" +
                                "\n" +
                                "    @DELETE\n" +
                                "    @Transactional\n" +
                                "    @Path(\"/{id}\")\n" +
                                "    public void delete" + clase + "(@PathParam(\"id\") " + tipopk + " id){\n" +
                                "        " + clase + ".deleteById(id);\n" +
                                "    }\n" +
                                "}";


                try {
                    File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/" + clase + "Api.java");
                    if (myObj.createNewFile()) {
                        // System.out.println("File created: " + myObj.getName());
                    } else {
                        //  System.out.println("Archivo ya existe.");
                    }
                } catch (IOException e) {
                    System.out.println("Se produjo un error.");
                    e.printStackTrace();
                }

                try {
                    FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/" + clase + "Api.java");

                    myWriter.write(archivoapi
                    );
                    myWriter.close();
                    //   System.out.println("Clase api generado");
                } catch (IOException e) {
                    System.out.println("Se produjo un error.");
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            //        return Response.ok().build();

        }
        //      Databasename_g; Variable Global para guardar el nombre de la base de datos!!
//        ArrayList<String> nombres = new ArrayList<>();
//        try {
////            Get Connection to DB
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databasename_g, "root", "12345678");
//
//            //Create a Statement
//            Statement dictoStatement = myconnection.createStatement();
//            System.out.println("Conectado correctamente a la Base de Datos antes de show all tables");
//            String queryalltables = "SELECT table_name\n" +
//                            "FROM information_schema.tables\n" +
//                            "WHERE table_schema ='" + databasename_g +"'"+
//                            "\nORDER BY table_name;";
//
//
//            //Execute SQL query
////        System.out.println(queryalltables);
//            ResultSet myRs = dictoStatement.executeQuery(queryalltables);
////             nombres = myRs.getArray("table_name").;
////            ArrayList<String> nombres = new ArrayList<>();
//            //Process the result set
//            while (myRs.next()) {
//                nombres.add(myRs.getString("table_name"));
//                System.out.println(myRs.getString("table_name"));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        String path = System.getProperty("user.dir");
//        for (FormValue formValue : Data.tablas) {
//            String clase = formValue.getNombreTabla().substring(0, 1).toUpperCase() + formValue.getNombreTabla().substring(1).toLowerCase();
//            File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");
//            if (myObj.exists()){
//                formValue.creado = true;
//            }
//        }
        try {
            creartodo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void ImplimirCLases(FormValue formValue){
        System.out.println("Nombre de la tabla: " + formValue.nombreTabla + "creada: " + formValue.creado);
        for (Form form : formValue.getFilas()) {
            System.out.println("nombre " + form.getNombre() + " -- tipo " + form.getTipoAtributo() + " -- pkchekbox " + form.isPkCheckcbox()
                    + " -- not null " + form.isNotNullCheckbox() + " -- Unique" + form.isCheckBoxUnique() + "---Tabla FK: "+form.getFkTablaRelacionada()+" Tipo de relacion: "+form.getFkRelacion());
//            + form.isFkCheckbox()
        }
        System.out.println("\n\n--------------- Nueva tabla ----------------\n\n");
    }

    public boolean containsName(final List<FormValue> list, final String name){
        return list.stream().anyMatch(o -> o.getNombreTabla().equals(name));
    }

    @POST
    @Path("/form")
    public boolean CrearTable(FormValue formValue) {

        if(!containsName(formValuesList, formValue.nombreTabla) ) {
            formValuesList.add(formValue);
            System.out.println("Tabla agregada a lista");
        }

        for (Form form : formValue.getFilas()) {
            System.out.println("nombre " + form.getNombre() + " -- tipo " + form.getTipoAtributo() + " -- pkchekbox " + form.isPkCheckcbox()
                    + " -- not null " + form.isNotNullCheckbox() + " -- Unique" + form.isCheckBoxUnique() + "---Tabla FK: " + form.getFkTablaRelacionada() + " Tipo de relacion: " + form.getFkRelacion());
//            + form.isFkCheckbox()
        }

        Data.tablasGeneradas.add(formValue);

        String nomb;
        String clase;
        String atributo;
        String tipo;
        String modelos = "";
        String getset = "";
        String entidad = "";
        String modelaje;
        String tipopk = "long";
        int haypk = 0;


        String path = System.getProperty("user.dir");
        String userHome = System.getProperty("user.home");

        File theDir = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/");
        if (!theDir.exists()) theDir.mkdirs();

        if (formValue != null) {
            //Entity Name
            System.out.println(formValue.getNombreTabla());

            nomb = formValue.getNombreTabla();
            clase = nomb.substring(0, 1).toUpperCase() + nomb.substring(1).toLowerCase();
            String claseminus = nomb.toLowerCase();

            //Entity details
            for (Form form : formValue.getFilas()) {
//                System.out.println("nombre " + form.getNombre() + " -- tipo " + form.getTipoAtributo() + " -- pkchekbox " + form.isPkCheckcbox()
//                        + " -- not null " + form.isNotNullCheckbox() + "-- fk " + form.isCheckBoxUnique() +"--Unique" + form.isFkCheckbox());


                ///////////////////////////////////
                atributo = form.getNombre();
                tipo = form.getTipoAtributo();

                if (tipo.toLowerCase().contains("int")) {
                    tipo = "int";
                }
                if (tipo.toLowerCase().contains("boolean")) {
                    tipo = "boolean";
                }
                if (tipo.toLowerCase().contains("double")) {
                    tipo = "double";
                }

                atributo = atributo.toLowerCase();
                if (form.isPkCheckcbox()) {
                    tipopk = form.getTipoAtributo();
                    modelos = modelos +
                            "    @Id \n";
                    haypk = 1;
                }
                if (form.isNotNullCheckbox() && !form.isCheckBoxUnique()) {
                    modelos = modelos +
                            "    @Column(nullable = false) \n";
                }
                if (!form.isNotNullCheckbox() && form.isCheckBoxUnique()) {
                    modelos = modelos +
                            "    @Column(unique = true) \n";
                }
                if (form.isNotNullCheckbox() && form.isCheckBoxUnique()) {
                    modelos = modelos +
                            "    @Column(unique=true, nullable=false) \n";
                }
                modelos = modelos +
                        "    public " + tipo + " " + atributo + ";\n" +
                        "\n";

                String aux;
                aux = atributo.substring(0, 1).toUpperCase() + atributo.substring(1).toLowerCase();

                getset = getset +
                        "    public " + tipo + " get" + aux + "() {\n" +
                        "        return " + atributo.toLowerCase() + ";\n" +
                        "    }\n" +
                        "\n" +
                        "    public void set" + aux + "(" + tipo + " " + atributo.toLowerCase() + ") {\n" +
                        "        this." + atributo.toLowerCase() + " = " + atributo.toLowerCase() + ";\n" +
                        "    }\n";


                entidad = entidad +
                        "        entity.set" + aux + "(" + claseminus + ".get" + aux + "());\n";

                ///////////////////////////////////
            }

            //String path = System.getProperty("user.dir");
            String archivojava = "package org.proyecto.Entity;\n" +
                    "import io.quarkus.hibernate.orm.panache.PanacheEntity;\n" +
                    "import io.quarkus.hibernate.orm.panache.PanacheEntityBase;\n" +
                    "import javax.persistence.Column;\n" +
                    "import javax.persistence.Entity;\n" +
                    "import javax.persistence.GeneratedValue;\n" +
                    "import javax.persistence.Id;\n" +
                    "import java.sql.Date;\n" +
                    "import java.io.Serializable;\n";

            if (haypk == 1) {
                //String path = System.getProperty("user.dir");
                archivojava = archivojava +
                        "@Entity\n" +
                        "public class " + clase + " extends PanacheEntityBase implements Serializable{\n" +

                        modelos

                        +

                        getset

                        +
                        "}"
                ;
            } else {
                archivojava = archivojava +
                        "@Entity\n" +
                        "public class " + clase + " extends PanacheEntity {\n" +

                        modelos

                        +

                        getset

                        +
                        "}";
            }


            try {
                File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");
                if (myObj.createNewFile()) {
                    //   System.out.println("File created: " + myObj.getName());
                } else {
                    //  System.out.println("Archivo ya existe.");
                }
            } catch (IOException e) {
                System.out.println("Se produjo un error.");
                e.printStackTrace();
            }

            try {
                FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");
                myWriter.write(archivojava
                );
                myWriter.close();
                //  System.out.println("Modelo generado");
            } catch (IOException e) {
                System.out.println("Se produjo un error.");
                e.printStackTrace();
            }


            String archivoapi =
                    "package org.proyecto;\n" +
                            "\n" +
                            "import org.proyecto.Entity.*;\n" +
                            "import javax.inject.Inject;\n" +
                            "import javax.persistence.EntityManager;\n" +
                            "import javax.transaction.Transactional;\n" +
                            "import javax.ws.rs.*;\n" +
                            "import javax.ws.rs.core.MediaType;\n" +
                            "import java.util.List;\n" +
                            "import org.eclipse.microprofile.openapi.annotations.tags.Tag;\n"+
                            "\n" +
                            "@Path(\"/api/" + nomb + "\")\n" +
                            "@Produces(MediaType.APPLICATION_JSON)\n" +
                            "@Consumes(MediaType.APPLICATION_JSON)\n" +
                            "@Tag(name = \""+ clase +"\" ,description = \"Here is all the information about "+ clase + ". \")"+
                            "public class " + clase + "Api {\n" +
                            "\n" +
                            "    @Inject\n" +
                            "    EntityManager entityManager;\n" +
                            "\n" +
                            "\n" +
                            "    @POST\n" +
                            "    @Transactional\n" +
                            "    public void add(" + clase + " " + claseminus + ") {\n" +
                            "        " + clase + ".persist(" + claseminus + ");\n" +
                            "    }\n" +
                            "\n" +
                            "    @GET\n" +
                            "    public List<" + clase + "> get" + clase + "(){\n" +
                            "        return " + clase + ".listAll();\n" +
                            "    }\n" +
                            "\n" +
                            "    @PUT\n" +
                            "    @Transactional\n" +
                            "    @Path(\"/{id}\")\n" +
                            "    public " + clase + " update(@PathParam(\"id\") " + tipopk + " id, " + clase + " " + claseminus + "){\n" + "        if (" + claseminus + ".findById(id) == null) {\n" +
                            "            throw new WebApplicationException(\"Id no fue enviado en la peticion.\", 422);\n" +
                            "        }\n" +
                            "\n" +
                            "        " + clase + " entity = entityManager.find(" + clase + ".class,id);\n" +
                            "\n" +
                            "        if (entity == null) {\n" +
                            "            throw new WebApplicationException(\" " + clase + " con el id: \" + id + \" no existe.\", 404);\n" +
                            "        }\n" +
                            "\n" +
                            "\n" +
                            entidad +
                            "        return entity;\n" +
                            "    }\n" +
                            "\n" +
                            "    @DELETE\n" +
                            "    @Transactional\n" +
                            "    @Path(\"/{id}\")\n" +
                            "    public void delete" + clase + "(@PathParam(\"id\") " + tipopk + " id){\n" +
                            "        " + clase + ".deleteById(id);\n" +
                            "    }\n" +
                            "}";


            try {
                File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/" + clase + "Api.java");
                if (myObj.createNewFile()) {
                    // System.out.println("File created: " + myObj.getName());
                } else {
                    //  System.out.println("Archivo ya existe.");
                }
            } catch (IOException e) {
                System.out.println("Se produjo un error.");
                e.printStackTrace();
            }

            try {
                FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/" + clase + "Api.java");

                myWriter.write(archivoapi
                );
                myWriter.close();
                //   System.out.println("Clase api generado");
            } catch (IOException e) {
                System.out.println("Se produjo un error.");
                e.printStackTrace();
            }

        }
        return true;

    }

//    @GET
//    @Path("/dbconnect")
//    public void GetAllDb() {
//
//        try {
////            Get Connection to DB
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databasename_g, "root", "12345678");
//
//            //Create a Statement
//            Statement dictoStatement = myconnection.createStatement();
//            System.out.println("Conectado correctamente a la Base de Datos antes de show all tables");
//            String querydb = "SELECT table_name\n" +
//                    "FROM information_schema.tables\n" +
//                    "WHERE table_schema ='" + databasename_g + "'" +
//                    "\nORDER BY table_name;";
//
//            ResultSet myRs = dictoStatement.executeQuery(querydb);
//
//
//        } catch (
//                Exception e) {
//            e.printStackTrace();
//        }
//
//    }


    @POST
    @Path("/createapp")
    public boolean CreateAPP(FormValue formValue) throws IOException {
        creartodo();
        ListIterator<FormValue> listItr = formValuesList.listIterator();
        while(listItr.hasNext()){
            ImplimirCLases(listItr.next());
        }
        return true;

    }

    public void creartodo() throws IOException {

        String path = System.getProperty("user.dir");
        String userHome = System.getProperty("user.home");

        //Recibe la ultima tabla y termina de Crear y Mover la APP.
//        if (formValue != null) {
//            //Entity Name
//            System.out.println(formValue.getNombreTabla());
//            //Entity details
//            for (Form form: formValue.getFilas()) {
//                System.out.println("nombre " + form.getNombre() + " -- tipo " + form.getTipoAtributo() + " -- pkchekbox "+ form.isPkCheckcbox()
//                        + " -- not null " + form.isNotNullCheckbox() + "-- fk " + form.isFkCheckbox());
//            }
//        }


        String custApp =
                "package org.proyecto;\n" +
                        "\n" +
                        "import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;\n" +
                        "import org.eclipse.microprofile.openapi.annotations.info.Contact;\n" +
                        "import org.eclipse.microprofile.openapi.annotations.info.Info;\n" +
                        "import org.eclipse.microprofile.openapi.annotations.info.License;\n" +
                        "\n" +
                        "import javax.ws.rs.core.Application;\n" +
                        "\n" +
                        "\n" +
                        "@OpenAPIDefinition( \n" +
                        "        info = @Info(\n" +
                        "                title=\"Java Framework\",\n" +
                        "                version = \"1.0.0 (Test)\",\n" +
                        "                contact = @Contact(\n" +
                        "                        name = \"API Explorer\",\n" +
                        "                        url = \"http://pucmm.edu.do/\",\n" +
                        "                        email = \"pucmmisc@example.com\"),\n" +
                        "                license = @License(\n" +
                        "                        name = \"Proyecto Final 1.0\",\n" +
                        "                        url = \"http://www.apache.org/licenses/LICENSE-2.0.html\")))\n" +
                        "public class CustomApplication extends Application {\n" +
                        "}";

        try {
            File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/CustomApplication.java");
            if (myObj.createNewFile()) {
                //   System.out.println("Archivo Creado: " + myObj.getName());
            } else {
                // System.out.println("Archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }


        try {
            FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/CustomApplication.java");
            myWriter.write(custApp
            );
            myWriter.close();
            //  System.out.println("ApiSwagger Inyectado");
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        File theDir = new File(userHome + "/Downloads/" + nombre);
        if (!theDir.exists()) theDir.mkdirs();
        File from = new File(path + "/" + nombre);
        File to = new File(userHome + "/Downloads/" + nombre);

        try {
            Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Aplicacion creada con exito.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
//        BufferedReader br = new BufferedReader(new FileReader(userHome+"/Downloads/"+nombre+"/pon.xml"));
//        String line;
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//        }

        java.nio.file.Path path2 = Paths.get(userHome + "/Downloads/" + nombre + "/pom.xml");
        Charset charset = StandardCharsets.UTF_8;

        String content = new String(Files.readAllBytes(path2), charset);
        content = content.replaceAll("1.9.0.CR1", "1.8.2.Final");
        Files.write(path2, content.getBytes(charset));


        path2 = Paths.get(userHome + "/Downloads/" + nombre + "/src/main/resources/application.properties");
        charset = StandardCharsets.UTF_8;

        content = new String(Files.readAllBytes(path2), charset);
        content = content.replaceAll("prueba", databasename_g);
        Files.write(path2, content.getBytes(charset));

        if (importado == 1) {
            path2 = Paths.get(userHome + "/Downloads/" + nombre + "/src/main/resources/application.properties");
            charset = StandardCharsets.UTF_8;

            content = new String(Files.readAllBytes(path2), charset);
            content = content.replaceAll("drop-and-create", "update");
            Files.write(path2, content.getBytes(charset));
        }
    }
}
