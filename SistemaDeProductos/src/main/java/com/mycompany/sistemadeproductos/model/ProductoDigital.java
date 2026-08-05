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
        
        // Condicion para calcular el precio final con descuento
        
        if(peso > 10){
            
        double recargo = getPrecioBase() * 0.08;    
            
        precioFinal = getPrecioBase() + costoEnvio + recargo;
        
        return precioFinal;
        }
        
        // Calcular precio final
        
        precioFinal = getPrecioBase() + costoEnvio;
        
        return precioFinal;
    }
    
    
    
    
    
}
