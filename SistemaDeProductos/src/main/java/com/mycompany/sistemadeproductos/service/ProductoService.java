/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadeproductos.service;

import com.mycompany.sistemadeproductos.model.Producto;
import com.mycompany.sistemadeproductos.model.ProductoDigital;
import com.mycompany.sistemadeproductos.model.ProductoFisico;
import com.mycompany.sistemadeproductos.repository.ProductoRepository;
import java.util.ArrayList;

/**
 *
 * @author cohorte5
 */
public class ProductoService {

    private ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    private void validarProducto(Producto producto) {
// Validaciones generales

        if (producto.getCodigo().isBlank()) {
            throw new IllegalArgumentException("El código no puede estar vacío");
        }

        if (producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (producto.getPrecioBase() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero");
        }

        if (producto.getCantidadDisponible() < 0) {
            throw new IllegalArgumentException("La cantidad disponible no puede ser negativa");
        }

        // Validaciones ProductoFisico
        if (producto instanceof ProductoFisico productoFisico) {

            if (productoFisico.getPeso() <= 0) {
                throw new IllegalArgumentException("El peso debe ser mayor que cero");
            }

            if (productoFisico.getCostoEnvio() < 0) {
                throw new IllegalArgumentException("El costo de envío no puede ser negativo");
            }
        }

        // Validaciones ProductoDigital
        if (producto instanceof ProductoDigital productoDigital) {

            if (productoDigital.getTamañoArchivo() <= 0) {
                throw new IllegalArgumentException("El tamaño del archivo debe ser mayor que cero");
            }

            if (productoDigital.getFormato().isBlank()) {
                throw new IllegalArgumentException("El formato no puede estar vacío");
            }
        }

    }

    public void registrarProducto(Producto producto) {

        validarProducto(producto);

        if (productoRepository.existeCodigo(producto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un producto con ese código");
        }

        productoRepository.guardar(producto);

    }

    public ArrayList listarProductos() {
        return productoRepository.listarTodos();
    }

    public Producto buscarProducto(String codigo) {

        Producto producto = productoRepository.buscarPorCodigo(codigo);

        if (producto == null) {
            throw new IllegalArgumentException("No existe un producto con el código: " + codigo);
        }

        return producto;

    }

    public void actualizarProducto(Producto producto) {

        Producto productoExistente = productoRepository.buscarPorCodigo(producto.getCodigo());

        if (productoExistente == null) {
            throw new IllegalArgumentException("No existe un producto con el código: " + producto.getCodigo());
        }

        validarProducto(producto);

        productoRepository.actualizar(producto);

    }

    public void eliminarProducto(String codigo) {

        Producto producto = productoRepository.buscarPorCodigo(codigo);

        if (producto == null) {
            throw new IllegalArgumentException("No existe un producto con el código: " + codigo);
        }

        productoRepository.eliminarPorCodigo(codigo);

    }

    public int obtenerCantidadProductos() {
        return productoRepository.obtenerCantidad();
    }

    public double calcularValorTotalInventario() {

        double total = 0;

        for (Producto producto : productoRepository.listarTodos()) {
            total += producto.calcularValorInventario();
        }

        return total;

    }
}
