package com.example.demo;

public class Banco {
    private String nombre;
    private double saldo;
    private String cuenta;
    private int banco_id;
    public int getBanco_id() {
        return banco_id;
    }

    public void setBanco_id(int banco_id) {
        this.banco_id = banco_id;
    }

    public Banco(String nombre, double saldo, String cuenta, int banco_id) {
        this.nombre = nombre;
        this.saldo = saldo;
        this.cuenta = cuenta;
        this.banco_id = banco_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getCuenta() {
        return cuenta;
    }
}
