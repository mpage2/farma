/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Controlador;

import com.mycompany.vista.InterProducto;
import com.mycompany.modelo.Producto;
import com.mycompany.DAO.CRUD_Producto;
import com.mycompany.Principal.Main;
import static com.mycompany.Principal.Main.geincat;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

public class Ctrl_Producto implements ActionListener {

    CRUD_Producto crudPro;
    Producto modPro;
    InterProducto inpro;
//    int obtenerIdCategoriaCombo = 0;

    //InterGestionarUsuario geinusu;
    public Ctrl_Producto(InterProducto pro) {
        inpro = pro;
        inpro.jButton_Guardar.addActionListener(this);
        crudPro = new CRUD_Producto();
        List<String> Categorias = crudPro.CargarComboCategorias();
        for (String categoria : Categorias) {
            inpro.jComboBox_categoria.addItem(categoria);

        }
        inpro.setVisible(true);
    }

    void Limpiar() {
        inpro.txt_nombre.setText("");
        inpro.txt_cantidad.setText("");
        inpro.txt_precio.setText("");
        inpro.txt_descripcion.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == inpro.jButton_Guardar) {
            Producto producto = new Producto();
            crudPro = new CRUD_Producto();
            String igv = "";
            String categoria = "";
            igv = inpro.jComboBox_igv.getSelectedItem().toString().trim();
            categoria = inpro.jComboBox_categoria.getSelectedItem().toString().trim();

            if (inpro.txt_nombre.getText().equals("") || inpro.txt_cantidad.getText().equals("") || inpro.txt_precio.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Complete todos los campos");
                inpro.txt_nombre.setBackground(Color.red);
                inpro.txt_cantidad.setBackground(Color.red);
                inpro.txt_precio.setBackground(Color.red);

            } else {
                //producto ya existe en bd
                if (!crudPro.existeProducto(inpro.txt_nombre.getText().trim())) {

                    if (igv.equalsIgnoreCase("Seleccione IGV:")) {
                        JOptionPane.showMessageDialog(null, "Seleccione IGV");

                    } else {

                        if (categoria.equalsIgnoreCase("Seleccione Categoria:")) {

                            JOptionPane.showMessageDialog(null, "Seleccione categoria");

                        } else {
                            try {

                                producto.setNombre(inpro.txt_nombre.getText().trim());
                                producto.setCantidad(Integer.parseInt(inpro.txt_cantidad.getText().trim()));
                                String precioTXT = "";
                                double Precio = 0.0;

                                precioTXT = inpro.txt_precio.getText().trim();
                                boolean aux = false;
                                /*
                            * Si el usuario ingresa (,) como punto decimal, lo transformamos a punto.
                                 */
                                for (int i = 0; i < precioTXT.length(); i++) {
                                    if (precioTXT.charAt(i) == ',') {
                                        String precioNuevo = precioTXT.replace(",", ".");
                                        Precio = Double.parseDouble(precioNuevo);
                                        aux = true;
                                    }
                                }
                                //evaluar 
                                if (aux == true) {
                                    producto.setPrecio(Precio);
                                } else {
                                    Precio = Double.parseDouble(precioTXT);
                                    producto.setPrecio(Precio);
                                }

                                producto.setDescripcion(inpro.txt_descripcion.getText().trim());

                                if (igv.equalsIgnoreCase("No")) {
                                    producto.setPorcentajeIgv(0);
                                } else if (igv.equalsIgnoreCase("18%")) {
                                    producto.setPorcentajeIgv(18);
                                }

                                //cargar método con idCategoria
                                int categoriaId = crudPro.IdCategoria(inpro.jComboBox_categoria.getSelectedItem().toString());
                                producto.setIdCategoria(categoriaId);
//                                producto.setEstado(1);

                                if (crudPro.guardar(producto)) {
                                    JOptionPane.showMessageDialog(null, "Registro guardado");
                                    inpro.txt_nombre.setBackground(Color.green);
                                    inpro.txt_cantidad.setBackground(Color.green);
                                    inpro.txt_precio.setBackground(Color.green);
                                    inpro.txt_descripcion.setBackground(Color.green);

                                    this.inpro.jComboBox_igv.setSelectedItem("Seleccione IGV:");
                                    this.Limpiar();

                                } else {
                                    JOptionPane.showMessageDialog(null, "Error al guardar");
                                }

                            } catch (HeadlessException | NumberFormatException ex) {
                                System.out.println("Error en: " + ex);
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El producto ya existe en la base de datos");
                }
            }
        }

    }

}
