/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Controlador;

import com.mycompany.vista.InterGestionarVenta;
import com.mycompany.modelo.Cliente;
import com.mycompany.modelo.CabeceraVenta;
import com.mycompany.DAO.CRUD_Producto;
import com.mycompany.DAO.CRUD_RegistrarVenta;
import com.mycompany.DAO.CRUD_Cliente;
import com.mycompany.bd.Conexion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author huama
 */
public class Ctrl_GestionarVenta implements ActionListener {

    private int idCliente = 0;
    int idVenta;
    CRUD_RegistrarVenta crudVen;
    CRUD_Producto crudPro;
    DefaultTableModel model;

//    DetalleVenta modDel;
    Cliente modCli;

    InterGestionarVenta geinven;
    CRUD_Cliente crudCli;

    public Ctrl_GestionarVenta(InterGestionarVenta geven) {
        geinven = geven;
        geinven.jButton_buscar.addActionListener(this);
        geinven.jButton_actualizar.addActionListener(this);

        crudVen = new CRUD_RegistrarVenta();
        crudCli = new CRUD_Cliente();

        String titulos[] = {"N°", "Cliente", "Total Pagar", "Fecha Venta", "Estado"};
        model = new DefaultTableModel(null, titulos);
        geinven.jTable_ventas.setModel(model);

        cargarTabla();

//        geinven.setVisible(true);
//        crudVen = new CRUD_RegistrarVenta();
        List<String> Clientes = crudVen.CargarComboCliente();
        for (String cliente : Clientes) {
            geinven.jComboBox_cliente.addItem(cliente);
        }
        geinven.setVisible(true);

    }

    void ActualizarFormulario() {
        crudVen = new CRUD_RegistrarVenta();
        cargarTabla();
        List<String> Clientes = crudCli.CargarComboCliente();
        for (String cliente : Clientes) {
            geinven.jComboBox_cliente.addItem(cliente);
        }
    }

    void cargarTabla() {
        //        tablaListener();
        model.setRowCount(0);
        List<CabeceraVenta> datosTabla = crudVen.CargarTablaVentas();
        int nro = 0;
        for (CabeceraVenta datos : datosTabla) {
            nro++;
            String estado = datos.getEstado() == 0 ? "Inactivo" : "Activo";
            model.addRow(new Object[]{
                nro,
                datos.getNombresCliente(),
                datos.getValorPagar(),
                datos.getFechaVenta(),
                estado
            });
        }
    }

    /*
    * ***********************************************************
    *    METODO PARA LIMPIAR
    * ***********************************************************
     */
    void Limpiar() {
        geinven.txt_total_pagar.setText("");
        geinven.txt_fecha.setText("");
        geinven.jComboBox_cliente.setSelectedIndex(0);
        geinven.jComboBox_estado.setSelectedItem("Seleccione Estado");
        this.idCliente = 0;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == geinven.jButton_buscar) {
            int idVenta = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el Id de la venta a buscar"));
            crudVen = new CRUD_RegistrarVenta();
            CabeceraVenta cabeceraVenta = new CabeceraVenta();
            cabeceraVenta = crudVen.EnviarDatosVentaSeleccionada(idVenta);

            if (cabeceraVenta == null) {
                JOptionPane.showMessageDialog(null, "El ID " + idVenta + " no existe en la tabla producto.");
            } else {
                geinven.txt_id.setText(Integer.toString(cabeceraVenta.getIdCabeceraventa()));
                geinven.txt_total_pagar.setText(Double.toString(cabeceraVenta.getValorPagar()));
                geinven.jComboBox_cliente.setSelectedItem(cabeceraVenta.getNombresCliente());

                geinven.txt_fecha.setText(cabeceraVenta.getFechaVenta());
                switch (cabeceraVenta.getEstado()) {
                    case 1:
                        geinven.jComboBox_estado.setSelectedIndex(1);
                        break;
                    default:
                        geinven.jComboBox_estado.setSelectedIndex(2);
                }
            }
        }
        if (e.getSource() == geinven.jButton_actualizar) {
            CabeceraVenta cabeceraVenta = new CabeceraVenta();
            crudVen = new CRUD_RegistrarVenta();
            cabeceraVenta.setIdCabeceraventa(Integer.parseInt(geinven.txt_id.getText()));
            String cliente, estado;
            cliente = geinven.jComboBox_cliente.getSelectedItem().toString().trim();
            estado = geinven.jComboBox_estado.getSelectedItem().toString().trim();
            //obtener cliente
            int clientId = crudVen.obtenerIdClientePorCliente(cliente);

            //actualizar datos
            if (!cliente.equalsIgnoreCase("Seleccione cliente:")) {
                cabeceraVenta.setIdCliente(clientId);
                if (estado.equalsIgnoreCase("Activo")) {
                    cabeceraVenta.setEstado(1);
                } else {
                    cabeceraVenta.setEstado(0);
                }
                if (crudVen.actualizar(cabeceraVenta)) {
                    JOptionPane.showMessageDialog(null, "Registro Actualizado");
                    cargarTabla();
                    this.Limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione registro para actualiza datos");
            }

        }
    }
}
