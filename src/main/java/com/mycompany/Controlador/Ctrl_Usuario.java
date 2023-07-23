
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Controlador;

import com.mycompany.vista.InterUsuario;
import com.mycompany.modelo.Usuario;
import com.mycompany.DAO.CRUD_Usuario;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class Ctrl_Usuario implements ActionListener {
    // int idUsuario = 0;

    CRUD_Usuario crudUsu;
    Usuario modUsu;
    InterUsuario inusu;

    //InterGestionarUsuario geinusu;
    public Ctrl_Usuario(InterUsuario usu) {
        inusu = usu;
        inusu.jButton_Guardar.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == inusu.jButton_Guardar) {
            Usuario usuario = new Usuario();
                crudUsu = new CRUD_Usuario();
            String estado = "";
            estado = inusu.jComboBox_estado.getSelectedItem().toString().trim();
            if (inusu.txt_nombre.getText().isEmpty() || inusu.txt_apellido.getText().isEmpty() || inusu.txt_usuario.getText().isEmpty()
                    || inusu.txt_password.getText().isEmpty() || inusu.txt_telefono.getText().isEmpty() || inusu.txt_correo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Completa todos los campos");

            } else {

                //validamos si el usuario ya está registrado
//                Usuario usuario = new Usuario();
//                crudUsu = new CRUD_Usuario();
                if (!crudUsu.existeUsuario(inusu.txt_usuario.getText().trim())) {
                    if (estado.equalsIgnoreCase("Seleccione Estado:")) {
                        JOptionPane.showMessageDialog(null, "Seleccione Estado");

                    } else {
                        try {
                            usuario.setNombre(inusu.txt_nombre.getText().trim());
                            usuario.setApellido(inusu.txt_apellido.getText().trim());
                            usuario.setUsuario(inusu.txt_usuario.getText().trim());
                            usuario.setPassword(inusu.txt_password.getText().trim());
                            usuario.setTelefono(inusu.txt_telefono.getText().trim());
                            usuario.setCorreo(inusu.txt_correo.getText().trim());
                             if (estado.equalsIgnoreCase("Administrador")) {
                                    usuario.setEstado("Administrador");
                                } else if (estado.equalsIgnoreCase("Usuario")) {
                                    usuario.setEstado("Usuario");
                                }
//                    usuario.setEstado(inusu.jComboBox_estado.getText().trim());
                            if (crudUsu.guardar(usuario)) {
                                
                                JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente");
                                 this.inusu.jComboBox_estado.setSelectedItem("Seleccione Estado:");
                            } else {
                                JOptionPane.showMessageDialog(null, "Error al registrar usuario");
                            }
                        } catch (HeadlessException | NumberFormatException ex) {
                            System.out.println("Error en: " + ex);

                        }
                        //enviamos datos del usuario

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El usuario ya está registrado");
                }

            }

        }
        this.Limpiar();

    }

    /**
     * *************************************
     * MÉTODO PARA LIMPIAR CAMPOS *************************************
     */
    void Limpiar() {
        inusu.txt_nombre.setText("");
        inusu.txt_apellido.setText("");
        inusu.txt_password.setText("");
        inusu.txt_usuario.setText("");
        inusu.txt_telefono.setText("");
        inusu.txt_correo.setText("");
    }

}
