package com.example.reto10.Model;

public class Funcionarios {

    private String nombre;
    private String cargo;
    private String dependencia;
    private String subdependencia;
    private  String tipo_vinculaci_n;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDependencia() {
        return dependencia;
    }

    public void setDependencia(String dependencia) {
        this.dependencia = dependencia;
    }

    public String getSubdependencia() {
        return subdependencia;
    }

    public void setSubdependencia(String subdependencia) {
        this.subdependencia = subdependencia;
    }

    public String getTipo_vinculaci_n() {
        return tipo_vinculaci_n;
    }

    public void setTipo_vinculaci_n(String tipo_vinculaci_n) {
        this.tipo_vinculaci_n = tipo_vinculaci_n;
    }
}
