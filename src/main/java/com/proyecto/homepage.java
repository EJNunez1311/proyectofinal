package com.proyecto;

import Entities.*;
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
import javax.ws.rs.core.Cookie;
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
import java.util.Objects;


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
    Template DbAcceso;
    @Inject
    Template DbFk;
    @Inject
    Template FormPregeneradaUpdate;
    @Inject
    Template CodigoVer;


    private static final Logger LOGGER = Logger.getLogger("ListenerBean");


    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        Data.fillList();
    }

    String nombre = "";
    int seguridad = 0;
    int microservicio = 0;

    String appProperties = "";
    String databasename_g = "prueba";
    int importado = 0;
    //Credenciales Admin para la DB (MYSQL)
    String dbUserAdmin, dbUserPassword;
    //Credenciales !=Admin para la DB (MySQL)
    String dbNamelist;
    List<FormValue> formValuesList = new ArrayList<FormValue>();

    List<String> RelacionFK = new ArrayList<String>();

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
    public Response GetAppName(@FormParam("name") String name,
                               @FormParam("securityCheckbox") String securityCheckbox) throws IOException {
        //        @FormParam("microserviceCheckbox") String microserviceCheckbox
        nombre = name;
        System.out.println("Nombre -> " + name);
//        System.out.println("Microservicio -> " + microserviceCheckbox);

        System.out.println("Security -> " + securityCheckbox);

//        microservicio = ((microserviceCheckbox != null) ? 1 : 0);
        seguridad = ((securityCheckbox != null) ? 1 : 0);
//        System.out.println(microservicio);
        System.out.println(seguridad);

//TODO: Usar campo de security y microservice

//        Create r = new Create(nombre, seguridad, microservicio);
//        Thread a = new Thread(r);
//        a.start();
//        try {
//            a.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        appProperties = r.getAppProperties();

        Runnable r = new Create(nombre, seguridad, microservicio);
        new Thread(r).start();

        appProperties = "#Datasource Config\n" +
                "quarkus.datasource.db-kind=mysql\n" +
                "quarkus.datasource.driver=com.mysql.cj.jdbc.Driver\n" +
                "quarkus.datasource.username=root\n" +
                "quarkus.datasource.password=12345678\n" +
                "quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/prueba\n" +
                "quarkus.hibernate-orm.log.sql=true\n" +
                "# drop and create the database at startup (use `update` to only update the schema)\n" +
                "quarkus.hibernate-orm.database.generation=update\n" +
                "quarkus.smallrye-openapi.path=/swagger\n" +
                "quarkus.swagger-ui.always-include=true\n" +
                "quarkus.swagger-ui.path=/explorer\n" +
                "mp.openapi.extensions.smallrye.operationIdStrategy=METHOD\n\n";
        if(seguridad != 0){
            appProperties = appProperties + "# Keycloak with 100 offset\n" +
                    "keycloak.url=http://localhost:8180\n" +
                    "\n" +
                    "quarkus.oidc.enabled=true\n" +
                    "quarkus.oidc.auth-server-url=${keycloak.url}/auth/realms/quarkus-realm\n" +
                    "quarkus.oidc.client-id=quarkus-client\n" +
                    "quarkus.oidc.credentials.secret=mysecret\n" +
                    "quarkus.http.cors=true\n" +
                    "quarkus.oidc.tls.verification=none\n" +
                    "grant_type=password\n";
        }

        return Response.ok().build();

    }

    @POST
    @Path("/conectar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //Funcion para verificar la conexion a la base de datos!
    public boolean Connect(DbName dbName) {
        //TODO: validar username y password
//        System.out.println(dbName.name);
//        System.out.println(dbName.username);
//        System.out.println(dbName.password);
        try {
//          Get Connection to DB
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName.name, dbUserAdmin, dbUserPassword);
            if (!myconnection.isClosed() || myconnection != null) {
                dbNamelist = dbName.name;
//                dbUserlist = dbName.username;
//                dbUserPassword = dbName.password;
                importado = 1;
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
//        return true;
    }

    @GET
    @Path("/fk/{table}")
    public TemplateInstance DbFk(@PathParam("table") String table) {
        //TODO: Cargar lista de relaciones
        ArrayList<TableFk> listafk = new ArrayList<>();
        try {
            ResultSet myRs = chequearFK(table);
            while (myRs.next()) {
                listafk.add((new TableFk(myRs.getString("COLUMN_NAME"), myRs.getString("REFERENCED_TABLE_NAME"), myRs.getString("REFERENCED_COLUMN_NAME"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return DbFk
                .data("title", "Foreign Keys of " + table)
                .data("listafk", listafk)
                .data("NombreTabla", table);
    }


    @GET
    @Path("/db/acceso")
    public TemplateInstance DbAcceso() {
        //TODO: Cargar lista de bases de datos
        ArrayList<String> alldatabase = new ArrayList<>();
        try {
//            Get Connection to DB
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/information_schema", dbUserAdmin, dbUserPassword);

            //Create a Statement
            Statement dictoStatement = myconnection.createStatement();
            System.out.println("Conectado correctamente a la Base de Datos antes de show all tables");
            String dbquery = "SELECT `schema_name` \n" +
                    "from INFORMATION_SCHEMA.SCHEMATA \n" +
                    "WHERE `schema_name` NOT IN('information_schema', 'mysql', 'performance_schema');\n";

            ResultSet myRs = dictoStatement.executeQuery(dbquery);
            while (myRs.next()) {
                alldatabase.add(myRs.getString("schema_name"));

            }


        } catch (
                Exception e) {
            e.printStackTrace();
        }

        return DbAcceso
                .data("title", "Database Name")
                .data("basesDeDatos", alldatabase);
//                .data("basesDeDatos", Data.obtenerBasesDeDatos());
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
    public boolean GetDataBaseName(@FormParam("username") String username, @FormParam("password") String password) throws IOException {
        //Solo para tomar o leer el nombre de la base de datos.
        //TODO: validar username y password

        try {
//          Get Connection to DB
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/information_schema", username, password);
            if (!myconnection.isClosed() || myconnection != null) {
                dbUserAdmin = username;
                dbUserPassword = password;
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        //Cambia el nombre de la DB en Application Properties.
        return true;
    }


    @GET
    @Path("/db/table")
    //Aqui muestro todas las tablas para mandarla a la vista.
    public TemplateInstance ShowallTables() {
        ArrayList<FormValue> nombres = new ArrayList<>();
        try {
//            Get Connection to DB
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbNamelist, dbUserAdmin, dbUserPassword);

            //Create a Statement
            Statement dictoStatement = myconnection.createStatement();
            System.out.println("Conectado correctamente a la Base de Datos antes de show all tables");
            String queryalltables = "SELECT table_name\n" +
                    "FROM information_schema.tables\n" +
                    "WHERE table_schema ='" + dbNamelist + "'" +
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
                ResultSet myRsFk = chequearFK(myRs.getString("table_name"));
                boolean tieneFk = true;
                if (!myRsFk.next()) {
                    tieneFk = false;
                }
//                System.out.println(myRsFk.getString("REFERENCED_COLUMN_NAME"));
                nombres.add(new FormValue(myRs.getString("table_name"), creado, tieneFk, null));
                System.out.println(myRs.getString("table_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String path = System.getProperty("user.dir");

        for (FormValue formValue : Data.tablas) {
            String clase = formValue.getNombreTabla().substring(0, 1).toUpperCase() + formValue.getNombreTabla().substring(1).toLowerCase();
            File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");
            if (myObj.exists()){
                formValue.creado = true;
            }
        }

//        return Tablesname.data("tablas", Data.tablas);
        return Tablesname.data("tablas", nombres).data("title", dbNamelist + " " + "Tables");
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
    @Path("/form/ver/codigos/{nombre}")
    public TemplateInstance FormCodigosVer(@PathParam("nombre") String nombre) {
        //Imprime El nombre de la tabla que elegiste
        System.out.println(nombre);
        return CodigoVer.data("title", "Generated Code")
                .data( /*mostrarClase(FormValue EDGAR, 2)*/"Prueba", Data.panel1)
                .data( /*mostrarClase(FormValue EDGAR, 2)*/"Prueba", Data.panel2)
                .data(appProperties, Data.panel3);
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
            Connection myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbNamelist, dbUserAdmin, dbUserPassword);

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
                            "AND table_schema ='" + dbNamelist + "'";
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
            detalle = new FormValue(name, false, false, detalles);

        } catch (Exception e) {
            e.printStackTrace();
        }


//        FormValue tabla = null;
//        for (FormValue formvalue : Data.tablas) {
//            if (formvalue.nombreTabla.equals(name)) {
//                tabla = formvalue;
//            }
//        }

        return FormUpdate.data("tablaDetalle", detalle).data("tipoAtributos", Data.obtenerAtributos()).data("title", "Table " +name);
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
//
//            String path = System.getProperty("user.dir");
//            String userHome = System.getProperty("user.home");
//
//            File theDir = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/");
//            if (!theDir.exists()) theDir.mkdirs();
//
//            /////////////////////////
//            clase = nomb.substring(0, 1).toUpperCase() + nomb.substring(1).toLowerCase();
//            String claseminus = nomb.toLowerCase();
//            /////////////////////////
//            try {
//                //Get Connection to DB
//                Class.forName("com.mysql.cj.jdbc.Driver");
//                Connection myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databasename_g, "root", "12345678");
//
//                //Create a Statement
//                Statement dictoStatement = myconnection.createStatement();
//                System.out.println("Conectado correctamente a la Base de Datos");
//
//                String QueryDic =
//                        "SELECT\n" +
//                                "tb.COLUMN_NAME AS Field_Name,\n" +
//                                "tb.COLUMN_TYPE AS Data_Type,\n" +
//                                "tb.IS_NULLABLE AS Allow_Empty,\n" +
//                                "tb.COLUMN_KEY AS PK,\n" +
//                                "tb.EXTRA AS Extra,\n" +
//                                "tb.COLUMN_COMMENT AS Field_Description \n" +
//                                "FROM\n" +
//                                "`INFORMATION_SCHEMA`.`COLUMNS` as tb\n" +
//                                "WHERE\n" +
//                                "TABLE_NAME = '" + nomb + "'" +
//                                "AND table_schema ='" + databasename_g + "'";
//
//
//                String NewQuery = "Show COLUMNS from " + nomb;
//                //Execute SQL query
//                System.out.println(NewQuery);
//                ResultSet myRs = dictoStatement.executeQuery(QueryDic);
//                //Process the result set
//                System.out.println(nomb);
//                while (myRs.next()) {
//                    System.out.println(myRs.getString("Field_Name") + "," + myRs.getString("Data_Type") + "," + myRs.getString("Allow_Empty") + "," + myRs.getString("PK") + "," + myRs.getString("Extra"));
//
//                    atributo = myRs.getString("Field_Name");
//                    String aux;
//
//                    aux = atributo.substring(0, 1).toUpperCase() + atributo.substring(1).toLowerCase();
//
//                    if (myRs.getString("Data_type").toLowerCase().startsWith("enum".toLowerCase())) {
//                        String dentroEnum = myRs.getString("Data_type");
//                        tipo = "enum";
//                        dentroEnum = dentroEnum.substring(dentroEnum.indexOf("(") + 1);
//                        dentroEnum = dentroEnum.substring(0, dentroEnum.indexOf(")"));
//                        dentroEnum = dentroEnum.replaceAll("\'", "");
////                        System.out.println(dentroEnum.replaceAll("\'",""));
//
//
//                        String archivojava = "package org.proyecto.Entity;\n" +
//                                "public enum " + aux + " {\n" +
//                                "    " + dentroEnum + ";\n" +
//                                "}";
//
//
//                        modelos = modelos +
//                                "    @Enumerated(EnumType.ORDINAL)\n";
//
//                        if (myRs.getString("Allow_Empty").toLowerCase().contains("no".toLowerCase())) {
//                            modelos = modelos +
//                                    "    @Column(nullable = false) \n";
//                        }
//
//                        modelos = modelos + "    public " + aux + " " + atributo + ";\n" + "\n";
//
//
//                        getset = getset +
//                                "    public " + aux + " get" + aux + "() {\n" +
//                                "        return " + atributo.toLowerCase() + ";\n" +
//                                "    }\n" +
//                                "\n" +
//                                "    public void set" + aux + "(" + aux + " " + atributo.toLowerCase() + ") {\n" +
//                                "        this." + atributo.toLowerCase() + " = " + atributo.toLowerCase() + ";\n" +
//                                "    }\n";
//
//
//                        entidad = entidad +
//                                "        entity.set" + aux + "(" + claseminus + ".get" + aux + "());\n";
//
//
//                        try {
//                            File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + aux + ".java");
//                            if (myObj.createNewFile()) {
//                                //   System.out.println("File created: " + myObj.getName());
//                            } else {
//                                //  System.out.println("Archivo ya existe.");
//                            }
//                        } catch (IOException e) {
//                            System.out.println("Se produjo un error.");
//                            e.printStackTrace();
//                        }
//
//                        try {
//                            FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + aux + ".java");
//                            myWriter.write(archivojava
//                            );
//                            myWriter.close();
//                            //  System.out.println("Modelo generado");
//                        } catch (IOException e) {
//                            System.out.println("Se produjo un error.");
//                            e.printStackTrace();
//                        }
//
//                        continue;
//
//                    }
//                    if (myRs.getString("Data_Type").toLowerCase().startsWith("varchar".toLowerCase()))
//                        tipo = "String";
//                    else if (myRs.getString("Data_Type").toLowerCase().startsWith("int".toLowerCase()))
//                        tipo = "int";
//                    else if (myRs.getString("Data_Type").toLowerCase().startsWith("date".toLowerCase()))
//                        tipo = "Date";
//                    else if (myRs.getString("Data_Type").toLowerCase().startsWith("float".toLowerCase()))
//                        tipo = "float";
//                    else if (myRs.getString("Data_Type").toLowerCase().startsWith("double".toLowerCase()))
//                        tipo = "double";
//                    else if (myRs.getString("Data_Type").toLowerCase().startsWith("boolean".toLowerCase()))
//                        tipo = "boolean";
//                    else if (myRs.getString("Data_Type").toLowerCase().startsWith("char".toLowerCase()))
//                        tipo = "String";
//                    else tipo = "/*ERROR AL TOMAR TIPO DESDE LA BD*/";
//
//                    atributo = atributo.toLowerCase();
//                    if (myRs.getString("PK").toLowerCase().contains("pri".toLowerCase())) {
//
//                        modelos = modelos +
//                                "    @Id \n";
//                        if (haypk == 0) {
//                            tipopk = tipo;
//                        }
//                        haypk++;
//
//                    }
//                    if (myRs.getString("Allow_Empty").toLowerCase().contains("no".toLowerCase()) && !myRs.getString("PK").toLowerCase().contains("uni".toLowerCase())) {
//                        modelos = modelos +
//                                "    @Column(nullable = false) \n";
//                    }
//                    if (!myRs.getString("Allow_Empty").toLowerCase().contains("no".toLowerCase()) && myRs.getString("PK").toLowerCase().contains("uni".toLowerCase())) {
//                        modelos = modelos +
//                                "    @Column(unique = true) \n";
//                    }
//                    if (myRs.getString("Allow_Empty").toLowerCase().contains("no".toLowerCase()) && myRs.getString("PK").toLowerCase().contains("uni".toLowerCase())) {
//                        modelos = modelos +
//                                "    @Column(unique=true, nullable=false) \n";
//                    }
//
//                    modelos = modelos + "    public " + tipo + " " + atributo + ";\n" + "\n";
//
//
//                    getset = getset +
//                            "    public " + tipo + " get" + aux + "() {\n" +
//                            "        return " + atributo.toLowerCase() + ";\n" +
//                            "    }\n" +
//                            "\n" +
//                            "    public void set" + aux + "(" + tipo + " " + atributo.toLowerCase() + ") {\n" +
//                            "        this." + atributo.toLowerCase() + " = " + atributo.toLowerCase() + ";\n" +
//                            "    }\n";
//
//
//                    entidad = entidad +
//                            "        entity.set" + aux + "(" + claseminus + ".get" + aux + "());\n";
//
//                    ///////////////////////////////////
//                }
//
//                //String path = System.getProperty("user.dir");
//                String archivojava = "package org.proyecto.Entity;\n" +
//                        "import io.quarkus.hibernate.orm.panache.PanacheEntity;\n" +
//                        "import io.quarkus.hibernate.orm.panache.PanacheEntityBase;\n" +
//                        "import javax.persistence.*;\n" +
//                        "import java.sql.Date;\n" +
//                        "import java.io.Serializable;\n";
//
//
//                if (haypk >= 1) {
//                    //String path = System.getProperty("user.dir");
//                    archivojava = archivojava + "@Entity\n" + "public class " + clase + " extends PanacheEntityBase implements Serializable{\n" + modelos + getset + "}"
//                    ;
//                } else {
//                    archivojava = archivojava + "@Entity\n" + "public class " + clase + " extends PanacheEntity {\n" + modelos + getset + "}";
//                }
//
//
//                try {
//                    File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");
//                    if (myObj.createNewFile()) {
//                        //   System.out.println("File created: " + myObj.getName());
//                    } else {
//                        //  System.out.println("Archivo ya existe.");
//                    }
//                } catch (IOException e) {
//                    System.out.println("Se produjo un error.");
//                    e.printStackTrace();
//                }
//
//                try {
//                    FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");
//                    myWriter.write(archivojava
//                    );
//                    myWriter.close();
//                    //  System.out.println("Modelo generado");
//                } catch (IOException e) {
//                    System.out.println("Se produjo un error.");
//                    e.printStackTrace();
//                }
//
//
//                String archivoapi =
//                        "package org.proyecto;\n" +
//                                "\n" +
//                                "import org.proyecto.Entity.*;\n" +
//                                "import javax.inject.Inject;\n" +
//                                "import javax.persistence.EntityManager;\n" +
//                                "import javax.transaction.Transactional;\n" +
//                                "import javax.ws.rs.*;\n" +
//                                "import javax.ws.rs.core.MediaType;\n" +
//                                "import java.util.List;\n" +
//                                "import org.eclipse.microprofile.openapi.annotations.tags.Tag;\n" +
//                                "\n" +
//                                "@Path(\"/api/" + nomb + "\")\n" +
//                                "@Produces(MediaType.APPLICATION_JSON)\n" +
//                                "@Consumes(MediaType.APPLICATION_JSON)\n" +
//                                "@Tag(name = \"" + clase + "\" ,description = \"Here is all the information about " + clase + ". \")\n" +
//                                "public class " + clase + "Api {\n" +
//                                "\n" +
//                                "    @Inject\n" +
//                                "    EntityManager entityManager;\n" +
//                                "\n" +
//                                "\n" +
//                                "    @POST\n" +
//                                "    @Transactional\n" +
//                                "    public void add(" + clase + " " + claseminus + ") {\n" +
//                                "        " + clase + ".persist(" + claseminus + ");\n" +
//                                "    }\n" +
//                                "\n" +
//                                "    @GET\n" +
//                                "    public List<" + clase + "> get" + clase + "(){\n" +
//                                "        return " + clase + ".listAll();\n" +
//                                "    }\n" +
//                                "\n" +
//                                "    @PUT\n" +
//                                "    @Transactional\n" +
//                                "    @Path(\"/{id}\")\n" +
//                                "    public " + clase + " update(@PathParam(\"id\") " + tipopk + " id, " + clase + " " + claseminus + "){\n" + "        if (" + claseminus + ".findById(id) == null) {\n" +
//                                "            throw new WebApplicationException(\"Id no fue enviado en la peticion.\", 422);\n" +
//                                "        }\n" +
//                                "\n" +
//                                "        " + clase + " entity = entityManager.find(" + clase + ".class,id);\n" +
//                                "\n" +
//                                "        if (entity == null) {\n" +
//                                "            throw new WebApplicationException(\" " + clase + " con el id: \" + id + \" no existe.\", 404);\n" +
//                                "        }\n" +
//                                "\n" +
//                                "\n" +
//                                entidad +
//                                "        return entity;\n" +
//                                "    }\n" +
//                                "\n" +
//                                "    @DELETE\n" +
//                                "    @Transactional\n" +
//                                "    @Path(\"/{id}\")\n" +
//                                "    public void delete" + clase + "(@PathParam(\"id\") " + tipopk + " id){\n" +
//                                "        " + clase + ".deleteById(id);\n" +
//                                "    }\n" +
//                                "}";
//
//
//                try {
//                    File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/" + clase + "Api.java");
//                    if (myObj.createNewFile()) {
//                        // System.out.println("File created: " + myObj.getName());
//                    } else {
//                        //  System.out.println("Archivo ya existe.");
//                    }
//                } catch (IOException e) {
//                    System.out.println("Se produjo un error.");
//                    e.printStackTrace();
//                }
//
//                try {
//                    FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/" + clase + "Api.java");
//
//                    myWriter.write(archivoapi
//                    );
//                    myWriter.close();
//                    //   System.out.println("Clase api generado");
//                } catch (IOException e) {
//                    System.out.println("Se produjo un error.");
//                    e.printStackTrace();
//                }
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            //        return Response.ok().build();

            String path = System.getProperty("user.dir");
            String userHome = System.getProperty("user.home");

            File theDir = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/");
            if (!theDir.exists()) theDir.mkdirs();


            theDir = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Api/");
            if (!theDir.exists()) theDir.mkdirs();


            /////////////////////////
            clase = nomb.toLowerCase();//nomb.substring(0, 1).toUpperCase() + nomb.substring(1).toLowerCase();
            String claseminus = nomb.toLowerCase();
            /////////////////////////
            try {
                //Get Connection to DB
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbNamelist, dbUserAdmin, dbUserPassword);

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
                                "AND table_schema ='" + dbNamelist + "'";


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
                    else if (myRs.getString("Data_Type").toLowerCase().startsWith("bigint".toLowerCase()))
                        tipo = "Long";
                    else if (myRs.getString("Data_Type").toLowerCase().startsWith("tinyint".toLowerCase()))
                        tipo = "boolean";
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
                        "package org.proyecto.Api;\n" +
                                "\n" +
                                "import org.proyecto.Entity.*;\n" +
                                "import javax.inject.Inject;\n" +
                                "import javax.persistence.EntityManager;\n" +
                                "import javax.transaction.Transactional;\n" +
                                "import javax.ws.rs.*;\n" +
                                "import javax.ws.rs.core.MediaType;\n" +
                                "import java.util.List;\n" +
                                "import org.eclipse.microprofile.openapi.annotations.tags.Tag;\n" +
                                "import io.quarkus.security.Authenticated;\n" +
                                "import io.quarkus.security.identity.SecurityIdentity;\n" +
                                "import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;\n" +
                                "import javax.annotation.security.RolesAllowed;" +
                                "\n" +
                                "@Path(\"/api/" + nomb + "\")\n" +
                                "@Produces(MediaType.APPLICATION_JSON)\n" +
                                "@Consumes(MediaType.APPLICATION_JSON)\n" +
                                "@Tag(name = \"" + clase + "\" ,description = \"Here is all the information about " + clase + ". \")\n";
                if (seguridad == 1) {
                    archivoapi = archivoapi + "@Authenticated\n" +
                            "@SecurityRequirement(name = \"apiKey\")\n";
                }
                archivoapi = archivoapi +
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
                if (seguridad == 1) {
                    archivoapi = archivoapi.replaceAll("@POST", "@POST\n    @RolesAllowed(\"user\")");
                    archivoapi = archivoapi.replaceAll("@GET", "@GET\n    @RolesAllowed(\"user\")");
                    archivoapi = archivoapi.replaceAll("@PUT", "@PUT\n    @RolesAllowed(\"user\")");
                    archivoapi = archivoapi.replaceAll("@DELETE", "@DELETE\n    @RolesAllowed(\"user\")");

                }


                try {
                    File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Api/" + clase + "Api.java");
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
                    FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/Api/" + clase + "Api.java");

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

        try {
            creartodo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void CrearUsuarioApi() {

        String UserApi = "package org.proyecto.Api;\n" +
                "\n" +
                "import org.eclipse.microprofile.config.inject.ConfigProperty;\n" +
                "import org.eclipse.microprofile.openapi.annotations.tags.Tag;\n" +
                "\n" +
                "import javax.ws.rs.*;\n" +
                "import javax.ws.rs.core.MediaType;\n" +
                "import java.io.BufferedReader;\n" +
                "import java.io.DataOutputStream;\n" +
                "import java.io.IOException;\n" +
                "import java.io.InputStreamReader;\n" +
                "import java.net.HttpURLConnection;\n" +
                "import java.net.MalformedURLException;\n" +
                "import java.net.ProtocolException;\n" +
                "import java.net.URL;\n" +
                "import java.nio.charset.StandardCharsets;\n" +
                "\n" +
                "\n" +
                "@Path(\"/UserToken\")\n" +
                "@Tag(name = \"UserToken\", description = \"Here is all the information about User and Access Token.\")\n" +
                "public class UserToken {\n" +
                "\n" +
                "    @ConfigProperty(name = \"grant_type\")\n" +
                "    private String grant_type;\n" +
                "    @ConfigProperty(name = \"quarkus.oidc.client-id\")\n" +
                "    private String client_id;\n" +
                "    @ConfigProperty(name = \"quarkus.oidc.credentials.secret\")\n" +
                "    private String client_secret;\n"+
                "    @ConfigProperty(name=\"urltoken_request\")\n" +
                "    private String request;\n"+
                "\n" +
                "\n" +
                "    @POST\n" +
                "    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)\n" +
                "    @Produces(MediaType.APPLICATION_JSON)\n" +
                "    public String ReturnToken(@FormParam(\"username\") String username, @FormParam(\"password\") String password) {\n" +
                "\n" +
                "        String urlParameters = \"username=\" + username + \"&password=\" + password + \"&grant_type=\" + grant_type + \"&client_id=\" + client_id + \"&client_secret=\" + client_secret;\n" +
                "\n" +
                "        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);\n" +
                "        int postDataLength = postData.length;\n" +
                "        URL url = null;\n" +
                "        try {\n" +
                "            url = new URL(request);\n" +
                "            HttpURLConnection conn = (HttpURLConnection) url.openConnection();\n" +
                "            conn.setDoOutput(true);\n" +
                "            conn.setInstanceFollowRedirects(false);\n" +
                "            conn.setRequestMethod(\"POST\");\n" +
                "            conn.setRequestProperty(\"Content-Type\", \"application/x-www-form-urlencoded\");\n" +
                "            conn.setRequestProperty(\"charset\", \"utf-8\");\n" +
                "            conn.setRequestProperty(\"Content-Length\", Integer.toString(postDataLength));\n" +
                "            conn.setUseCaches(false);\n" +
                "            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {\n" +
                "                wr.write(postData);\n" +
                "            }\n" +
                "            BufferedReader in = new BufferedReader(\n" +
                "                    new InputStreamReader(\n" +
                "                            conn.getInputStream()));\n" +
                "\n" +
                "            StringBuilder response = new StringBuilder();\n" +
                "            String currentLine;\n" +
                "\n" +
                "            while ((currentLine = in.readLine()) != null)\n" +
                "                response.append(currentLine);\n" +
                "\n" +
                "            in.close();\n" +
                "            return response.toString();\n" +
                "        } catch (MalformedURLException e) {\n" +
                "            e.printStackTrace();\n" +
                "        } catch (ProtocolException e) {\n" +
                "            e.printStackTrace();\n" +
                "        } catch (IOException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "        return \"\";\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "}\n";


        String path = System.getProperty("user.dir");
        String userHome = System.getProperty("user.home");

        try {
            File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Api/UserToken.java");
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
            FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/Api/UserToken.java");
            myWriter.write(UserApi);
            myWriter.close();
            //   System.out.println("Clase api generado");
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }
    }

    public void ImprimirClases(FormValue formValue) {
        System.out.println("Nombre de la tabla: " + formValue.nombreTabla + "creada: " + formValue.creado);
        for (Form form : formValue.getFilas()) {
            System.out.println("nombre " + form.getNombre() + " -- tipo " + form.getTipoAtributo() + " -- pkchekbox " + form.isPkCheckcbox()
                    + " -- not null " + form.isNotNullCheckbox() + " -- Unique" + form.isCheckBoxUnique() + "---Tabla FK: " + form.getFkTablaRelacionada() + " Tipo de relacion: " + form.getFkRelacion());
//            + form.isFkCheckbox()
        }
        System.out.println("\n\n--------------- Nueva tabla ----------------\n\n");
    }

    public boolean containsName(final List<FormValue> list, final String name) {
        return list.stream().anyMatch(o -> o.getNombreTabla().equals(name));
    }

    @POST
    @Path("/form")
    public boolean CrearTable(FormValue formValue) {

//        if (!containsName(formValuesList, formValue.nombreTabla)) {
//            formValuesList.add(formValue);
//            System.out.println("Tabla agregada a lista " + formValue.nombreTabla);
//        }

        crearClase(formValue);

        for (Form form : formValue.getFilas()) {
            System.out.println("nombre " + form.getNombre() + " -- tipo " + form.getTipoAtributo() + " -- pkchekbox " + form.isPkCheckcbox()
                    + " -- not null " + form.isNotNullCheckbox() + " -- Unique" + form.isCheckBoxUnique() + "---Tabla FK: "
                    + form.getFkTablaRelacionada() + " Tipo de relacion: " + form.getFkRelacion());
//            + form.isFkCheckbox()
        }


        Data.tablasGeneradas.add(formValue);

        return true;

    }

    public void crearClase(FormValue formValue) {

        String nomb;
        String clase;
        String atributo;
        String tipo;
        String modelos = "\n";
        String getset = "\n";
        String entidad = "\n";
        String tipopk = "long";
        String fk = "\n";
        int haypk = 0;


        String path = System.getProperty("user.dir");
        String userHome = System.getProperty("user.home");

        File theDir = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/");
        if (!theDir.exists()) theDir.mkdirs();

        theDir = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Api/");
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

            for (String cad : RelacionFK) {
                String[] test;
                test = cad.split(" ");
                //Employees emp_no Dept_manager ManyToOne 1
                if (test[0].equals(clase)) {
                    if (test[4].equals("2")) {
                        if (test[3].equals("OneToOne")) {
                            fk = fk + "    @OneToOne(mappedBy = \"" + nomb.toLowerCase() + "\")\n" +
                                    "    @PrimaryKeyJoinColumn\n" +
                                    "    public " + test[2] + " " + test[2].toLowerCase() + ";\n";
                        } else if (test[3].equals("OneToMany")) {
                            fk = fk + "    @OneToMany(fetch=FetchType.EAGER, mappedBy = \"" + nomb.toLowerCase() + "\")\n" +
                                    "    public Set<" + test[2] + "> " + test[2].toLowerCase() + ";\n";

                        } else if (test[3].equals("ManyToOne")) {
                            fk = fk + "    @ManyToOne(fetch=FetchType.EAGER)\n" +
                                    "    @JoinColumn(name = \"" + test[1] + "\", insertable = false, updatable = false)\n" +
                                    "    public " + test[2] + " " + test[2].toLowerCase() + ";";
                        } else if (test[3].equals("ManyToMany")) {
                            fk = fk + "    @ManyToMany\n" +
                                    "    public Set<" + test[2] + "> " + test[2].toLowerCase() + ";";
                        }
                    } else if (test[4].equals("1")) {
                        if (test[3].equals("OneToOne")) {
                            fk = fk + "    @OneToOne\n" +
                                    "    @MapsId\n" +
                                    "    @JoinColumn(name = \"" + test[1] + "\")\n" +
                                    "    public " + test[2] + " " + test[2].toLowerCase() + ";\n";
                        } else if (test[3].equals("OneToMany")) {
                            fk = fk + "    @OneToMany(fetch=FetchType.EAGER, mappedBy = \"" + nomb.toLowerCase() + "\")\n" +
                                    "    public Set<" + test[2] + "> " + test[2].toLowerCase() + ";\n";

                        } else if (test[3].equals("ManyToOne")) {
                            fk = fk + "    @ManyToOne(fetch=FetchType.EAGER)\n" +
                                    "    @JoinColumn(name = \"" + test[1] + "\", insertable = false, updatable = false)\n" +
                                    "    public " + test[2] + " " + test[2].toLowerCase() + ";";
                        } else if (test[3].equals("ManyToMany")) {
                            fk = fk + "    @ManyToMany(fetch=FetchType.EAGER, mappedBy = \"" + nomb.toLowerCase() + "\")\n" +
                                    "    public Set<" + test[2] + "> " + test[2].toLowerCase() + ";";
                        }
                    }
                }
                for (int i = 0; i < test.length; i++) {
                    System.out.println(test[i]);
                }
            }
            //String path = System.getProperty("user.dir");
            String archivojava = "package org.proyecto.Entity;\n" +
                    "import io.quarkus.hibernate.orm.panache.PanacheEntity;\n" +
                    "import io.quarkus.hibernate.orm.panache.PanacheEntityBase;\n" +
                    "import javax.persistence.*;\n" +
                    "import java.sql.Date;\n" +
                    "import java.io.Serializable;\n" +
                    "import java.util.Set;\n";

            if (haypk == 1) {
                //String path = System.getProperty("user.dir");
                archivojava = archivojava +
                        "@Entity\n" +
                        "public class " + clase + " extends PanacheEntityBase implements Serializable{\n" +

                        fk

                        +

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

                        fk

                        +

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
                    "package org.proyecto.Api;\n" +
                            "\n" +
                            "import org.proyecto.Entity.*;\n" +
                            "import javax.inject.Inject;\n" +
                            "import javax.persistence.EntityManager;\n" +
                            "import javax.transaction.Transactional;\n" +
                            "import javax.ws.rs.*;\n" +
                            "import javax.ws.rs.core.MediaType;\n" +
                            "import java.util.List;\n" +
                            "import org.eclipse.microprofile.openapi.annotations.tags.Tag;\n" +
                            "import io.quarkus.security.Authenticated;\n" +
                            "import io.quarkus.security.identity.SecurityIdentity;\n" +
                            "import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;\n" +
                            "import javax.annotation.security.RolesAllowed;" +
                            "\n" +
                            "@Path(\"/api/" + nomb + "\")\n" +
                            "@Produces(MediaType.APPLICATION_JSON)\n" +
                            "@Consumes(MediaType.APPLICATION_JSON)\n" +
                            "@Tag(name = \"" + clase + "\" ,description = \"Here is all the information about " + clase + ". \")\n";
            if (seguridad == 1) {
                archivoapi = archivoapi + "@Authenticated\n" +
                        "@SecurityRequirement(name = \"apiKey\")\n";
            }
            archivoapi = archivoapi +
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
            if (seguridad == 1) {
                archivoapi = archivoapi.replaceAll("@POST", "@POST\n    @RolesAllowed(\"user\")");
                archivoapi = archivoapi.replaceAll("@GET", "@GET\n    @RolesAllowed(\"user\")");
                archivoapi = archivoapi.replaceAll("@PUT", "@PUT\n    @RolesAllowed(\"user\")");
                archivoapi = archivoapi.replaceAll("@DELETE", "@DELETE\n    @RolesAllowed(\"user\")");

            }


            try {
                File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Api/" + clase + "Api.java");
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
                FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/Api/" + clase + "Api.java");

                myWriter.write(archivoapi
                );
                myWriter.close();
                //   System.out.println("Clase api generado");
            } catch (IOException e) {
                System.out.println("Se produjo un error.");
                e.printStackTrace();
            }

        }
    }

    public void llenarFK() {
        String[] auxiliar;
        String cad = "";
        String fkAlrevez = "";

        for (FormValue formValue : Data.tablasGeneradas) {
            for (Form form : formValue.filas) {
                if (!form.getFkTablaRelacionada().equals("") && !form.getFkRelacion().equals("")) {
//                    System.out.println(formValue.nombreTabla + " " + form.getNombre() + " " + form.getFkTablaRelacionada() + " " + form.getFkRelacion() + " 1");
                    auxiliar = form.getFkRelacion().split("To");
                    fkAlrevez = (auxiliar[1] + "To" + auxiliar[0]);
//                    System.out.println(form.getFkTablaRelacionada() + " " + form.getNombre() + " " + formValue.nombreTabla + " " + fkAlrevez +" 2");

                    RelacionFK.add(formValue.nombreTabla + " " + form.getNombre() + " " + form.getFkTablaRelacionada() + " " + form.getFkRelacion() + " 1");
                    RelacionFK.add(form.getFkTablaRelacionada() + " " + form.getNombre() + " " + formValue.nombreTabla + " " + fkAlrevez + " 2");

                }
            }
        }
    }

    @GET
    @Path("/createapp")
    public TemplateInstance CreateAPP() throws IOException {
        ListIterator<FormValue> listItr = Data.tablasGeneradas.listIterator();
        llenarFK();
        while (listItr.hasNext()) {
//            ImprimirClases(listItr.next());
            crearClase(listItr.next());
        }
        creartodo();
        return Homepage();
    }


    public void creartodo() throws IOException {


        if (seguridad == 1) {
            CrearUsuarioApi();
            JSONWriter jsonWriter = new JSONWriter(nombre);
            jsonWriter.crearConfigJson(nombre);
        }

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
                        "import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;\n" +
                        "import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;\n" +
                        "import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;" +
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
                        "@SecuritySchemes(value = {\n" +
                        "        @SecurityScheme(securitySchemeName = \"apiKey\",\n" +
                        "                type = SecuritySchemeType.HTTP,\n" +
                        "                scheme = \"Bearer\")}\n" +
                        ")" +
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

        if (importado == 1) {
            content = new String(Files.readAllBytes(path2), charset);
            content = content.replaceAll("prueba", dbNamelist);
            Files.write(path2, content.getBytes(charset));

            path2 = Paths.get(userHome + "/Downloads/" + nombre + "/src/main/resources/application.properties");
            charset = StandardCharsets.UTF_8;

            content = new String(Files.readAllBytes(path2), charset);
            content = content.replaceAll("root", dbUserAdmin);
            Files.write(path2, content.getBytes(charset));
            content = new String(Files.readAllBytes(path2), charset);
            content = content.replaceAll("12345678",dbUserPassword );
            Files.write(path2, content.getBytes(charset));
        }
    }

    public ResultSet chequearFK(String table) {
        try {
//            Get Connection to DB
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection myconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbNamelist, dbUserAdmin, dbUserPassword);

            //Create a Statement
            Statement dictoStatement = myconnection.createStatement();
            System.out.println("Conectado correctamente a la Base de Datos antes de show all tables");
            String queryfk = "SELECT TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,CONSTRAINT_NAME, REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME\n" +
                    "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE\n" +
                    "WHERE REFERENCED_TABLE_SCHEMA IS NOT NULL \n" +
                    "AND REFERENCED_TABLE_NAME IS NOT NULL \n" +
                    "AND REFERENCED_COLUMN_NAME IS NOT NULL\n" +
                    "AND REFERENCED_TABLE_SCHEMA = '" + dbNamelist + "'\n" +
                    "AND TABLE_NAME = '" + table + "';";


            //Execute SQL query
//        System.out.println(queryfk);
            return dictoStatement.executeQuery(queryfk);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String mostrarClase(FormValue formValue, int Valor) {

        String nomb;
        String clase;
        String atributo;
        String tipo;
        String modelos = "\n";
        String getset = "\n";
        String entidad = "\n";
        String tipopk = "long";
        String fk = "\n";
        int haypk = 0;

        if (formValue != null) {
            //Entity Name
            System.out.println(formValue.getNombreTabla());

            nomb = formValue.getNombreTabla();
            clase = nomb.substring(0, 1).toUpperCase() + nomb.substring(1).toLowerCase();
            String claseminus = nomb.toLowerCase();


            for (Form form : formValue.getFilas()) {

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

            for (String cad : RelacionFK) {
                String[] test;
                test = cad.split(" ");
                //Employees emp_no Dept_manager ManyToOne 1
                if (test[0].equals(clase)) {
                    if (test[4].equals("2")) {
                        if (test[3].equals("OneToOne")) {
                            fk = fk + "    @OneToOne(mappedBy = \"" + nomb.toLowerCase() + "\")\n" +
                                    "    @PrimaryKeyJoinColumn\n" +
                                    "    public " + test[2] + " " + test[2].toLowerCase() + ";\n";
                        } else if (test[3].equals("OneToMany")) {
                            fk = fk + "    @OneToMany(fetch=FetchType.EAGER, mappedBy = \"" + nomb.toLowerCase() + "\")\n" +
                                    "    public Set<" + test[2] + "> " + test[2].toLowerCase() + ";\n";

                        } else if (test[3].equals("ManyToOne")) {
                            fk = fk + "    @ManyToOne(fetch=FetchType.EAGER)\n" +
                                    "    @JoinColumn(name = \"" + test[1] + "\", insertable = false, updatable = false)\n" +
                                    "    public " + test[2] + " " + test[2].toLowerCase() + ";";
                        } else if (test[3].equals("ManyToMany")) {
                            fk = fk + "    @ManyToMany\n" +
                                    "    public Set<" + test[2] + "> " + test[2].toLowerCase() + ";";
                        }
                    } else if (test[4].equals("1")) {
                        if (test[3].equals("OneToOne")) {
                            fk = fk + "    @OneToOne\n" +
                                    "    @MapsId\n" +
                                    "    @JoinColumn(name = \"" + test[1] + "\")\n" +
                                    "    public " + test[2] + " " + test[2].toLowerCase() + ";\n";
                        } else if (test[3].equals("OneToMany")) {
                            fk = fk + "    @OneToMany(fetch=FetchType.EAGER, mappedBy = \"" + nomb.toLowerCase() + "\")\n" +
                                    "    public Set<" + test[2] + "> " + test[2].toLowerCase() + ";\n";

                        } else if (test[3].equals("ManyToOne")) {
                            fk = fk + "    @ManyToOne(fetch=FetchType.EAGER)\n" +
                                    "    @JoinColumn(name = \"" + test[1] + "\", insertable = false, updatable = false)\n" +
                                    "    public " + test[2] + " " + test[2].toLowerCase() + ";";
                        } else if (test[3].equals("ManyToMany")) {
                            fk = fk + "    @ManyToMany(fetch=FetchType.EAGER, mappedBy = \"" + nomb.toLowerCase() + "\")\n" +
                                    "    public Set<" + test[2] + "> " + test[2].toLowerCase() + ";";
                        }
                    }
                }
                for (int i = 0; i < test.length; i++) {
                    System.out.println(test[i]);
                }
            }
            //String path = System.getProperty("user.dir");
            String archivojava = "package org.proyecto.Entity;\n" +
                    "import io.quarkus.hibernate.orm.panache.PanacheEntity;\n" +
                    "import io.quarkus.hibernate.orm.panache.PanacheEntityBase;\n" +
                    "import javax.persistence.*;\n" +
                    "import java.sql.Date;\n" +
                    "import java.io.Serializable;\n" +
                    "import java.util.Set;\n";

            if (haypk == 1) {
                //String path = System.getProperty("user.dir");
                archivojava = archivojava +
                        "@Entity\n" +
                        "public class " + clase + " extends PanacheEntityBase implements Serializable{\n" +

                        fk

                        +

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

                        fk

                        +

                        modelos

                        +

                        getset

                        +
                        "}";
            }

            String archivoapi =
                    "package org.proyecto.Api;\n" +
                            "\n" +
                            "import org.proyecto.Entity.*;\n" +
                            "import javax.inject.Inject;\n" +
                            "import javax.persistence.EntityManager;\n" +
                            "import javax.transaction.Transactional;\n" +
                            "import javax.ws.rs.*;\n" +
                            "import javax.ws.rs.core.MediaType;\n" +
                            "import java.util.List;\n" +
                            "import org.eclipse.microprofile.openapi.annotations.tags.Tag;\n" +
                            "import io.quarkus.security.Authenticated;\n" +
                            "import io.quarkus.security.identity.SecurityIdentity;\n" +
                            "import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;\n" +
                            "import javax.annotation.security.RolesAllowed;" +
                            "\n" +
                            "@Path(\"/api/" + nomb + "\")\n" +
                            "@Produces(MediaType.APPLICATION_JSON)\n" +
                            "@Consumes(MediaType.APPLICATION_JSON)\n" +
                            "@Tag(name = \"" + clase + "\" ,description = \"Here is all the information about " + clase + ". \")\n";
            if (seguridad == 1) {
                archivoapi = archivoapi + "@Authenticated\n" +
                        "@SecurityRequirement(name = \"apiKey\")\n";
            }
            archivoapi = archivoapi +
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

            if (seguridad == 1) {
                archivoapi = archivoapi.replaceAll("@POST", "@POST\n    @RolesAllowed(\"user\")");
                archivoapi = archivoapi.replaceAll("@GET", "@GET\n    @RolesAllowed(\"user\")");
                archivoapi = archivoapi.replaceAll("@PUT", "@PUT\n    @RolesAllowed(\"user\")");
                archivoapi = archivoapi.replaceAll("@DELETE", "@DELETE\n    @RolesAllowed(\"user\")");

            }

            if(Valor == 1)
                return archivojava;
            if(Valor == 2)
                return archivoapi;

        }
        return "Error En La Generacion de Clase";
    }

}
