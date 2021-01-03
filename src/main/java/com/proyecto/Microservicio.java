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
    Template FormProyecto;

    private String rutaEntity ="";
    private String rutaApi ="";
    String rutaFolder = "";
    String nombre = "";

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

//TODO: Crear MicroServicio

//        Runnable r = new Create(nombre);
//
//        new Thread(r).start();

        return Response.ok().build();

    }
}
