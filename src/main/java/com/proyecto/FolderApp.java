package com.proyecto;

import Entities.AppFolder;
import Entities.Data;
import Entities.Form;
import Entities.FormValue;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.runtime.Startup;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
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

    private String rutaEntity ="";
    private String rutaApi ="";
    String rutaFolder = "";

    ArrayList<FormValue> listaTablasCreadas = new ArrayList<>();


    @GET
    @Path("/folder/app/ver/{nombreProyecto}")
    public TemplateInstance GetFolderAppTableView(@PathParam("nombreProyecto") String nombreProyecto) {
        // TODO: Find folder with pom.xml
        ArrayList<String> listaApi = new ArrayList<>();
        ArrayList<String> listaEntity = new ArrayList<>();
        System.out.println(rutaFolder);
        if (!rutaFolder.isEmpty()) {
            File directory = new File(rutaFolder + '\\'+ nombreProyecto);
            if (directory.exists()) {
                listApi(directory.getAbsolutePath(), false);
                System.out.println("StringaApi-> "+ rutaApi);
                if (!rutaApi.isEmpty()) {
                    File apis = new File(rutaApi);
                    for (File file : apis.listFiles()) {
                        if (file.isFile()){
                            int lastPeriodPos = file.getName().lastIndexOf('.');
                            listaApi.add(file.getName().substring(0, lastPeriodPos));
                        }
                    }
                }

                listEntity(directory.getAbsolutePath(), false);
                System.out.println("StringEntity-> "+ rutaEntity);
                if (!rutaEntity.isEmpty()) {
                    File entities = new File(rutaEntity);
                    for (File file : entities.listFiles()) {
                        if (file.isFile()){
                            int lastPeriodPos = file.getName().lastIndexOf('.');
                            listaEntity.add(file.getName().substring(0, lastPeriodPos));
                        }
                    }
                }
            }
        }
        ArrayList <String> listaTablas = (ArrayList<String>) Stream.of(listaApi, listaEntity)
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
                .data("listaNueva", Data.tablasProyecto);
    }


    @GET
    @Path("/folder/form/{nombreProyecto}")
    public TemplateInstance ProyectoTableCreation(@PathParam("nombreProyecto") String nombreProyecto) {


        return FormProyecto.data("title", "Table Creation")
                .data("tipoAtributos", Data.obtenerAtributos())
                .data("tablasCreadas", listaTablasCreadas)
                .data("nombreProyecto", nombreProyecto)
                .data("relaciones", Data.obtenerRelaciones());
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

        Data.tablasProyecto.add(formValue);

        return true;
    }

    @GET
    @Path("/folder/form/actualizar/{nombreProyecto}/{index}")
    public TemplateInstance TableUpdateProyecto(@PathParam("index") int index, @PathParam("nombreProyecto") String nombreProyecto) {
        FormValue formValue = new FormValue();
        if (index <= Data.tablasProyecto.size()) {
            formValue = Data.tablasProyecto.get(index - 1);
        }

        return FormUpdateProyecto.data("title", "Table Update")
                .data("tablaDetalle", formValue)
                .data("index", index)
                .data("nombreProyecto", nombreProyecto)
                .data("tipoAtributos", Data.obtenerAtributos())
                .data("tablasCreadas", listaTablasCreadas)
                .data("relaciones", Data.obtenerRelaciones());
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
        if (index <= Data.tablasProyecto.size()) {
            FormValue formValueActual = Data.tablasProyecto.get(index - 1);
            if (formValueActual != null) {
                formValueActual.nombreTabla = formValue.nombreTabla;
                formValueActual.creado = formValue.creado;
                formValueActual.filas = formValue.filas;
            }
        }
        return true;
    }

    @GET
    @Path("/folder/form/eliminar/{nombreProyecto}/{index}")
    public TemplateInstance TableDeletePdoyecto(@PathParam("index") int index, @PathParam("nombreProyecto") String nombreProyecto) {
        if (index <= Data.tablasProyecto.size()) {
            Data.tablasProyecto.remove(index - 1);
        }

        ArrayList<String> listaApi = new ArrayList<>();
        ArrayList<String> listaEntity = new ArrayList<>();
        System.out.println(rutaFolder);
        if (!rutaFolder.isEmpty()) {
            File directory = new File(rutaFolder + '\\'+ nombreProyecto);
            if (directory.exists()) {
                listApi(directory.getAbsolutePath(), false);
                System.out.println("StringaApi-> "+ rutaApi);
                if (!rutaApi.isEmpty()) {
                    File apis = new File(rutaApi);
                    for (File file : apis.listFiles()) {
                        if (file.isFile()){
                            int lastPeriodPos = file.getName().lastIndexOf('.');
                            listaApi.add(file.getName().substring(0, lastPeriodPos));
                        }
                    }
                }

                listEntity(directory.getAbsolutePath(), false);
                System.out.println("StringEntity-> "+ rutaEntity);
                if (!rutaEntity.isEmpty()) {
                    File entities = new File(rutaEntity);
                    for (File file : entities.listFiles()) {
                        if (file.isFile()){
                            int lastPeriodPos = file.getName().lastIndexOf('.');
                            listaEntity.add(file.getName().substring(0, lastPeriodPos));
                        }
                    }
                }
            }
        }
        ArrayList <String> listaTablas = (ArrayList<String>) Stream.of(listaApi, listaEntity)
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
                .data("listaNueva", Data.tablasProyecto);
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
    //Metodo para recibir el nombre de la app y generar los primero parametros de la app!
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
            System.out.println("Nombre -> " + appFolder.nombreFolder);
            System.out.println("Microservicio -> " + appFolder.microservicioCheckbox);
            System.out.println("Security -> " + appFolder.seguridadCheckbox);
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
                    listApi(file.getAbsolutePath(), false);
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
        for (File file : files) {
            if (file.isDirectory()) {
                if (file.getName().equals("Entity")) {
                    listEntity(file.getAbsolutePath(), true);
                } else {
                    listEntity(file.getAbsolutePath(), false);
                }
            }
        }
        return false;
    }

}
