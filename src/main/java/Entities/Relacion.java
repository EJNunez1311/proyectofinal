package Entities;

public class Relacion {
    public String nombre;
    public String valor;

    public Relacion() {
    }

    public Relacion(String nombre, String valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }


    public void setValor(String valor) {
        this.valor = valor;
    }
}
