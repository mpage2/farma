/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Controlador;

import com.mycompany.vista.FrmMenu;
import com.mycompany.vista.FrmRecuperar;
import com.mycompany.vista.FrmLogin;
import com.mycompany.modelo.Usuario;
import com.mycompany.DAO.CRUD_Usuario;
import com.mycompany.Principal.Main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class Ctrl_Inicio implements ActionListener {

    CRUD_Usuario crudUsu;

    FrmLogin vista;

    public Ctrl_Inicio(FrmLogin ini) {
        vista = ini;
        vista.jButton_IniciarSesion.addActionListener(this);
        vista.jButton_RecuperarContraseña.addActionListener(this);
        System.out.print(vista.jButton_IniciarSesion.getActionListeners().length);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.jButton_IniciarSesion) {
            if (!vista.txt_usuario.getText().isEmpty() && !vista.txt_password.getText().isEmpty()) {

                crudUsu = new CRUD_Usuario();
                Usuario usuario = new Usuario();
                usuario.setUsuario(vista.txt_usuario.getText().trim());
                usuario.setPassword(vista.txt_password.getText().trim());
                int idUsuario = crudUsu.loginUser(usuario);
                if (idUsuario!=-1) {
//            JOptionPane.showMessageDialog(null, "Bienvenido");

                    Main.menu = new FrmMenu();
                    Main.menu.setVisible(true);

                    Main.cmenu = new Ctrl_Menu(Main.menu, idUsuario);
                    vista.dispose();

                } else {
                    JOptionPane.showMessageDialog(null, "Usuario y/o Clave incorrecto");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Campos vacíos");
            }
        }
        if (e.getSource() == vista.jButton_RecuperarContraseña) {
            Main.rec = new FrmRecuperar();
            Main.rec.setVisible(true);
            Main.crec = new Ctrl_Recuperar(Main.rec);
            vista.dispose();
        }
    }
}
