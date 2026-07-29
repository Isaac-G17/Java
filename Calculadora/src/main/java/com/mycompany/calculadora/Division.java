/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.calculadora;

import javax.swing.JOptionPane;

/**
 *
 * @author cohorte5
 */
public class Division {
    
    public int num1;
    public int num2;
    
    public Division(int num1,int num2){
         this.num1=num1;
         this.num2=num2;
    }
    
    public void division(){
        
        int division=this.num1/this.num2;
        
        System.out.println("La division es : "+division);
        
        JOptionPane.showMessageDialog(null, "La division es : "+division);
        
    }
}
