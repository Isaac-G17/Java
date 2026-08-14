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
    
    public void cargarDatosIniciales() {
        productoService.cargarDatosIniciales();
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
    
    
    public int obtenerCantidadProductos(){
        return productoService.obtenerCantidadProductos();
    }
    
    public int obtenerCantidadProductosFisicos() {
        return productoService.obtenerCantidadProductosFisicos();
    }
    
    public int obtenerCantidadProductosDigitales() {
        return productoService.obtenerCantidadProductosDigitales();
    }
    
    public int obtenerCantidadProductosActivos() {
        return productoService.obtenerCantidadProductosActivos();
    }
    
    public int obtenerCantidadProductosInactivos() {
        return productoService.obtenerCantidadProductosInactivos();
    }
    
    public Producto obtenerProductoMayorPrecio() {
        return productoService.obtenerProductoMayorPrecio();
    }
    
    public Producto obtenerProductoMenorPrecio() {
        return productoService.obtenerProductoMenorPrecio();
    }
    
    public double calcularValorTotalInventario(){
        return productoService.calcularValorTotalInventario();
    }
}
