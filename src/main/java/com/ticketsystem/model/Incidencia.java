package com.ticketsystem.model;

public class Incidencia {

    private int id;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String telefono;
    private String tipo;
    private String descripcion;
    private String estado = "En espera"; // Estado inicial por defecto
    private Integer idTecnico; // ID del técnico asignado, puede ser null

    public Incidencia(int id, String nombre, String primerApellido, String segundoApellido,
                      String telefono, String tipo, String descripcion, String estado, Integer idTecnico) {

        this.id = id;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.telefono = telefono;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.idTecnico = idTecnico;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPrimerApellido() { return primerApellido; }
    public String getSegundoApellido() { return segundoApellido; }
    public String getTelefono() { return telefono; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }

    public Integer getIdTecnico() { 
        return idTecnico; 
    }


    public void setIdTecnico(Integer idTecnico) {
        this.idTecnico = idTecnico;
    }

    public String getDetalles() {
        return descripcion;
    }

    public String getEstado() { 
        return estado; 
    }
}
