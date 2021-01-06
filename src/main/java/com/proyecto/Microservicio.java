package com.proyecto;

import Entities.*;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.runtime.Startup;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
@Startup
@Path("microservices")
public class Microservicio {
    @Inject
    Template MicroserviceName;
    @Inject
    Template FormProyecto;
    @Inject
    Template MicroservicioVer;
    @Inject
    Template MicroservicioProyecto;
    @Inject
    Template MicroservicioForm;
    @Inject
    Template MicroservicioTablasVer;
    @Inject
    Template MicroservicioFormUpdate;
    @Inject
    Template MicroservicioCodigoVer;
    @Inject
    Template MicroservicioDBName;
    @Inject
    Template MicroservicioDbAcceso;
    @Inject
    Template MicroservicioTablesname;
    @Inject
    Template MicroservicioFormDBUpdate;
    @Inject
    Template MicroservicioDbFk;


    private String rutaEntity = "";
    private String rutaApi = "";
    String rutaCarpetaMadre = "";
    String nombre_carpeta_madre = "";
    int seguridad = 0;
    //Variables para la base de datos
    String dburl, dbUserAdmin, dbUserPassword, dbNamelist;
    String proyecto_elegido;
    int importado;
    ArrayList<FormValue> listaTablasCreadas = new ArrayList<>();

