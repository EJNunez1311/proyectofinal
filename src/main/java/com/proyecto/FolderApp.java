package com.proyecto;

import Entities.*;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.runtime.Startup;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
@Startup
@Path("proyecto")
public class FolderApp {
    @Inject
    Template TablasProyectoVer;
    @Inject
    Template FormProyecto;
    @Inject
    Template FormUpdateProyecto;
    @Inject
    Template FolderApp;
    @Inject
    Template FolderAppView;

    private String rutaEntity = "";
    private String rutaApi = "";
    String rutaFolder = "";
    String proyectoActual = "";

    ArrayList<FormValue> listaTablasCreadas = new ArrayList<>();
    ArrayList<ProyectoValue> proyectoValues = new ArrayList<>();

    ArrayList<String[]> proyects = new ArrayList<String[]>();


    @GET
    @Path("/folder/app/ver/{nombreProyecto}")
    public TemplateInstance GetFolderAppTableView(@PathParam("nombreProyecto") String nombreProyecto) {
        // TODO: Find folder with pom.xml
        proyectoActual = nombreProyecto;
        ArrayList<String> listaApi = new ArrayList<>();
        ArrayList<String> listaEntity = new ArrayList<>();
        if (!rutaFolder.isEmpty()) {
            File directory = new File(rutaFolder + '/' + nombreProyecto);

            if (directory.exists()) {
                rutaApi = "";
                rutaEntity = "";
                listApi(directory.getAbsolutePath(), false);
                System.out.println("StringaApi-> " + rutaApi);
                if (!rutaApi.isEmpty()) {
                    File apis = new File(rutaApi);
                    for (File file : apis.listFiles()) {
                        if (file.isFile()) {
                            int lastPeriodPos = file.getName().lastIndexOf('.');
                            listaApi.add(file.getName().substring(0, lastPeriodPos));
                            System.out.println(file.getName());
                        }
                    }
                }

                listEntity(directory.getAbsolutePath(), false);
                System.out.println("StringEntity-> " + rutaEntity);
                if (!rutaEntity.isEmpty()) {
                    File entities = new File(rutaEntity);
                    for (File file : entities.listFiles()) {
                        if (file.isFile()) {
                            int lastPeriodPos = file.getName().lastIndexOf('.');
                            listaEntity.add(file.getName().substring(0, lastPeriodPos));
                        }
                    }
                }
            }
        }
        ArrayList<String> listaTablas = (ArrayList<String>) Stream.of(listaApi, listaEntity)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
        listaTablasCreadas = new ArrayList<>();
        for (String item : listaTablas) {
            listaTablasCreadas.add(new FormValue(item, false, false, null));
        }

        //arreglando
        ProyectoValue currentPv = new ProyectoValue();
        if (!nombreProyecto.isEmpty()) {
            for (ProyectoValue pv : Data.tablasProyecto) {
                if (pv.nombreProyecto.equals(nombreProyecto)) {
                    currentPv = pv;
                    break;
                }
            }
        }


        return TablasProyectoVer
                .data("title", "Name of Application")
                .data("entities", listaEntity)
                .data("apis", listaApi)
                .data("nombreProyecto", nombreProyecto)
                .data("listaNueva", currentPv != null ? currentPv.tablas : new ArrayList<FormValue>());
    }


    @GET
    @Path("/folder/form/{nombreProyecto}")
    public TemplateInstance ProyectoTableCreation(@PathParam("nombreProyecto") String nombreProyecto) {

        proyectoActual = nombreProyecto;

        return FormProyecto
                .data("title", "Table Creation")
                .data("tipoAtributos", Data.obtenerAtributos())
                .data("nombreProyecto", nombreProyecto)
                .data("tablasCreadas", listaTablasCreadas);

//            .data("relaciones", Data.obtenerRelaciones());
    }


