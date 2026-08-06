/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadeproductos.model;

/**
 *
 * @author cohorte5
 */
public abstract class Producto {
    
    // Atributos 
    private String codigo;
    private String nombre;
    private double precioBase;
    private int cantidadDisponible;
    private boolean activo;

    // Contructor 
    public Producto(String codigo, String nombre, double precioBase, int cantidadDisponible,boolean activo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.cantidadDisponible = cantidadDisponible;
        this.activo = activo;
    }
    
    // Getters y Setters 
    
    // Codigo

    public String getCodigo() {
        return codigo;
    }

    // Nombre
    
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Precio Base
    
    public double getPrecioBase() {
        return precioBase;
    }
    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    // Cantidad disponible
    
    public int getCantidadDisponible() {
        return cantidadDisponible;
    }
    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    // Activo
    
    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

        
    // Métedo Calcular precio final
    
    public abstract double calcularPrecioFinal();
    
    // Método Mostrar informacion
    
    public String mostrarInformacion(){
        return "";
    }
    
    // Método Calcular valor inventario
    
    public double calcularValorInventario(){
        
        double valorInventario = calcularPrecioFinal() * cantidadDisponible;
        
        return valorInventario;
    }
    
     
    
    
}
