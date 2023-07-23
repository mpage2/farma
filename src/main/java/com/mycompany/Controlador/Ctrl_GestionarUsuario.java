/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Controlador;

import com.mycompany.vista.InterGestionarUsuario;
import com.mycompany.modelo.Usuario;
import com.mycompany.DAO.CRUD_Usuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import java.awt.event.MouseAdapter;

public class Ctrl_GestionarUsuario implements ActionListener {

    CRUD_Usuario crudUsu;
    Usuario modUsu;
    InterGestionarUsuario geinusu;

    int codigoUsuario = 0;
    int idUsuario;

    public Ctrl_GestionarUsuario(InterGestionarUsuario geusu) {
        geinusu = geusu;
        geinusu.jButton_buscar.addActionListener(this);
        geinusu.jButton_actualizar.addActionListener(this);
        geinusu.jButton_eliminar.addActionListener(this);

        crudUsu = new CRUD_Usuario();
        tablaListener();
        crudUsu.CargarTablaUsuarios(geinusu.jTable_usuarios);
        geinusu.setVisible(true);

    }

    void ActualizarFormulario() {
        crudUsu = new CRUD_Usuario();
        crudUsu.CargarTablaUsuarios(geinusu.jTable_usuarios);
        this.Limpiar();
    }

    void Limpiar() {
        geinusu.txt_id.setText("");
        geinusu.txt_nombre.setText("");
        geinusu.txt_apellido.setText("");
        geinusu.txt_password.setText("");
        geinusu.txt_usuario.setText("");
        geinusu.txt_telefono.setText("");
        geinusu.txt_correo.setText("");
    }

    void tablaListener() {
        geinusu.jTable_usuarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = geinusu.jTable_usuarios.getSelectedRow();
                codigoUsuario = (int) geinusu.jTable_usuarios.getValueAt(fila, 0);
                idUsuario = (int) geinusu.jTable_usuarios.getValueAt(fila, 0);
                
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == geinusu.jButton_buscar) {
            int idUsuario = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el Id del usuario a buscar"));
            crudUsu = new CRUD_Usuario();
            modUsu = crudUsu.EnviarDatosUsuarioSeleccionado(idUsuario);

            if (modUsu == null) {
                JOptionPane.showMessageDialog(null, "El ID " + idUsuario + " no existe en la tabla usuario.");
            } else {
                geinusu.txt_id.setText(Integer.toString(modUsu.getIdUsuario()));
                geinusu.txt_nombre.setText(modUsu.getNombre());
                geinusu.txt_apellido.setText(modUsu.getApellido());
                geinusu.txt_usuario.setText(modUsu.getUsuario());
                geinusu.txt_password.setText(modUsu.getPassword());
                geinusu.txt_telefono.setText(modUsu.getTelefono());
                geinusu.txt_correo.setText(modUsu.getCorreo());

            }
        }

        if (e.getSource() == geinusu.jButton_actualizar) {

            Usuario usuario = new Usuario();
            crudUsu = new CRUD_Usuario();
            if (geinusu.txt_nombre.getText().isEmpty() || geinusu.txt_apellido.getText().isEmpty() || geinusu.txt_usuario.getText().isEmpty()
                    || geinusu.txt_password.getText().isEmpty() || geinusu.txt_telefono.getText().isEmpty() || geinusu.txt_correo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Complete los campos");
            } else {
                usuario.setNombre(geinusu.txt_nombre.getText().trim());
                usuario.setApellido(geinusu.txt_apellido.getText().trim());
                usuario.setUsuario(geinusu.txt_usuario.getText().trim());
                usuario.setPassword(geinusu.txt_password.getText().trim());
                usuario.setTelefono(geinusu.txt_telefono.getText().trim());
                usuario.setCorreo(geinusu.txt_correo.getText().trim());
//                usuario.setEstado(1);

                usuario.setIdUsuario(Integer.parseInt(geinusu.txt_id.getText()));
                if (!crudUsu.existeUsuario(geinusu.txt_id.getText().trim())) {
                    usuario.setIdUsuario(Integer.parseInt(geinusu.txt_id.getText()));
//                    usuario.setEstado(1);
                    if (crudUsu.actualizar(usuario)) {
                        JOptionPane.showMessageDialog(null, "Actualización exitosa");
                        this.Limpiar();
                        ActualizarFormulario();
                        //idUsuario = 0;

                    } else {
                        JOptionPane.showMessageDialog(null, "Error al actualizar");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El usuario ya existe");

                }
            }

        }
        if (e.getSource() == geinusu.jButton_eliminar) {

            if (codigoUsuario == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione un usuario");
            } else {

                int respuesta = JOptionPane.showConfirmDialog(null,
                        "Esta seguro de eliminar al usuario?", "Confirmación...", JOptionPane.YES_NO_CANCEL_OPTION);
                if (respuesta == 0) {
                    crudUsu = new CRUD_Usuario();
                    crudUsu.eliminar(codigoUsuario);
                    ActualizarFormulario();
                    JOptionPane.showMessageDialog(null, "¡Usuario eliminado!");
                }
            }
        }
    }
}
