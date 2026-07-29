/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.registrovideojuego;

import javax.swing.JOptionPane;

/**
 *
 * @author cohorte5
 */
public class RegistroVideojuego {

    public static void main(String[] args) {
        
        String nombre=JOptionPane.showInputDialog("Ingrese el nombre del videojuego:");
        
        String desarrollador=JOptionPane.showInputDialog("Ingrese el nombre del desarrollador del videojuego:");
        
        double precio=Double.parseDouble(JOptionPane.showInputDialog("Ingrese el precio del videojuego:"));
        
        float horasDeJuego=Float.parseFloat(JOptionPane.showInputDialog("Ingrese las horas de juego del videojuego:"));
        
        char clasificacion=JOptionPane.showInputDialog("Ingrese la clasificacion del videojuego(E, T o M)").toUpperCase().charAt(0);
        
        
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Tiene Multijugador?", "Seleccione una opción", JOptionPane.YES_NO_OPTION);
        boolean multijugador = (respuesta == JOptionPane.YES_OPTION);
                
        Videojuego juego = new Videojuego(nombre,desarrollador,precio,horasDeJuego,clasificacion,multijugador);
        juego.mostrarInformacion();
        juego.esRecomendado();
    }
}
