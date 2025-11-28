package com.ticketsystem.model;

public class User {
    private int id;
    private String usuario;
    private String password;
    private String rol;

    public User(int id, String usuario, String password, String rol) {
        this.id = id;
        this.usuario = usuario;
        this.password = password;
        this.rol = rol;
    }

    public int getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }
}
