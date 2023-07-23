/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Controlador;

import com.mycompany.vista.InterGestionarCategoria;
import com.mycompany.modelo.Categoria;
import com.mycompany.DAO.CRUD_Categoria;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;

public class Ctrl_GestionarCategoria implements ActionListener {

    CRUD_Categoria crudCat;
    Categoria modCat;
    InterGestionarCategoria geincat;
    int codigoCategoria = 0;

    public Ctrl_GestionarCategoria(InterGestionarCategoria gecat) {
        geincat = gecat;
        geincat.jButton_buscar.addActionListener(this);
        geincat.jButton_actualizar.addActionListener(this);
        geincat.jButton_eliminar.addActionListener(this);

        crudCat = new CRUD_Categoria();
        tablaListener();
        crudCat.CargarTablaCategorias(geincat.jTable_categorias);
        geincat.setVisible(true);

    }

    void ActualizarFormulario() {
        crudCat = new CRUD_Categoria();
        crudCat.CargarTablaCategorias(geincat.jTable_categorias);
        this.Limpiar();
    }

    void Limpiar() {
        geincat.txt_id.setText("");
        geincat.txt_descripcion.setText("");

    }

    void tablaListener() {
        geincat.jTable_categorias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = geincat.jTable_categorias.getSelectedRow();
                codigoCategoria = (int) geincat.jTable_categorias.getValueAt(fila, 0);

            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == geincat.jButton_buscar) {
            int idCategoria = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el Id de la categoria a buscar"));
            crudCat = new CRUD_Categoria();
            modCat = crudCat.EnviarDatosCategoriaSeleccionada(idCategoria);

            if (modCat == null) {
                JOptionPane.showMessageDialog(null, "El ID " + idCategoria + " no existe en la tabla categoria.");
            } else {
                geincat.txt_id.setText(Integer.toString(modCat.getIdCategoria()));
                geincat.txt_descripcion.setText(modCat.getDescripcion());

            }
        }
        if (e.getSource() == geincat.jButton_actualizar) {
            if (!geincat.txt_descripcion.getText().isEmpty()) {
                Categoria categoria = new Categoria();
                crudCat = new CRUD_Categoria();
                categoria.setIdCategoria(Integer.parseInt(geincat.txt_id.getText()));

                categoria.setDescripcion(geincat.txt_descripcion.getText().trim());
                if (!crudCat.existeCategoria(geincat.txt_descripcion.getText().trim())) {
                    categoria.setIdCategoria(Integer.parseInt(geincat.txt_id.getText()));
                    categoria.setDescripcion(geincat.txt_descripcion.getText().trim());
//                    categoria.setEstado(1);

//                categoria.setIdCategoria(Integer.parseInt(geincat.txt_id.getText()));
                    if (crudCat.actualizar(categoria)) {
                        JOptionPane.showMessageDialog(null, "Categoria actualizada");
                    }
                    geincat.txt_descripcion.setText("");
                    this.ActualizarFormulario();
                } else {
                    JOptionPane.showMessageDialog(null, "La categoría ya existe");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una categoría");
            }

        }
        if (e.getSource() == geincat.jButton_eliminar) {
            if (codigoCategoria == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione una categoria");
            } else {

                int respuesta = JOptionPane.showConfirmDialog(null,
                        "Esta seguro de eliminar a la categoria?", "Confirmación...", JOptionPane.YES_NO_CANCEL_OPTION);
                if (respuesta == 0) {
                    crudCat = new CRUD_Categoria();
                    crudCat.eliminar(codigoCategoria);
                    ActualizarFormulario();
                    JOptionPane.showMessageDialog(null, "¡Categoria eliminada!");
                }

            }

        }
    }

}