    @POST
    @Path("/folder/form")
    public boolean CrearTableProyecto(FormValue formValue) {

        //        crearClase(formValue);

        for (Form form : formValue.getFilas()) {
            System.out.println("nombre " + form.getNombre() + " -- tipo " + form.getTipoAtributo() + " -- pkchekbox " + form.isPkCheckcbox()
                    + " -- not null " + form.isNotNullCheckbox() + " -- Unique" + form.isCheckBoxUnique() + "---Tabla FK: "
                    + form.getFkTablaRelacionada() + " Tipo de relacion: " + form.getFkRelacion());
//            + form.isFkCheckbox()
        }

        System.out.println("Nombre del Proyeto Actual:" + proyectoActual);
        if (!proyectoActual.isEmpty()) {
            for (ProyectoValue pv : Data.tablasProyecto) {
                if (pv.nombreProyecto.equals(proyectoActual)) {
                    pv.tablas.add(formValue); //TODO Se guarda las tablas que se van a crear
                    break;
                }
            }
        }
        return true;
    }

    @GET
    @Path("/folder/form/actualizar/{nombreProyecto}/{index}")
    public TemplateInstance TableUpdateProyecto(@PathParam("index") int index, @PathParam("nombreProyecto") String nombreProyecto) {

        FormValue formValue = new FormValue();
        ProyectoValue currentPv = null;
        if (!nombreProyecto.isEmpty()) {
            for (ProyectoValue pv : Data.tablasProyecto) {
                if (pv.nombreProyecto.equals(nombreProyecto)) {
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

        return FormUpdateProyecto
                .data("title", "Table Update")
                .data("tablaDetalle", formValue)
                .data("index", index)
                .data("nombreProyecto", nombreProyecto)
                .data("tipoAtributos", Data.obtenerAtributos());
//            .data("tablasCreadas", listaTablasCreadas)
//            .data("relaciones", Data.obtenerRelaciones());
    }

    @POST
    @Path("/folder/form/actualizar/{index}")
    public boolean ActualizarTableProyecto(@PathParam("index") int index, FormValue formValue) {
        for (Form form : formValue.getFilas()) {
            System.out.println("nombre " + form.getNombre() + " -- tipo " + form.getTipoAtributo() + " -- pkchekbox " + form.isPkCheckcbox()
                    + " -- not null " + form.isNotNullCheckbox() + " -- Unique" + form.isCheckBoxUnique() + "---Tabla FK: " + form.getFkTablaRelacionada() + " Tipo de relacion: "
                    + form.getFkRelacion());
//            + form.isFkCheckbox()
        }
        ProyectoValue currentPv = null;
        if (!proyectoActual.isEmpty()) {
            for (ProyectoValue pv : Data.tablasProyecto) {
                if (pv.nombreProyecto.equals(proyectoActual)) {
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

//        if (index <= Data.tablasProyecto.size()) {
//            FormValue formValueActual = Data.tablasProyecto.get(index - 1);
//            if (formValueActual != null) {
//                formValueActual.nombreTabla = formValue.nombreTabla;
//                formValueActual.creado = formValue.creado;
//                formValueActual.filas = formValue.filas;
//            }
//        }
        return true;
    }

    @GET
    @Path("/folder/form/eliminar/{nombreProyecto}/{index}")
    public TemplateInstance TableDeletePdoyecto(@PathParam("index") int index, @PathParam("nombreProyecto") String nombreProyecto) {
//            String nombre_borrar = Data.tablasProyecto.get(index).nombreTabla;
//            System.out.println("Tabla a borrar: " + nombre_borrar);

        ProyectoValue currentPv = null;
        if (!nombreProyecto.isEmpty()) {
            for (ProyectoValue pv : Data.tablasProyecto) {
                if (pv.nombreProyecto.equals(nombreProyecto)) {
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

//        if (index <= Data.tablasProyecto.size()) {
//            Data.tablasProyecto.remove(index - 1);
//        }

        ArrayList<String> listaApi = new ArrayList<>();
        ArrayList<String> listaEntity = new ArrayList<>();
        System.out.println(rutaFolder);
        if (!rutaFolder.isEmpty()) {
            File directory = new File(rutaFolder + '\\' + nombreProyecto);
            if (directory.exists()) {
                listApi(directory.getAbsolutePath(), false);
                System.out.println("StringaApi-> " + rutaApi);
                if (!rutaApi.isEmpty()) {
                    File apis = new File(rutaApi);
                    for (File file : apis.listFiles()) {
                        if (file.isFile()) {
                            int lastPeriodPos = file.getName().lastIndexOf('.');
                            listaApi.add(file.getName().substring(0, lastPeriodPos));
                        }
                    }
                }

                listEntity(directory.getAbsolutePath(), false);
                System.out.println("StringEntity-> " + rutaEntity);
                if (!rutaEntity.isEmpty()) {
                    File entities = new File(rutaEntity);
                    for (File file : entities.listFiles()) {
                        if (file.isFile()) {
                            int lastPeriodPos = file.getName().lastIndexOf('.');
                            listaEntity.add(file.getName().substring(0, lastPeriodPos));
                        }
                    }
                }
            }
        }
        ArrayList<String> listaTablas = (ArrayList<String>) Stream.of(listaApi, listaEntity)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
        listaTablasCreadas = new ArrayList<>();
        for (String item : listaTablas) {
            listaTablasCreadas.add(new FormValue(item, false, false, null));
        }

        return TablasProyectoVer
                .data("title", "Name of Application")
                .data("entities", listaEntity)
                .data("apis", listaApi)
                .data("nombreProyecto", nombreProyecto)
//                .data("listaNueva", Data.tablasProyecto)
                .data("listaNueva", currentPv != null ? currentPv.tablas : new ArrayList<FormValue>());
    }

    @GET
    @Path("/folder/app/ver")
    public TemplateInstance GetFolderAppView() {
        // TODO: Find folder with pom.xml
        System.out.println(rutaFolder);


        ArrayList<String> folder = new ArrayList<>();
        if (!rutaFolder.isEmpty()) {
            File directory = new File(rutaFolder);
            if (directory.exists()) {
                File[] files = directory.listFiles();
                for (File file : Objects.requireNonNull(files)) {
                    if (file.isDirectory()) {
                        boolean tieneArchivoPomGradle = listFiles(file.getAbsolutePath());
                        if (tieneArchivoPomGradle) {
                            folder.add(file.getName());
                            boolean existe = false;
                            for (ProyectoValue pv : Data.tablasProyecto) {
                                if (pv.nombreProyecto.equals(file.getName())) {
                                    existe = true;
                                    break;
                                }
                            }
                            if (!existe) {
                                ProyectoValue proyectoValue = new ProyectoValue(file.getName(), new ArrayList<FormValue>());
                                Data.tablasProyecto.add(proyectoValue);
                            }

                        }
                    }
                }
            }
        }


        for (String name : folder) {
            System.out.println(name);
        }
        return FolderAppView
                .data("title", "Name of Application")
                .data("rutas", folder);
    }

    @GET
    @Path("/app/folder")
    public TemplateInstance GetFolderApp() {
        return FolderApp.data("title", "Name of Application");
    }

    @POST
    @Path("/app/folder")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public boolean GetFolderApp(@FormParam("ruta") String ruta) throws IOException {
        rutaFolder = ruta;
        return true;
    }

    @POST
    @Path("/folder/app/ver")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean FolderAppSeleccion(ArrayList<AppFolder> appFolders) {
        //TODO: validar username y password
        for (AppFolder appFolder : appFolders) {
            int seguridad = ((appFolder.seguridadCheckbox) ? 1 : 0);
            int microservicio = ((appFolder.microservicioCheckbox) ? 1 : 0);
            proyects.add(new String[]{appFolder.nombreFolder, String.valueOf(seguridad), String.valueOf(microservicio)});

            System.out.println("Nombre -> " + appFolder.nombreFolder);
            System.out.println("Microservicio -> " + appFolder.microservicioCheckbox);
            System.out.println("Security -> " + appFolder.seguridadCheckbox);
        }

        // AGREGA DEPENDENCIAS NECESARIAS
        for (AppFolder appFolder : appFolders)
        {
            agregarDependencias(appFolder.nombreFolder);
        }
        // AGREGA CLASES CREADAS
        for (ProyectoValue pv : Data.tablasProyecto) {
            System.out.println("Proyecto Actual: " + pv.nombreProyecto);
            for (FormValue fv : pv.tablas) {
                System.out.println("Tabla: " + fv.nombreTabla);
                crearClase(fv, pv.nombreProyecto);
                for (Form form : fv.filas) {
                    System.out.println("columna: " + form.nombre);
                }
            }
        }

        return false;
    }

    private boolean listFiles(String ruta) {
        File folder = new File(ruta);

        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                if (file.getName().equals("JF-LINP.txt")) {
                    return true;
                }
            } else if (file.isDirectory()) {
                listFiles(file.getAbsolutePath());
            }
        }
        return false;
    }

    private boolean listApi(String ruta, boolean encontrado) {
        File folder = new File(ruta);

        File[] files = folder.listFiles();
        if (encontrado) {
            rutaApi = ruta;
            return true;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                if (file.getName().equals("Api")) {
                    listApi(file.getAbsolutePath(), true);
                } else {
                    if (!file.getName().equals("target")) {
                        listApi(file.getAbsolutePath(), false);
                    }
                }
            }
        }
        return false;
    }

    private boolean listEntity(String ruta, boolean encontrado) {
        File folder = new File(ruta);

        File[] files = folder.listFiles();
        if (encontrado) {
            rutaEntity = ruta;
            return true;
        }
//        System.out.println(ruta);
        for (File file : files) {
            if (file.isDirectory()) {
                if (file.getName().equals("Entity")) {
                    listEntity(file.getAbsolutePath(), true);
                } else {
                    if (!file.getName().equals("target")) {
                        listEntity(file.getAbsolutePath(), false);
                    }
                }
            }
        }
        return false;
    }

    public void agregarDependencias(String proyecto) {

        int seguridad = 0;
        int microservicio = 0;

        String path = rutaFolder + "/";

        String appProp = "#Datasource Config\n" +
                "quarkus.datasource.db-kind=mysql\n" +
                "quarkus.datasource.driver=com.mysql.cj.jdbc.Driver\n" +
                "quarkus.datasource.username=root\n" +
                "quarkus.datasource.password=12345678\n" +
                "quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/employees\n" +
                "quarkus.hibernate-orm.log.sql=true\n" +
                "# drop and create the database at startup (use `update` to only update the schema)\n" +
                "quarkus.hibernate-orm.database.generation=update\n" +
                "quarkus.smallrye-openapi.path=/swagger\n" +
                "quarkus.swagger-ui.always-include=true\n" +
                "quarkus.swagger-ui.path=/explorer\n" +
                "mp.openapi.extensions.smallrye.operationIdStrategy=METHOD\n\n";

        for (String[] a : proyects) {
            if (a[0].equals(proyecto)) {
                seguridad = Integer.parseInt(a[1]);
                microservicio = Integer.parseInt(a[2]);
            }
        }

        String dockerCompose = "version: \"3\"\n" +
                "services:\n";


        if (seguridad == 1) {
            appProp = appProp + "# Keycloak with 100 offset\n" +
                    "keycloak.url=http://localhost:8180\n" +
                    "\n" +
                    "quarkus.oidc.enabled=true\n" +
                    "quarkus.oidc.auth-server-url=${keycloak.url}/auth/realms/quarkus-realm\n" +
                    "quarkus.oidc.client-id=quarkus-client\n" +
                    "quarkus.oidc.credentials.secret=mysecret\n" +
                    "quarkus.http.cors=true\n" +
                    "quarkus.oidc.tls.verification=none\n" +
                    "grant_type=password\n" +
                    "urltoken_request=${keycloak.url}/auth/realms/quarkus-realm/protocol/openid-connect/token\n\n";

            dockerCompose = dockerCompose + "\n  keycloak:\n" +
                    "    image: quay.io/keycloak/keycloak:7.0.1\n" +
                    "    ports:\n" +
                    "    - 8180:8180\n" +
                    "    environment:\n" +
                    "      - KEYCLOAK_IMPORT=/tmp/quarkus-realm.json\n" +
                    "      - KEYCLOAK_USER=admin\n" +
                    "      - KEYCLOAK_PASSWORD=admin\n" +
                    "    command: [\"-Djboss.http.port=8180\", \"-Dkeycloak.profile.feature.upload_scripts=enabled\"]\n" +
                    "    volumes:\n" +
                    "    - ./quarkus-realm.json:/tmp/quarkus-realm.json";

            java.nio.file.Path path2 = Paths.get(path + "/" + proyecto + "/pom.xml");
            Charset charset = StandardCharsets.UTF_8;

            String content = null;
            try {
                content = new String(Files.readAllBytes(path2), charset);
            } catch (IOException e) {
                e.printStackTrace();
            }
            content = content.replaceAll("<dependency>\n" +
                            "      <groupId>io.quarkus</groupId>\n" +
                            "      <artifactId>quarkus-resteasy-jsonb</artifactId>\n" +
                            "    </dependency>"

                    , "<dependency>\n" +
                            "      <groupId>io.quarkus</groupId>\n" +
                            "      <artifactId>quarkus-resteasy-jsonb</artifactId>\n" +
                            "    </dependency>" +
                            "   <dependency>\n" +
                            "      <groupId>io.quarkus</groupId>\n" +
                            "      <artifactId>quarkus-oidc</artifactId>\n" +
                            "    </dependency>\n" +
                            "    <dependency>\n" +
                            "      <groupId>io.quarkus</groupId>\n" +
                            "      <artifactId>quarkus-keycloak-authorization</artifactId>\n" +
                            "    </dependency>" +
                            "   </dependencies>");

            try {
                Files.write(path2, content.getBytes(charset));
            } catch (IOException e) {
                e.printStackTrace();
            }

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
                    "    private String client_secret;\n" +
                    "    @ConfigProperty(name=\"urltoken_request\")\n" +
                    "    private String request;\n" +
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

            try {
                File myObj = new File(path + "/" + proyecto + "/src/main/java/org/proyecto/Api/UserToken.java");
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
                FileWriter myWriter = new FileWriter(path + "/" + proyecto + "/src/main/java/org/proyecto/Api/UserToken.java");
                myWriter.write(UserApi);
                myWriter.close();
                //   System.out.println("Clase api generado");
            } catch (IOException e) {
                System.out.println("Se produjo un error.");
                e.printStackTrace();
            }

            try {
                File myObj = new File(path + "/" + proyecto + "/quarkus-realm.json");
                if (myObj.createNewFile()) {
                    // System.out.println("File created: " + myObj.getName());
                } else {
                    //  System.out.println("Archivo ya existe.");
                }
            } catch (
                    IOException e) {
                System.out.println("Se produjo un error.");
                e.printStackTrace();
            }

            try {
                FileWriter myWriter = new FileWriter(path + "/" + proyecto + "/quarkus-realm.json");
                JSONWriter aux = new JSONWriter();
                String config = aux.getConfig();
                myWriter.write(config);
                myWriter.close();
                //   System.out.println("Clase api generado");
            } catch (
                    IOException e) {
                System.out.println("Se produjo un error.");
                e.printStackTrace();
            }

        }

        if (microservicio == 1) {
            appProp = appProp + "quarkus.http.port=0\n" +
                    "quarkus.application.name=" + proyecto + "\n" +
                    "quarkus.application.version=1.0\n" +
                    "quarkus.consul-config.enabled=true\n" +
                    "quarkus.consul-config.properties-value-keys=config/${quarkus.application.name}";

            dockerCompose = dockerCompose + "\n  consul:\n" +
                    "    image: consul:latest\n" +
                    "    ports:\n" +
                    "    - 8500:8500\n" +
                    "    environment:\n" +
                    "      - CONSUL_BIND_INTERFACE=eth0";

            java.nio.file.Path path2 = Paths.get(path + "/" + proyecto + "/pom.xml");
            Charset charset = StandardCharsets.UTF_8;

            String content = null;
            try {
                content = new String(Files.readAllBytes(path2), charset);
            } catch (IOException e) {
                e.printStackTrace();
            }
            content = content.replaceAll("<dependency>\n" +
                            "      <groupId>io.quarkus</groupId>\n" +
                            "      <artifactId>quarkus-resteasy-jsonb</artifactId>\n" +
                            "    </dependency>"

                    , "<dependency>\n" +
                            "      <groupId>io.quarkus</groupId>\n" +
                            "      <artifactId>quarkus-resteasy-jsonb</artifactId>\n" +
                            "    </dependency>" +
                            "<      dependency>\n" +
                            "      <groupId>org.apache.camel.quarkus</groupId>\n" +
                            "      <artifactId>camel-quarkus-consul</artifactId>\n" +
                            "    </dependency>\n" +
                            "   </dependencies>");
            try {
                Files.write(path2, content.getBytes(charset));
            } catch (IOException e) {
                e.printStackTrace();
            }


            String clase_micro = "";

            File theDir = new File(path + "/" + proyecto + "/src/main/java/org/proyecto/MicroServiceConsul/");
            if (!theDir.exists()) theDir.mkdirs();

            // carpeta cliente *****************************************

            theDir = new File(path + "/" + proyecto + "/src/main/java/org/proyecto/MicroServiceConsul/Client");
            if (!theDir.exists()) theDir.mkdirs();

            clase_micro = "package org.proyecto.MicroServiceConsul.Client;\n" +
                    "\n" +
                    "import java.net.URI;\n" +
                    "import java.util.List;\n" +
                    "import java.util.concurrent.atomic.AtomicInteger;\n" +
                    "\n" +
                    "import javax.ws.rs.client.ClientRequestContext;\n" +
                    "import javax.ws.rs.client.ClientRequestFilter;\n" +
                    "import javax.ws.rs.core.UriBuilder;\n" +
                    "\n" +
                    "import com.orbitz.consul.Consul;\n" +
                    "import com.orbitz.consul.HealthClient;\n" +
                    "import com.orbitz.consul.model.health.ServiceHealth;\n" +
                    "import org.slf4j.Logger;\n" +
                    "import org.slf4j.LoggerFactory;\n" +
                    "\n" +
                    "public class LoadBalancedFilter implements ClientRequestFilter {\n" +
                    "\n" +
                    "    private static final Logger LOGGER = LoggerFactory\n" +
                    "            .getLogger(LoadBalancedFilter.class);\n" +
                    "\n" +
                    "    private Consul consulClient;\n" +
                    "    private AtomicInteger counter = new AtomicInteger();\n" +
                    "\n" +
                    "    public LoadBalancedFilter(Consul consulClient) {\n" +
                    "        this.consulClient = consulClient;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void filter(ClientRequestContext ctx) {\n" +
                    "        URI uri = ctx.getUri();\n" +
                    "        HealthClient healthClient = consulClient.healthClient();\n" +
                    "        List<ServiceHealth> instances = healthClient\n" +
                    "                .getHealthyServiceInstances(uri.getHost()).getResponse();\n" +
                    "        instances.forEach(it ->\n" +
                    "                LOGGER.info(\"Instance: uri={}:{}\",\n" +
                    "                        it.getService().getAddress(),\n" +
                    "                        it.getService().getPort()));\n" +
                    "        ServiceHealth instance = instances.get(counter.getAndIncrement()%instances.size());\n" +
                    "        URI u = UriBuilder.fromUri(uri)\n" +
                    "                .host(instance.getService().getAddress())\n" +
                    "                .port(instance.getService().getPort())\n" +
                    "                .build();\n" +
                    "        ctx.setUri(u);\n" +
                    "    }\n" +
                    "\n" +
                    "}";

            try {
                File myObj = new File(path + "/" + proyecto + "/src/main/java/org/proyecto/MicroServiceConsul/Client/LoadBalancedFilter.java");
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
                FileWriter myWriter = new FileWriter(path + "/" + proyecto + "/src/main/java/org/proyecto/MicroServiceConsul/Client/LoadBalancedFilter.java");
                myWriter.write(clase_micro);
                myWriter.close();
            } catch (IOException e) {
                System.out.println("Se produjo un error.");
                e.printStackTrace();
            }

            // carpeta config ***********************************

            theDir = new File(path + "/" + proyecto + "/src/main/java/org/proyecto/MicroServiceConsul/Config/");
            if (!theDir.exists()) theDir.mkdirs();

            clase_micro = "package org.proyecto.MicroServiceConsul.Config;\n" +
                    "\n" +
                    "\n" +
                    "import javax.enterprise.context.ApplicationScoped;\n" +
                    "import javax.enterprise.inject.Produces;\n" +
                    "\n" +
                    "import com.orbitz.consul.Consul;\n" +
                    "import org.proyecto.MicroServiceConsul.Client.LoadBalancedFilter;\n" +
                    "\n" +
                    "@ApplicationScoped\n" +
                    "public class " + proyecto + "BeansProducer {\n" +
                    "\n" +
                    "\n" +
                    "    @Produces\n" +
                    "    Consul consulClient = Consul.builder().build();\n" +
                    "\n" +
                    "    @Produces\n" +
                    "    LoadBalancedFilter filter = new LoadBalancedFilter(consulClient);\n" +
                    "\n" +
                    "}\n";

            try {
                File myObj = new File(path + "/" + proyecto + "/src/main/java/org/proyecto/MicroServiceConsul/Config/" + proyecto + "BeansProducer.java");
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
                FileWriter myWriter = new FileWriter(path + "/" + proyecto + "/src/main/java/org/proyecto/MicroServiceConsul/Config/" + proyecto + "BeansProducer.java");
                myWriter.write(clase_micro);
                myWriter.close();
            } catch (IOException e) {
                System.out.println("Se produjo un error.");
                e.printStackTrace();
            }

            // carpeta liceCycle ********************************
            theDir = new File(path + "/" + proyecto + "/src/main/java/org/proyecto/MicroServiceConsul/LifeCycle/");
            if (!theDir.exists()) theDir.mkdirs();

            clase_micro = "package org.proyecto.MicroServiceConsul.LifeCycle;\n" +
                    "\n" +
                    "import java.util.List;\n" +
                    "import java.util.concurrent.Executors;\n" +
                    "import java.util.concurrent.ScheduledExecutorService;\n" +
                    "import java.util.concurrent.TimeUnit;\n" +
                    "\n" +
                    "import javax.enterprise.context.ApplicationScoped;\n" +
                    "import javax.enterprise.event.Observes;\n" +
                    "import javax.inject.Inject;\n" +
                    "\n" +
                    "import com.orbitz.consul.Consul;\n" +
                    "import com.orbitz.consul.HealthClient;\n" +
                    "import com.orbitz.consul.model.agent.ImmutableRegistration;\n" +
                    "import com.orbitz.consul.model.health.ServiceHealth;\n" +
                    "import io.quarkus.runtime.ShutdownEvent;\n" +
                    "import io.quarkus.runtime.StartupEvent;\n" +
                    "import org.eclipse.microprofile.config.inject.ConfigProperty;\n" +
                    "import org.slf4j.Logger;\n" +
                    "import org.slf4j.LoggerFactory;\n" +
                    "\n" +
                    "@ApplicationScoped\n" +
                    "public class " + proyecto + "LifeCycle {\n" +
                    "\n" +
                    "    private static final Logger LOGGER = LoggerFactory.getLogger(" + proyecto + "LifeCycle.class);\n" +
                    "\n" +
                    "    private String instanceId;\n" +
                    "\n" +
                    "    @Inject\n" +
                    "    Consul consulClient;\n" +
                    "    @ConfigProperty(name = \"quarkus.application.name\")\n" +
                    "    String appName;\n" +
                    "    @ConfigProperty(name = \"quarkus.application.version\")\n" +
                    "    String appVersion;\n" +
                    "\n" +
                    "\n" +
                    "    void onStart(@Observes StartupEvent ev) {\n" +
                    "        ScheduledExecutorService executorService = Executors\n" +
                    "                .newSingleThreadScheduledExecutor();\n" +
                    "        executorService.schedule(() -> {\n" +
                    "            HealthClient healthClient = consulClient.healthClient();\n" +
                    "            List<ServiceHealth> instances = healthClient\n" +
                    "                    .getHealthyServiceInstances(appName).getResponse();\n" +
                    "            instanceId = appName + \"-\" + instances.size();\n" +
                    "            ImmutableRegistration registration = ImmutableRegistration.builder()\n" +
                    "                    .id(instanceId)\n" +
                    "                    .name(appName)\n" +
                    "                    .address(\"127.0.0.1\")\n" +
                    "                    .port(Integer.parseInt(System.getProperty(\"quarkus.http.port\")))\n" +
                    "                    .putMeta(\"version\", appVersion)\n" +
                    "                    .build();\n" +
                    "            consulClient.agentClient().register(registration);\n" +
                    "            LOGGER.info(\"Instance registered: id={}\", registration.getId());\n" +
                    "        }, 5000, TimeUnit.MILLISECONDS);\n" +
                    "    }\n" +
                    "\n" +
                    "    void onStop(@Observes ShutdownEvent ev) {\n" +
                    "        consulClient.agentClient().deregister(instanceId);\n" +
                    "        LOGGER.info(\"Instance de-registered: id={}\", instanceId);\n" +
                    "    }\n" +
                    "\n" +
                    "}\n";

            try {
                File myObj = new File(path + "/" + proyecto + "/src/main/java/org/proyecto/MicroServiceConsul/LifeCycle/" + proyecto + "LifeCycle.java");
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
                FileWriter myWriter = new FileWriter(path + "/" + proyecto + "/src/main/java/org/proyecto/MicroServiceConsul/LifeCycle/" + proyecto + "LifeCycle.java");
                myWriter.write(clase_micro);
                myWriter.close();
            } catch (IOException e) {
                System.out.println("Se produjo un error.");
                e.printStackTrace();
            }

        }


        try {
            File myObj = new File(path + "/" + proyecto + "//src/main/resources/application.properties");
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
            FileWriter myWriter = new FileWriter(path + "/" + proyecto + "/src/main/resources/application.properties");
            myWriter.write(appProp);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        try {
            File myObj = new File(path + "/" + proyecto + "/docker-compose.yml");
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
            FileWriter myWriter = new FileWriter(path + "/" + proyecto + "/docker-compose.yml");
            myWriter.write(dockerCompose);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

    }

    public void crearClase(FormValue formValue, String nombre) {

        String nomb;
        String clase;
        String atributo;
        String tipo;
        String modelos = "\n";
        String getset = "\n";
        String entidad = "\n";
        String tipopk = "long";
        int haypk = 0;
        //arreglando
        int seguridad = 0;

        for (String[] a : proyects) {
            if (a[0].equals(nombre)) {
                seguridad = Integer.parseInt(a[1]);
            }
        }

        File theDir = new File(rutaFolder + "/" + nombre + "/src/main/java/org/proyecto/Entity/");
        if (!theDir.exists()) theDir.mkdirs();

        theDir = new File(rutaFolder + "/" + nombre + "/src/main/java/org/proyecto/Api/");
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
                    "import javax.persistence.*;\n" +
                    "import java.sql.Date;\n" +
                    "import java.io.Serializable;\n" +
                    "import java.util.Set;\n";

            if (haypk == 1) {
                //String path = System.getProperty("user.dir");
                archivojava = archivojava +
                        "@Entity\n" +
                        "public class " + clase + " extends PanacheEntityBase implements Serializable{\n"

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

                        modelos

                        +

                        getset

                        +
                        "}";
            }


            try {
                File myObj = new File(rutaFolder + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");
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
                FileWriter myWriter = new FileWriter(rutaFolder + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");
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
                File myObj = new File(rutaFolder + "/" + nombre + "/src/main/java/org/proyecto/Api/" + clase + "Api.java");
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
                FileWriter myWriter = new FileWriter(rutaFolder + "/" + nombre + "/src/main/java/org/proyecto/Api/" + clase + "Api.java");

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

}