    @GET
    @Path("/create")
    // Vista para el input del nombre de la app
    public TemplateInstance CreateMicroservice() {
        return MicroserviceName.data("title", "Name of MicroService");
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    //Metodo para recibir el nombre de la app y generar los primero parametros de la app!
    public Response GetAppName(@FormParam("name") String name) throws IOException {
        nombre_carpeta_madre = name;

        String path = System.getProperty("user.dir");
        String userHome = System.getProperty("user.home");

        rutaCarpetaMadre = path + "/" + nombre_carpeta_madre + "/";

        File theDir = new File(rutaCarpetaMadre);
        if (!theDir.exists()) theDir.mkdirs();

//        System.out.println("Nombre -> " + name);
        System.out.println("Proyecto Madre: " + nombre_carpeta_madre);

        //TODO: Crear Proyecto MicroServicio

        //        Runnable r = new Create(nombre);
        //
        //        new Thread(r).start();

        return Response.ok().build();

    }

    @GET
    @Path("/crear/proyecto")
    // Vista para el input del nombre de la app
    public TemplateInstance CreateApp() {
        return MicroservicioProyecto.data("title", "Microservices");
    }

    @POST
    @Path("/crear/proyecto")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    //Metodo para recibir el nombre de la app y generar los primero parametros de la app!
    public Response GetAppName(@FormParam("name") String name,
                               @FormParam("securityCheckbox") String securityCheckbox) throws IOException {
        System.out.println("Nombre -> " + name);
        System.out.println("Security -> " + securityCheckbox);

        seguridad = ((securityCheckbox != null) ? 1 : 0);
        System.out.println(seguridad);

        //TODO: Usar campo de security y microservice

        Runnable r = new CreateMicro(name, seguridad,1, rutaCarpetaMadre);
        new Thread(r).start();

//        ProyectoValue proyectoValue = new ProyectoValue(name, new ArrayList<FormValue>());
//        Data.proyectosGenerados.add(proyectoValue);

        return Response.ok().build();
    }

    @GET
    @Path("/importar/tablas/{proyecto}")
    public TemplateInstance ImportarTablasBaseDatos(@PathParam("proyecto") String proyecto) {
        //TODO: Importar la tablas desde la base de datos
        //TODO: Asignar estas tablas en la lista de tablas del prooyecto especificado
        proyecto_elegido = proyecto;
        return MicroservicioDBName
                .data("title", "Table View")
                .data("proyectosGenerados", Data.proyectosGenerados);
    }

    @GET
    @Path("/ver")
    public TemplateInstance GetProyectosGenerados() {

        //TODO: Utilizar el codigo de abajo para leer la ruta de la carpeta madre
        String path = System.getProperty("user.dir");
//         File myObj = new File(path + "/" + nombre_carpeta_madre);

        //        ProyectoValue proyectoValue = new ProyectoValue(name, new ArrayList<FormValue>());
//        Data.proyectosGenerados.add(proyectoValue);

        ArrayList<String> folder = new ArrayList<>();
        if (!nombre_carpeta_madre.isEmpty()) {
//             File directory = new File(myObj);
            File directory = new File(path + "/" + nombre_carpeta_madre);
            System.out.println(directory);
            if (directory.exists()) {
                System.out.println("Entro a -->" + directory);
                File[] files = directory.listFiles();
                for (File file : Objects.requireNonNull(files)) {
                    if (file.isDirectory()) {
                        boolean tieneArchivoPomGradle = listFiles(file.getAbsolutePath());
                        if (tieneArchivoPomGradle) {
                            boolean existe = false;
                            for (ProyectoValue pv : Data.proyectosGenerados) {
                                if (pv.nombreProyecto.equals(file.getName())) {
                                    existe = true;
                                    break;
                                }
                            }
                            if (!existe) {
                                ProyectoValue proyectoValue = new ProyectoValue(file.getName(), new ArrayList<FormValue>());
                                Data.proyectosGenerados.add(proyectoValue);
                            }
                        }
                    }
                }
            }
        }

        return MicroservicioVer
                .data("title", "Table View")
                .data("proyectosGenerados", Data.proyectosGenerados);
    }

    @GET
    @Path("/form/{proyecto}")
    public TemplateInstance TableCreation(@PathParam("proyecto") String proyecto) {
        ProyectoValue currentPv = new ProyectoValue();
        if (!proyecto.isEmpty()) {
            for (ProyectoValue pv : Data.proyectosGenerados) {
                if (pv.nombreProyecto.equals(proyecto)) {
                    currentPv = pv;
                    break;
                }
            }
        }

        return MicroservicioForm.data("title", "Table Creation")
                .data("tipoAtributos", Data.obtenerAtributos())
                .data("tablasCreadas", currentPv != null ? currentPv.tablas : new ArrayList<FormValue>())
                .data("proyecto", proyecto)
                .data("relaciones", Data.obtenerRelaciones());
    }

    @POST
    @Path("/form/{proyecto}")
    public boolean CrearTable(FormValue formValue, @PathParam("proyecto") String proyecto) {
        // crearClase(formValue);

        for (Form form : formValue.getFilas()) {
            System.out.println("nombre " + form.getNombre() + " -- tipo " + form.getTipoAtributo() + " -- pkchekbox " + form.isPkCheckcbox()
                    + " -- not null " + form.isNotNullCheckbox() + " -- Unique" + form.isCheckBoxUnique() + "---Tabla FK: "
                    + form.getFkTablaRelacionada() + " Tipo de relacion: " + form.getFkRelacion());
//            + form.isFkCheckbox()
        }

        if (!proyecto.isEmpty()) {
            for (ProyectoValue pv : Data.proyectosGenerados) {
                if (pv.nombreProyecto.equals(proyecto)) {
                    pv.tablas.add(formValue); //TODO Se guarda las tablas que se van a crear
                    break;
                }
            }
        }

        return true;
    }

    @GET
    @Path("/form/actualizar/{proyecto}/{index}")
    public TemplateInstance TableUpdate(@PathParam("proyecto") String proyecto, @PathParam("index") int index) {
        ProyectoValue currentPv = null;
        FormValue formValue = new FormValue();

        if (!proyecto.isEmpty()) {
            for (ProyectoValue pv : Data.proyectosGenerados) {
                if (pv.nombreProyecto.equals(proyecto)) {
                    currentPv = pv;
                    break;
                }
            }
        }

        if (currentPv != null) {
            if (index <= currentPv.tablas.size()) {
                formValue = currentPv.tablas.get(index - 1);
            }
        }

        return MicroservicioFormUpdate.data("title", "Table Update")
                .data("tablaDetalle", formValue)
                .data("index", index)
                .data("proyecto", proyecto)
                .data("tipoAtributos", Data.obtenerAtributos())
                .data("tablasCreadas", currentPv != null ? currentPv.tablas : new ArrayList<FormValue>())
                .data("relaciones", Data.obtenerRelaciones());
    }

    @POST
    @Path("/form/actualizar/{proyecto}/{index}")
    public boolean ActualizarTable(@PathParam("index") int index, @PathParam("proyecto") String proyecto, FormValue formValue) {
        for (Form form : formValue.getFilas()) {
            System.out.println("nombre " + form.getNombre() + " -- tipo " + form.getTipoAtributo() + " -- pkchekbox " + form.isPkCheckcbox()
                    + " -- not null " + form.isNotNullCheckbox() + " -- Unique" + form.isCheckBoxUnique() + "---Tabla FK: " + form.getFkTablaRelacionada() + " Tipo de relacion: "
                    + form.getFkRelacion());
//            + form.isFkCheckbox()
        }

        ProyectoValue currentPv = null;

        if (!proyecto.isEmpty()) {
            for (ProyectoValue pv : Data.proyectosGenerados) {
                if (pv.nombreProyecto.equals(proyecto)) {
                    currentPv = pv;
                    break;
                }
            }
        }

        if (currentPv != null) {
            if (index <= currentPv.tablas.size()) {
                FormValue currentFormValue = currentPv.tablas.get(index - 1);
                currentFormValue.nombreTabla = formValue.nombreTabla;
                currentFormValue.creado = formValue.creado;
                currentFormValue.filas = formValue.filas;
            }
        }

        return true;
    }

    @GET
    @Path("/form/ver/{proyecto}")
    public TemplateInstance TableView(@PathParam("proyecto") String proyecto) {
        ProyectoValue currentPv = null;
        if (!proyecto.isEmpty()) {
            for (ProyectoValue pv : Data.proyectosGenerados) {
                if (pv.nombreProyecto.equals(proyecto)) {
                    currentPv = pv;
                    break;
                }
            }
        }

        return MicroservicioTablasVer.data("title", "Table View")
                .data("proyecto", proyecto)
                .data("tablasCreadas", currentPv != null ? currentPv.tablas : new ArrayList<FormValue>());
    }

    @GET
    @Path("/form/eliminar/{proyecto}/{index}")
    public TemplateInstance TableDelete(@PathParam("index") int index, @PathParam("proyecto") String proyecto) {

        ProyectoValue currentPv = null;
        if (!proyecto.isEmpty()) {
            for (ProyectoValue pv : Data.proyectosGenerados) {
                if (pv.nombreProyecto.equals(proyecto)) {
                    currentPv = pv;
                    break;
                }
            }
        }

        if (currentPv != null) {
            if (index <= currentPv.tablas.size()) {
                currentPv.tablas.remove(index - 1);
            }
        }

        return MicroservicioTablasVer.data("title", "Table View")
                .data("proyecto", proyecto)
                .data("tablasCreadas", currentPv != null ? currentPv.tablas : new ArrayList<FormValue>());
    }


    @GET
    @Path("/form/ver/codigos/{proyecto}")
    public TemplateInstance FormCodigosVer(@PathParam("proyecto") String proyecto) {
        return MicroservicioCodigoVer
                .data("title", "Codigos Generados")
                .data("proyecto", proyecto)
                .data("panel1", Data.panel1)
                .data("panel2", Data.panel2)
                .data("panel3", Data.panel3);
    }

    //TODO: Controladores con Base datos
    @GET
    @Path("/db")
    //Vista para el input para introducir el nombre de la DB!
    public TemplateInstance DBNameView() {

        return MicroservicioDBName.data("title", "Database Name");
    }

    @POST
    @Path("/db")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    //Funcion para leer el nombre de la base de datos!
    public boolean GetDataBaseName(@FormParam("databaseurl") String url, @FormParam("username") String username, @FormParam("password") String password) throws IOException {
        //Solo para tomar o leer el nombre de la base de datos.
        //TODO: validar username y password
        try {
//          Get Connection to DB
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection myconnection = DriverManager.getConnection("jdbc:mysql://" + url + "/information_schema", username, password);
            if (!myconnection.isClosed() || myconnection != null) {
                dburl = url;
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
    @Path("/db/acceso")
    public TemplateInstance DbAcceso() {
        //TODO: Cargar lista de bases de datos
        ArrayList<String> alldatabase = new ArrayList<>();
        try {
//            Get Connection to DB
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection myconnection = DriverManager.getConnection("jdbc:mysql://" + dburl + "/information_schema", dbUserAdmin, dbUserPassword);

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

        return MicroservicioDbAcceso
                .data("title", "Database Name")
                .data("basesDeDatos", alldatabase);
//                .data("basesDeDatos", Data.obtenerBasesDeDatos());
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

    //TODO: UNCOMMENT LOAD TABLES
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

            //CHEQUEA EL PROYECTO ACTUAL
            ProyectoValue currentPv = null;
            if (!proyecto_elegido.isEmpty()) {
                for (ProyectoValue pv : Data.proyectosGenerados) {
                    if (pv.nombreProyecto.equals(proyecto_elegido)) {
                        currentPv = pv;
                        break;
                    }
                }
            }

            //Execute SQL query
//        System.out.println(queryalltables);
            ResultSet myRs = dictoStatement.executeQuery(queryalltables);
//             nombres = myRs.getArray("table_name").;
//            ArrayList<String> nombres = new ArrayList<>();
            //Process the result set
            while (myRs.next()) {
                String path = System.getProperty("user.dir");
                String nombreTabla = myRs.getString("table_name");
                String clase = nombreTabla.substring(0, 1).toUpperCase() + nombreTabla.substring(1).toLowerCase();
                //TODO Chequear si existe la clase pero lo haremos con el arraylist
                File myObj = new File(path);
//                File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");
                boolean creado = false;

                for (FormValue fv : currentPv.tablas) {
                    if (fv.nombreTabla.equals(nombreTabla)) {
                        creado = true;
                        break;
                    }
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

//        String path = System.getProperty("user.dir");

        //TODO:CHEQUEAR LISTA
//        for (FormValue formValue : Data.tablas) {
//            String clase = formValue.getNombreTabla().substring(0, 1).toUpperCase() + formValue.getNombreTabla().substring(1).toLowerCase();
//            File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");
//            if (myObj.exists()){
//                formValue.creado = true;
//            }
//        }

//        return Tablesname.data("tablas", Data.tablas);
        return MicroservicioTablesname.data("tablas", nombres).data("title", dbNamelist + " " + "Tables");
//        return Tablesname.data("title", "table list");
    }


    @GET
    @Path("/form/update/{nombre}")
    public TemplateInstance TableUpdate(@PathParam("nombre") String name) {
//        FormValue form = Data.tablas.stream().filter(o -> o.nombreTabla.equals(nombre)).findFirst().orElse(null);
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

        return MicroservicioFormDBUpdate.data("tablaDetalle", detalle).data("tipoAtributos", Data.obtenerAtributos()).data("title", "Table " + name);
//        return FormUpdate.data("tablaDetalle", tabla).data("tipoAtributos", Data.obtenerAtributos());
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

        return MicroservicioDbFk
                .data("title", "Foreign Keys of " + table)
                .data("listafk", listafk)
                .data("NombreTabla", table);
    }

    @POST
    @Path("/create/group")
    public boolean CrearGroupTable(ArrayList<String> nombreTablas) {
        for (String nomb : nombreTablas) {
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
                ProyectoValue currentPv = null;

                if (!proyecto_elegido.isEmpty()) {
                    for (ProyectoValue pv : Data.proyectosGenerados) {
                        if (pv.nombreProyecto.equals(proyecto_elegido)) {
                            currentPv = pv;
                            break;
                        }
                    }
                }
                FormValue formValue = null;
                if (currentPv != null) {
                    if (currentPv.tablas.size() > 0) {
                        boolean existe = false;
                        for (FormValue fv : currentPv.tablas) {
                            if (fv.nombreTabla.equals(nomb)) {
                                existe = true;
                                break;
                            }
                        }
                        if (!existe) {
                            formValue = new FormValue(nomb, false, false, new ArrayList<Form>());
                        }
                    } else {
                        formValue = new FormValue(nomb, true, false, new ArrayList<Form>());
                    }
                }

                if (formValue != null) {
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
                    formValue.filas = detalles;
                    currentPv.tablas.add(formValue);
                }

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    @POST
    @Path("/form/createdb")
    public boolean CrearOnlyDBTable(FormValue formValue) {
        for (Form form : formValue.getFilas()) {
            System.out.println("nombre " + form.getNombre() + " -- tipo " + form.getTipoAtributo() + " -- pkchekbox " + form.isPkCheckcbox()
                    + " -- not null " + form.isNotNullCheckbox() + " -- Unique" + form.isCheckBoxUnique() + "---Tabla FK: "
                    + form.getFkTablaRelacionada() + " Tipo de relacion: " + form.getFkRelacion());
//            + form.isFkCheckbox()
        }

        if (!proyecto_elegido.isEmpty()) {
            for (ProyectoValue pv : Data.proyectosGenerados) {
                if (pv.nombreProyecto.equals(proyecto_elegido)) {
                    boolean existe = false;
                    for (FormValue fv : pv.tablas) {
                        if (fv.nombreTabla.equals(formValue.nombreTabla)) {
                            existe = true;
                            break;
                        }
                    }
                    if (!existe) {
                        pv.tablas.add(formValue); //TODO Se guarda las tablas que se van a crear
                    }
                    break;
                }
            }
        }

        return true;
    }

    //TODO: Terminar Todo con Base de Datos


    @POST
    @Path("/finalizar")
    public boolean Finalizar() {
        for (ProyectoValue pv : Data.proyectosGenerados) {
            System.out.println("Proyecto Actual: " + pv.nombreProyecto);
            for (FormValue fv : pv.tablas) {
                System.out.println("Tabla: " + fv.nombreTabla);
                for (Form form : fv.filas) {
                    System.out.println("columna: " + form.nombre);
                }
            }
        }
        return true;
    }


    private boolean listFiles(String ruta) {
        File folder = new File(ruta);

        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                if (file.getName().equals("pom.xml") || file.getName().equals("build.gradle")) {
                    return true;
                }
            } else if (file.isDirectory()) {
                listFiles(file.getAbsolutePath());
            }
        }
        return false;
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
}
