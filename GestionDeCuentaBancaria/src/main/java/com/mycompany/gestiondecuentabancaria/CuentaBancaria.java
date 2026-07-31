/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestiondecuentabancaria;

import javax.swing.JOptionPane;

/**
 *
 * @author cohorte5
 */
public class CuentaBancaria {
    
    private String numeroCuenta;
    private String nombreTitular;
    private double saldo;
    private boolean activa;
    private int cantidadMovimientos;

    public CuentaBancaria(String numeroCuenta, String nombreTitular, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.nombreTitular = nombreTitular;
        this.saldo = saldoInicial;    
        this.activa = true;
        this.cantidadMovimientos = 0;
    }
    
    // Getters 
    public String getNumeroCuenta(){
        return numeroCuenta;
    }
    
    public String getNombreTitular(){
        return nombreTitular;
    }
    
    public double getSaldo(){
        return saldo;
    }
    
    public boolean isActiva(){
        return activa;
    }
    
    public int getCantidadMovimientos(){
        return cantidadMovimientos; 
    }
    
    // Metodo consignar
    
    public boolean consignar(double cantidad){
       
        
        if(!activa){
            //JOptionPane.showMessageDialog(null,"La cuenta esta inactiva no se puede consignar");
            return false;
        }
        
        if(cantidad <= 0){
            //JOptionPane.showMessageDialog(null,"La cantidad a consignar no es validad");
            return false;
        }
        
        saldo += cantidad;
        cantidadMovimientos += 1;

        //
        
        return true;  
    }
    
    
    // Metodo retirar
    public boolean retirar(double cantidad){
        
        if(!activa){
            //JOptionPane.showMessageDialog(null,"La cuenta esta inactiva no se puede consignar");
            return false;
        }
        
        if(cantidad <= 0){
            //JOptionPane.showMessageDialog(null,"La cantidad a retirar no es validad");
            return false;
        }
        
        if(cantidad > saldo){
            //JOptionPane.showMessageDialog(null, "Fondos insuficientes");
            return false;
        }
        
        saldo -= cantidad;
        cantidadMovimientos += 1;
        //JOptionPane.showMessageDialog(null,"El nuevo saldo es: $" + saldo);
        
        return true;  
    }
    
    // Metodo consultar tipo de saldo
    public String obtenerTipoSaldo(){
        
        if(saldo < 100000){
            return "Saldo bajo";
        }else if(saldo < 1000000){
            return "Saldo estable";
        }else{
            return "Saldo alto";
        }
    }
    
    // Metodo mostrar información de la cuenta
    public String obtenerInformacion(){
        return """
               ========== CUENTA BANCARIA ==========
               Número de cuenta: %s
               Titular: %s
               Saldo disponible: $%.2f
               Estado: %s
               Clasificación: %s
               Cantidad de movimientos: %d
               """.formatted(numeroCuenta, nombreTitular, saldo, (activa ? "Activa" : "Inactiva" ), obtenerTipoSaldo(), getCantidadMovimientos());
    }
    
    //Metodo desactivar cuenta
    public void desactivarCuenta(){
        
        activa = false;
        
        
        
        
    }
}
