/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadeproductos.model;

/**
 *
 * @author cohorte5
 */
public class ProductoFisico extends Producto{
    
    // Atributos 
    
    private double peso;
    private double costoEnvio;

    // Constructor
    
    public ProductoFisico(String codigo, String nombre, double precioBase, int cantidadDisponible,boolean activo, double peso, double costoEnvio) {
        super(codigo, nombre, precioBase, cantidadDisponible, activo);
        this.peso = peso;
        this.costoEnvio = costoEnvio;
    }

    // Implementacion del método precio final

    @Override
    public double calcularPrecioFinal() {
        
        double precioFinal;
        
        // Condicion para calcular el precio final con recargo
        
        if(peso > 10){
            
        double recargo = getPrecioBase() * 0.08;    
            
        precioFinal = getPrecioBase() + costoEnvio + recargo;
        
        return precioFinal;
        }
        
        // Calcular precio final
        
        precioFinal = getPrecioBase() + costoEnvio;
        
        return precioFinal;
    }

    
    
    // Getters y Setters
 
    // Peso
    
    public double getPeso() {
        return peso;
    }
    public void setPeso(double peso) {
        this.peso = peso;
    }
    
    // Costo Envio

    public double getCostoEnvio() {
        return costoEnvio;
    }
    public void setCostoEnvio(double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }


    

}
