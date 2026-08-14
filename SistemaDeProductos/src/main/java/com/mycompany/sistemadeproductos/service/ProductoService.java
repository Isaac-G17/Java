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

    public void cargarDatosIniciales() {

        ProductoFisico fisico1 = new ProductoFisico(
                "F001",
                "Computador portátil",
                3500000,
                4,
                2.5,
                35000
        );

        ProductoFisico fisico2 = new ProductoFisico(
                "F002",
                "Escritorio",
                800000,
                3,
                25,
                90000
        );

        ProductoDigital digital1 = new ProductoDigital(
                "D001",
                "Curso de Java",
                120000,
                50,
                3.5,
                "MP4"
        );

        ProductoDigital digital2 = new ProductoDigital(
                "D002",
                "Licencia de software",
                450000,
                15,
                7,
                "ZIP"
        );

        productoRepository.guardar(fisico1);
        productoRepository.guardar(fisico2);
        productoRepository.guardar(digital1);
        productoRepository.guardar(digital2);
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

    public int obtenerCantidadProductosFisicos() {

        int cantidad = 0;

        for (Producto producto : productoRepository.listarTodos()) {

            if (producto instanceof ProductoFisico) {
                cantidad++;
            }
        }

        return cantidad;
    }

    public int obtenerCantidadProductosDigitales() {

        int cantidad = 0;

        for (Producto producto : productoRepository.listarTodos()) {

            if (producto instanceof ProductoDigital) {
                cantidad++;
            }
        }

        return cantidad;
    }

    public int obtenerCantidadProductosActivos() {

        int cantidad = 0;

        for (Producto producto : productoRepository.listarTodos()) {

            if (producto.isActivo()) {
                cantidad++;
            }
        }

        return cantidad;
    }

    public int obtenerCantidadProductosInactivos() {

        int cantidad = 0;

        for (Producto producto : productoRepository.listarTodos()) {

            if (!producto.isActivo()) {
                cantidad++;
            }
        }

        return cantidad;
    }

    public Producto obtenerProductoMayorPrecio() {

        Producto mayor = null;

        for (Producto producto : productoRepository.listarTodos()) {

            if (mayor == null
                    || producto.calcularPrecioFinal() > mayor.calcularPrecioFinal()) {

                mayor = producto;
            }
        }

        return mayor;
    }

    public Producto obtenerProductoMenorPrecio() {

        Producto menor = null;

        for (Producto producto : productoRepository.listarTodos()) {

            if (menor == null
                    || producto.calcularPrecioFinal() < menor.calcularPrecioFinal()) {

                menor = producto;
            }
        }

        return menor;
    }

    public double calcularValorTotalInventario() {

        double total = 0;

        for (Producto producto : productoRepository.listarTodos()) {
            total += producto.calcularValorInventario();
        }

        return total;

    }
}
