/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadeproductos.model;

/**
 *
 * @author cohorte5
 */
public class ProductoDigital extends Producto{
    
    private double tamañoArchivo;
    private String formato;

    public ProductoDigital(String codigo, String nombre, double precioBase, int cantidadDisponible, boolean activo, double tamañoArchivo, String formato){
        super(codigo, nombre, precioBase, cantidadDisponible, activo);
        this.tamañoArchivo = tamañoArchivo;
        this.formato = formato;
    }

    @Override
    public double calcularPrecioFinal() {
        
        double precioFinal;
        double descuento;
        
        // Condicion para calcular el precio final con descuento del 5%
        
        if(tamañoArchivo > 5){
            
        descuento = getPrecioBase() * 0.05;    
            
        precioFinal = getPrecioBase() - descuento;
        
        return precioFinal;
        }
        
        // Calcular precio final con descuento del 10%
        
        descuento = getPrecioBase() * 0.10;
        
        precioFinal = getPrecioBase() - descuento ;
        
        return precioFinal;
    }

    
    
    // Getters y Setters
    
    // Tamaño del Archivo
    
    public double getTamañoArchivo() {
        return tamañoArchivo;
    }
    public void setTamañoArchivo(double tamañoArchivo) {
        this.tamañoArchivo = tamañoArchivo;
    }

    // Formato del archivo
    
    public String getFormato() {
        return formato;
    }


    public void setFormato(String formato) {
        this.formato = formato;
    }

    
    
}
