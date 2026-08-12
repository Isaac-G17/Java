/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadeproductos.controller;

import com.mycompany.sistemadeproductos.model.Producto;
import com.mycompany.sistemadeproductos.service.ProductoService;
import java.util.ArrayList;

/**
 *
 * @author cohorte5
 */
public class ProductoController {

    private ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Registrar producto
    public void registrarProducto(Producto producto) {
        productoService.registrarProducto(producto);
    }

    // Consultar productos
    public ArrayList<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    // Buscar producto
    public Producto buscarProducto(String codigo) {
        return productoService.buscarProducto(codigo);
    }

    // Actualizar producto
    public void actualizarProducto(Producto producto) {
        productoService.actualizarProducto(producto);
    }

    // Eliminar producto
    public void eliminarProducto(String codigo) {
        productoService.eliminarProducto(codigo);
    }

    public String consultarEstadisticas() {
        int cantidadProductos = productoService.obtenerCantidadProductos();
        double valorInventario = productoService.calcularValorTotalInventario();

        return """
                ===== ESTADÍSTICAS DEL INVENTARIO =====

                Cantidad de productos: %d
                Valor total del inventario: $%.2f
                """.formatted(cantidadProductos, valorInventario);

    }
}
