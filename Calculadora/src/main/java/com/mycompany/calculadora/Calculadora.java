/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.calculadora;
import javax.swing.JOptionPane;

/**
 *
 * @author cohorte5
 */
public class Calculadora {

    public static void main(String[] args) {
        
        int num1=Integer.parseInt(JOptionPane.showInputDialog("Inserte el numero 1"));
        int num2=Integer.parseInt(JOptionPane.showInputDialog("Inserte el numero 2"));

        
        
        Sumar objetoSumar=new Sumar(num1,num2);
        objetoSumar.sumar();
        
        Restar objetoRestar=new Restar(num1,num2);
        objetoRestar.restar();
        
        Multiplicar objetoMultiplicar=new Multiplicar(num1,num2);
        objetoMultiplicar.multiplicar();
        
        Division objetoDividir=new Division(num1,num2);
        objetoDividir.division();
    }
}
