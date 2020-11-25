package com.proyecto;

import Entities.FormValue;
import java.io.*;

public class Create implements Runnable {

    String nombre;
    int seguridad = 0;
    int microservicio = 0;

    public Create(String name,int seg, int serv){
        nombre = name;
        seguridad = seg;
        microservicio = serv;
    }

    public void CrearClase (FormValue clase){
        clase.getFilas();

    }

    @Override
    public void run() {

        // Crear Aplicacion con Nombre e Importar toda las libs necesaria con los cmd de Mvn
        //////////////////////////////////////////////////////////

        String path = System.getProperty("user.dir");
        String userHome = System.getProperty("user.home");
        String comandos, archivo_comando;
        comandos =
                "mvn io.quarkus:quarkus-maven-plugin:1.8.2.Final:create -DprojectGroupId=org.proyecto " +
                        "-DprojectArtifactId=" + nombre +
                        " -DclassName=\"org.proyecto.Apiapp\" -Dpath=\"/hello\"\n";
//        comando2= "cd "+ nombre + "\n";
//        comando3 = "mvn quarkus:add-extension -Dextensions=\"agroal\"";
//        comando4 = "mvn quarkus:add-extension -Dextensions=\"quarkus-hibernate-orm-panache\" \n";
//        comando5="mvn quarkus:add-extension -Dextensions=\"jdbc-mysql\" \n";
//        comando6="mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-smallrye-openapi\"\n";
//        comando7="mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-spring-security\"\n";
//        comando8="mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-resteasy-jsonb\"\n";


        archivo_comando = "cd " + path + "\\" + nombre + "\n" +
                "\n" +
                "call mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-agroal\"\n" +
                "\n" +
                "call mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-hibernate-orm-panache\"\n" +
                "\n" +
                "call mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-jdbc-mysql\"\n" +
                "\n" +
                "call mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-smallrye-openapi\"\n" +
                "\n" +
                "call mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-resteasy-jsonb\"";

        if(seguridad != 0){
            archivo_comando = archivo_comando + "call mvn quarkus:add-extension -Dextensions=\"oidc,keycloak-authorization\"\n";
        }
        //System.out.println(comandos);

        ProcessBuilder processBuilder = new ProcessBuilder();
        ProcessBuilder processBuilder2 = new ProcessBuilder();
        // -- Linux --

        // Run a shell command
        //   processBuilder.command("bash", "-c", comandos);

        // -- Windows --

        // Run a command
        processBuilder.command("cmd.exe", "/c", comandos);


//        File currentDir = new File(".");
        try {
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
//                System.out.println("App creada!");
//                System.out.println(archivo_comando);
//                System.out.println(output);
//                System.exit(0);
//                System.out.println("Working Directory = " + path);
//                System.out.println(userHome);
            } else {
                //abnormal...
            }
            process.destroyForcibly();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ///////////////////////////////////////////////////////////////////////////////////////////
        try {
            File myObj = new File(path + "/comando_add.bat");
            if (myObj.createNewFile()) {
//                System.out.println("Archivo Creado: " + myObj.getName());
            } else {
//                System.out.println("Archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(path + "/comando_add.bat");
            myWriter.write(archivo_comando);
            myWriter.close();
            //  System.out.println("Archivo bat creado.");
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }
        processBuilder2.command("cmd.exe", "/c", "comando_add.bat");
        try {
            Process process2 = processBuilder2.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process2.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process2.waitFor();
            if (exitVal == 0) {
                //  System.out.println("Archivo Bat ejecutado");
//                System.out.println(archivo_comando);
//                System.out.println(output);
//                System.exit(0);
//                System.out.println("Working Directory = " + path);
//                System.out.println(userHome);
            } else {
                //abnormal...
            }
            process2.destroyForcibly();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            File myObj = new File(path + "/" + nombre + "/src/main/resources/application.properties");
            if (myObj.createNewFile()) {
                //  System.out.println("Archivo Creado: " + myObj.getName());
            } else {
                //   System.out.println("Archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        try {

            FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/resources/application.properties");
            String apppropert = "#Datasource Config\n" +
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
            System.out.println(seguridad);
            if(seguridad != 0){
                apppropert = apppropert + "# Keycloak with 100 offset\n" +
                        "keycloak.url=http://localhost:8180\n" +
                        "\n" +
                        "quarkus.oidc.enabled=true\n" +
                        "quarkus.oidc.auth-server-url=${keycloak.url}/auth/realms/quarkus-realm\n" +
                        "quarkus.oidc.client-id=quarkus-client\n" +
                        "quarkus.oidc.credentials.secret=mysecret\n" +
                        "quarkus.http.cors=true\n" +
                        "quarkus.oidc.tls.verification=none\n";
            }
            myWriter.write(apppropert);
            myWriter.close();
            //  System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        //////////////////////////////////////////////////////////

    }
}
