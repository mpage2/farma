/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Controlador;

import com.mycompany.DAO.CRUD_Producto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import com.mycompany.modelo.*;
import com.mycompany.vista.*;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.util.List;

public class Ctrl_ActualizarStock implements ActionListener {

    CRUD_Producto crudPro;
    Producto modPro;
    InterActualizarStock actstock;
    int idProducto = 0;

    public Ctrl_ActualizarStock(InterActualizarStock act) {
        actstock = act;
        actstock.jButton1.addActionListener(this);
        actstock.jComboBox_producto.addActionListener(this);
        crudPro = new CRUD_Producto();
//        actstock.jButton1.addActionListener(this);
        List<String> Producto = crudPro.CargarComboProductos();
        for (String producto : Producto) {
            actstock.jComboBox_producto.addItem(producto);

        }
        actstock.setVisible(true);

    }

    void ActualizarFormulario() {
        crudPro = new CRUD_Producto();
//        crudPro.CargarTablaProducto(actstock.);
        List<String> Producto = crudPro.CargarComboProductos();
        for (String producto : Producto) {
            actstock.jComboBox_producto.addItem(producto);

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource()==actstock.jComboBox_producto){
            String nombreProducto = actstock.jComboBox_producto.getSelectedItem().toString();
            if(actstock.jComboBox_producto.getSelectedIndex()!=0){
                modPro = crudPro.obtenerDatosProductoPorNombre(nombreProducto);
                actstock.txt_cantidad_actual.setText(String.valueOf(modPro.getCantidad()));
            }
        }
        
        if (e.getSource() == actstock.txt_cantidad_actual) {
            
        }
        if (e.getSource() == actstock.jButton1) {
            //validamos seleccion de producto
            if (!actstock.jComboBox_producto.getSelectedItem().equals("Seleccione producto:")) {
                //validamos campos vacios
                if (!actstock.txt_cantidad_nueva.getText().isEmpty()) {
                    //validados que el usuario no ingrese otros caracteres no numericos
                    boolean validacion = crudPro.validar(actstock.txt_cantidad_nueva.getText().trim());
                    if (validacion == true) {
                        //validamos que la cantidad sea mayor a 0
                        if (Integer.parseInt(actstock.txt_cantidad_nueva.getText()) > 0) {

                            Producto nuevoProducto = modPro;
                            crudPro = new CRUD_Producto();
                            int stockActual = Integer.parseInt(actstock.txt_cantidad_actual.getText().trim());
                            int stockNuevo = Integer.parseInt(actstock.txt_cantidad_nueva.getText().trim());

                            stockNuevo = stockActual + stockNuevo;
                            nuevoProducto.setCantidad(stockNuevo);
                            if (crudPro.actualizarStock(nuevoProducto)) {
                                JOptionPane.showMessageDialog(null, "Stock actualizado");
                                actstock.jComboBox_producto.setSelectedItem("Seleccione producto:");
                                actstock.txt_cantidad_actual.setText("");
                                actstock.txt_cantidad_nueva.setText("");
                                this.ActualizarFormulario();
                            } else {
                                JOptionPane.showMessageDialog(null, "Error al actualizar");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "la cantidad no puede ser 0 ni negativa");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "En la cantidad no se se permiten caracteres no numéricos.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese la nueva cantidad.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un producto.");
            }
        }
    }
}
