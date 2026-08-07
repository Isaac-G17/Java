/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadeproductos.repository;

import com.mycompany.sistemadeproductos.model.Producto;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author cohorte5
 */
public class ProductoRepository {

    // Colección donde se almacenan los productos.
    private ArrayList<Producto> productos;

    // Constructor para inicializar la lista
    public ProductoRepository() {
        this.productos = new ArrayList<>();

    }

    // Método guardar producto
    public void guardar(Producto producto) {

        productos.add(producto);
    }

    public ArrayList<Producto> listarTodos() {
        return productos;
    }

    public Producto buscarPorCodigo(String codigo) {

        for (Producto producto : productos) {

            if (producto.getCodigo().equals(codigo)) {
                return producto;

            }

        }

        return null;
    }

    public boolean eliminarPorCodigo(String codigo) {

        Producto producto = buscarPorCodigo(codigo);

        if (producto != null) {
            productos.remove(producto);
            return true;
        }

        return false;
    }

    public boolean existeCodigo(String codigo) {

        Producto producto = buscarPorCodigo(codigo);

        if (producto != null) {
            return true;
        }

        return false;

    }

}
