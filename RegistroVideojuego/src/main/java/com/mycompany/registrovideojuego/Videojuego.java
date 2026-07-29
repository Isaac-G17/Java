/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registrovideojuego;

import javax.swing.JOptionPane;

/**
 *
 * @author cohorte5
 */
public class Videojuego {
    
    private String nombre;
    private String desarrollador;
    private double precio;
    private float horasDeJuego;
    private char clasificacion;
    private boolean multijugador;
    
    
    
    
    public Videojuego(String nombre,String desarrollador,double precio,float horasDeJuego, char clasificacion, boolean multijugador){
           
        this.nombre=nombre;
        this.desarrollador=desarrollador;
        this.precio=precio;
        this.horasDeJuego=horasDeJuego;
        this.clasificacion=clasificacion;
        this.multijugador=multijugador;
    
    }
    
    public void mostrarInformacion(){
        
        String rangoPrecio;
        String duracion;
        
        
        if(precio < 100000 ){
            rangoPrecio="Económico";            
        }else if (precio >= 100000 && precio <= 200000){
            rangoPrecio="Medio";            
        }else{
            rangoPrecio="Premium";
        }
        
        
         if(horasDeJuego < 20 ){
            duracion="Corta";            
        }else if (horasDeJuego >= 20 && horasDeJuego <= 50){
            duracion="Media";            
        }else{
            duracion="Larga";
        }
        
        
        
        String info = "========== VIDEOJUEGO REGISTRADO ==========\n" +                      
                      "Nombre: "  + nombre + "\n" +
                      "Desarrollador: "  + desarrollador + "\n" +
                      "Precio: $"  + precio + "\n" +
                      "Horas de juego: " + horasDeJuego + "horas\n" +
                      "Clasificación: "  + clasificacion + "\n" +
                      "Multijugador: "  + (multijugador ? "Si" : "No") + "\n" +
                      "Rango de precio: " + rangoPrecio + "\n" +
                      "Duracion de juego: " + duracion + "\n";
        
        
        System.out.println(info);
        JOptionPane.showMessageDialog(null, info);
    }
    
    public boolean esRecomendado(){
        
        if(precio < 150000 && horasDeJuego > 30){
            System.out.println("Recomendado para comprar.");
            JOptionPane.showMessageDialog(null,"Recomendado para comprar.");
            return true;
        }else{
            System.out.println("No recomendado según los criterios establecidos.");
            JOptionPane.showMessageDialog(null,"No recomendado según los criterios establecidos.");
            return false;
        }
    } 
}

