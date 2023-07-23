/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Controlador;

import com.mycompany.vista.FrmMenu;
import com.mycompany.vista.InterGestionarCategoria;
import com.mycompany.vista.InterActualizarStock;
import com.mycompany.vista.InterGestionarVenta;
import com.mycompany.vista.InterCategoria;
import com.mycompany.vista.InterFacturacion;
import com.mycompany.vista.InterGestionarCliente;
import com.mycompany.vista.InterProducto;
import com.mycompany.vista.InterCliente;
import com.mycompany.vista.InterGestionarProducto;
import com.mycompany.vista.InterGestionarUsuario;
import com.mycompany.vista.InterUsuario;
import com.mycompany.Principal.Main;
import com.mycompany.DAO.CRUD_Usuario;
import com.mycompany.DAO.Reportes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mycompany.Controlador.*;
import javax.swing.JOptionPane;

/**
 *
 * @author huama
 */
public class Ctrl_Menu implements ActionListener {

    FrmMenu vista;
    private int idUsuario;
    private boolean esAdmin;

    public Ctrl_Menu(FrmMenu menu, int idUsuario) {
        vista = menu;
        vista.jMenuItem_nuevo_usuario.addActionListener(this);
        vista.jMenuItem_gestionar_usuario.addActionListener(this);
        vista.jMenuItem3_nuevo_producto.addActionListener(this);
        vista.jMenuItem_gestionar_producto.addActionListener(this);
        vista.jMenuItem_actualizar_stock.addActionListener(this);
        vista.jMenuItem_nuevo_cliente.addActionListener(this);
        vista.jMenuItem_gestionar_cliente.addActionListener(this);
        vista.jMenuItem_nueva_categoria.addActionListener(this);
        vista.jMenuItem_gestionar_categoria.addActionListener(this);
        vista.jMenuItem_nueva_venta.addActionListener(this);
        vista.jMenuItem_gestionar_venta.addActionListener(this);
        vista.jMenuItem_reportes_clientes.addActionListener(this);
        vista.jMenuItem_reportes_categorias.addActionListener(this);
        vista.jMenuItem_reportes_productos.addActionListener(this);
        vista.jMenuItem_reportes_ventas.addActionListener(this);
        vista.jMenu1.addActionListener(this);
        this.idUsuario = idUsuario;
        verificarRol();
    }
    
    private void verificarRol(){
        CRUD_Usuario crudUsuario = new CRUD_Usuario();
        System.out.println("id: "+idUsuario);
        this.esAdmin = crudUsuario.esUsuarioAdmin(idUsuario);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.jMenuItem_nuevo_usuario) {
            Main.inusu = new InterUsuario();
            Main.cinusu = new Ctrl_Usuario(Main.inusu);
            vista.JDesktopPane_menu.add(Main.inusu);
            Main.inusu.setVisible(true);
        }
        if (e.getSource() == vista.jMenuItem_gestionar_usuario) {
            Main.geinusu = new InterGestionarUsuario();
            Main.cgeinusu = new Ctrl_GestionarUsuario(Main.geinusu);
            vista.JDesktopPane_menu.add(Main.geinusu);
            Main.geinusu.setVisible(true);
        }
        if (e.getSource() == vista.jMenuItem3_nuevo_producto) {
            Main.inpro = new InterProducto();
            Main.cinpro = new Ctrl_Producto(Main.inpro);
            vista.JDesktopPane_menu.add(Main.inpro);
            Main.inpro.setVisible(true);
        }
        if (e.getSource() == vista.jMenuItem_actualizar_stock) {
            if(esAdmin){
                Main.actstock = new InterActualizarStock();
            Main.cactstock = new Ctrl_ActualizarStock(Main.actstock);
            vista.JDesktopPane_menu.add(Main.actstock);
            Main.actstock.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "No puedes acceder a esta vista");
            }
        }
        if (e.getSource() == vista.jMenuItem_gestionar_producto) {
            Main.geinpro = new InterGestionarProducto();
            Main.cgeinpro = new Ctrl_GestionarProducto(Main.geinpro);
            vista.JDesktopPane_menu.add(Main.geinpro);
            Main.geinpro.setVisible(true);
        }

        if (e.getSource() == vista.jMenuItem_nuevo_cliente) {
            Main.incli = new InterCliente();
            Main.cincli = new Ctrl_Cliente(Main.incli);
            vista.JDesktopPane_menu.add(Main.incli);
            Main.incli.setVisible(true);
        }
        if (e.getSource() == vista.jMenuItem_gestionar_cliente) {
            Main.geincli = new InterGestionarCliente();
            Main.cgeincli = new Ctrl_GestionarCliente(Main.geincli);
            vista.JDesktopPane_menu.add(Main.geincli);
            Main.geincli.setVisible(true);
        }

        if (e.getSource() == vista.jMenuItem_nueva_categoria) {
            Main.incat = new InterCategoria();
            Main.cincat = new Ctrl_Categoria(Main.incat);
            vista.JDesktopPane_menu.add(Main.incat);
            Main.incat.setVisible(true);
        }
        if (e.getSource() == vista.jMenuItem_gestionar_categoria) {
            Main.geincat = new InterGestionarCategoria();
            Main.cgeincat = new Ctrl_GestionarCategoria(Main.geincat);
            vista.JDesktopPane_menu.add(Main.geincat);
            Main.geincat.setVisible(true);
        }
        if(e.getSource() == vista.jMenuItem_nueva_venta){
                Main.inven = new InterFacturacion();
                Main.cinven = new Ctrl_RegistrarVenta(Main.inven);
                vista.JDesktopPane_menu.add(Main.inven);
                Main.inven.setVisible(true);
            }
        if (e.getSource() == vista.jMenuItem_gestionar_venta) {
            Main.geinven = new InterGestionarVenta();
            Main.cgeinven = new Ctrl_GestionarVenta(Main.geinven);
            vista.JDesktopPane_menu.add(Main.geinven);
            Main.geinven.setVisible(true);
        }
        if (e.getSource() == vista.jMenuItem_reportes_clientes) {
            Reportes reportes = new Reportes();
            reportes.ReportesClientes();
        }
        if (e.getSource() == vista.jMenuItem_reportes_categorias) {
            Reportes reportes = new Reportes();
            reportes.ReportesCategorias();
        }
        if (e.getSource() == vista.jMenuItem_reportes_productos) {
            Reportes reportes = new Reportes();
            reportes.ReportesProductos();
        }
        if (e.getSource() == vista.jMenuItem_reportes_ventas) {
            Reportes reportes = new Reportes();
            reportes.ReportesVentas();
        }

    }
}
