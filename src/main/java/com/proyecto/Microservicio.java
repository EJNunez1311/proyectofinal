package com.proyecto;

import Entities.AppFolder;
import Entities.Data;
import Entities.Form;
import Entities.FormValue;
import Entities.ProyectoValue;
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

    private String rutaEntity ="";
    private String rutaApi ="";
    String rutaCarpetaMadre = "";
    String nombre = "";
    int seguridad = 0;

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
        nombre = name;
        System.out.println("Nombre -> " + name);

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

        // Runnable r = new Create(name, seguridad);

        // new Thread(r).start();

        ProyectoValue proyectoValue = new ProyectoValue(name, new ArrayList<FormValue>()); 
        Data.proyectosGenerados.add(proyectoValue);

        return Response.ok().build();
    }

    @GET
    @Path("/importar/tablas/{proyecto}")
    public TemplateInstance ImportarTablasBaseDatos(@PathParam("proyecto") String proyecto) {
        //TODO: Importar la tablas desde la base de datos
        //TODO: Asignar estas tablas en la lista de tablas del prooyecto especificado

        return MicroservicioVer
            .data("title", "Table View")
            .data("proyectosGenerados", Data.proyectosGenerados);
    }
      
    @GET
    @Path("/ver")
    public TemplateInstance GetProyectosGenerados() {

        //TODO: Utilizar el codigo de abajo para leer la ruta de la carpeta madre
        String path = System.getProperty("user.dir");
        // File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Entity/" + clase + ".java");

        // ArrayList<String> folder = new ArrayList<>();
        // if (!rutaCarpetaMadre.isEmpty()) {
        //     File directory = new File(rutaCarpetaMadre);
        //     if (directory.exists()) {
        //         File[] files = directory.listFiles();
        //         for (File file : Objects.requireNonNull(files)) {
        //             if (file.isDirectory()) {
        //                 boolean tieneArchivoPomGradle = listFiles(file.getAbsolutePath());
        //                 if (tieneArchivoPomGradle) {
        //                     folder.add(file.getName());
        //                 }
        //             }
        //         }
        //     }
        // }

        return MicroservicioVer
            .data("title", "Table View")
            .data("proyectosGenerados", Data.proyectosGenerados); // Devolver la lista folder
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
                    pv.tablas.add(formValue);
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

    @POST
    @Path("/finalizar")
    public boolean Finalizar() {
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
}
