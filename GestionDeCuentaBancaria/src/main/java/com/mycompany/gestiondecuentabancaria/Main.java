/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.gestiondecuentabancaria;

import javax.swing.JOptionPane;

/**
 *
 * @author cohorte5
 */
public class Main {

    public static void main(String[] args) {
        
        String numeroCuenta=JOptionPane.showInputDialog("Ingrese el número de la cuenta:");
        
        String nombreTitular=JOptionPane.showInputDialog("Ingrese el nombre del titular:");
        
        double saldoInicial;//=Double.parseDouble(JOptionPane.showInputDialog("Ingrese el saldo inicial:"));

        do {
            saldoInicial = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el saldo inicial:"));
        
            if (saldoInicial < 0) {
                JOptionPane.showMessageDialog(null, 
                    "Error: El saldo no puede ser negativo. Intente de nuevo.", 
                    "Saldo Inválido", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } while (saldoInicial < 0);
        
        
        CuentaBancaria cuenta = new CuentaBancaria(numeroCuenta,nombreTitular,saldoInicial);
        
        int option = 0;
        double cantidad;
        
        do{
        
            String menu = """
                          ======== BANCO RIWI ========
                          
                          1. Consultar cuenta
                          2. Consignar dinero
                          3. Retirar dinero
                          4. Consultar saldo
                          5. Desactivar cuenta
                          6. Salir
                          
                          Seleccione una opción:
                          """;
            
            
           option = Integer.parseInt(JOptionPane.showInputDialog(null, menu));
            
           switch (option) {
                case 1:
                    
                    JOptionPane.showMessageDialog(null, cuenta.obtenerInformacion());
                    break;
                    
                case 2:
                    
                    if(cuenta.isActiva()){
                        JOptionPane.showMessageDialog(null,"La cuenta esta desactivada, no se puede consignar","Aviso",JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    
                    cantidad=Double.parseDouble(JOptionPane.showInputDialog("Ingrese el saldo a consignar: "));

                    boolean resultado = cuenta.consignar(cantidad);

                    if(resultado){
                        JOptionPane.showMessageDialog(null,"Consignación realizada correctamente");
                    }else{
                        JOptionPane.showMessageDialog(null,"No fue posible realizar la consignación."); 
                    }
                    
                    break;
                    
                case 3:
                    
                    
                    
                    if(!cuenta.isActiva()){
                        JOptionPane.showMessageDialog(null,"La cuenta esta desactivada, no se puede retirar","Aviso",JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                        
                    cantidad=Double.parseDouble(JOptionPane.showInputDialog("Ingrese el saldo a retirar: "));

                    boolean resultadoRetirar = cuenta.retirar(cantidad);

                    if(resultadoRetirar){
                        JOptionPane.showMessageDialog(null,"Retiro realizado correctamente.");
                    }else{
                        JOptionPane.showMessageDialog(null,"No fue posible realizar el retiro."); 
                    }
                    
                      
                    break;
                    
                case 4:
                    
                    JOptionPane.showMessageDialog(null, "Su saldo actual es: $%.2f \n Estado del saldo: %s".formatted(cuenta.getSaldo(),cuenta.obtenerTipoSaldo()));
                    break;
                    
                case 5:
                    
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de desactivar la cuenta?","Confirmar",JOptionPane.YES_NO_OPTION);
                    
                    if(respuesta == JOptionPane.YES_OPTION){
                       cuenta.desactivarCuenta();
                       JOptionPane.showMessageDialog(null, "Cuenta desactivada");
                    }else {
                        JOptionPane.showMessageDialog(null, "Operación cancelada. La cuenta sigue activa.");
                    }
                    
                    break;
                    
                case 6:
                    
                    JOptionPane.showMessageDialog(null, "Gracias por usar Banco Riwi. ¡Hasta luego!","Aviso",JOptionPane.PLAIN_MESSAGE);
                    break;
                    
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Digite un número entre 1 y 6.", "Error",JOptionPane.ERROR_MESSAGE);
           }
            
        } while (option != 6);
    }
}
