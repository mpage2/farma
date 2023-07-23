/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Controlador;

import com.mycompany.vista.InterGestionarCliente;
import com.mycompany.modelo.Cliente;
import com.mycompany.DAO.CRUD_Cliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Desktop;

import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Ctrl_GestionarCliente implements ActionListener {

    CRUD_Cliente crudCli;
    Cliente modCli;
    InterGestionarCliente geincli;

    int codigoCliente = 0;

    public Ctrl_GestionarCliente(InterGestionarCliente gecli) {
        geincli = gecli;
        geincli.jButton_buscar.addActionListener(this);
        geincli.jButton_actualizar.addActionListener(this);
        geincli.jButton_eliminar.addActionListener(this);
        geincli.jButton_excel.addActionListener(this);

        crudCli = new CRUD_Cliente();
        tablaListener();
        crudCli.CargarTablaCliente(geincli.jTable_clientes);
        geincli.setVisible(true);

    }

    void ActualizarFormulario() {
        crudCli = new CRUD_Cliente();
        crudCli.CargarTablaCliente(geincli.jTable_clientes);
        this.Limpiar();
    }

    void Limpiar() {
        geincli.txt_id.setText("");
        geincli.txt_nombre.setText("");
        geincli.txt_apellido.setText("");
        geincli.txt_dni.setText("");
        geincli.txt_telefono.setText("");
        geincli.txt_direccion.setText("");
    }

    void tablaListener() {
        geincli.jTable_clientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = geincli.jTable_clientes.getSelectedRow();
                codigoCliente = (int) geincli.jTable_clientes.getValueAt(fila, 0);

            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == geincli.jButton_buscar) {
            int idCliente = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el Id del cliente a buscar"));
            crudCli = new CRUD_Cliente();
            modCli = crudCli.EnviarDatosClienteSeleccionado(idCliente);

            if (modCli == null) {
                JOptionPane.showMessageDialog(null, "El ID " + idCliente + " no existe en la tabla cliente.");
            } else {
                geincli.txt_id.setText(Integer.toString(modCli.getIdCliente()));
                geincli.txt_nombre.setText(modCli.getNombre());
                geincli.txt_apellido.setText(modCli.getApellido());
                geincli.txt_dni.setText(modCli.getDni());
                geincli.txt_telefono.setText(modCli.getTelefono());
                geincli.txt_direccion.setText(modCli.getDireccion());

            }
        }

        if (e.getSource() == geincli.jButton_actualizar) {

            Cliente cliente = new Cliente();
            crudCli = new CRUD_Cliente();
            if (geincli.txt_id.getText().isEmpty() || geincli.txt_nombre.getText().isEmpty() || geincli.txt_apellido.getText().isEmpty() || geincli.txt_dni.getText().isEmpty()
                    || geincli.txt_telefono.getText().isEmpty() || geincli.txt_direccion.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Complete los campos");
            } else {
                cliente.setIdCliente(Integer.parseInt(geincli.txt_id.getText()));
                cliente.setNombre(geincli.txt_nombre.getText().trim());
                cliente.setApellido(geincli.txt_apellido.getText().trim());
                cliente.setDni(geincli.txt_dni.getText().trim());
                cliente.setTelefono(geincli.txt_telefono.getText().trim());
                cliente.setDireccion(geincli.txt_direccion.getText().trim());
//                cliente.setEstado(1);

                cliente.setDni(geincli.txt_dni.getText().trim());
                if (!crudCli.existeCliente(geincli.txt_dni.getText().trim())) {
                    cliente.setDni(geincli.txt_dni.getText().trim());
//                    cliente.setEstado(1);
                    if (crudCli.actualizar(cliente)) {
                        JOptionPane.showMessageDialog(null, "¡Datos del cliente actualizado!");
                        this.Limpiar();
                        ActualizarFormulario();
                        //idUsuario = 0;

                    } else {
                        JOptionPane.showMessageDialog(null, "Error al actualizar");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El cliente ya existe");

                }
            }

        }
        if (e.getSource() == geincli.jButton_eliminar) {

            if (codigoCliente == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione un cliente");
            } else {

                int respuesta = JOptionPane.showConfirmDialog(null,
                        "Esta seguro de eliminar al cliente?", "Confirmación...", JOptionPane.YES_NO_CANCEL_OPTION);
                if (respuesta == 0) {
                    crudCli = new CRUD_Cliente();
                    crudCli.eliminar(codigoCliente);
                    JOptionPane.showMessageDialog(null, "¡Cliente eliminado!");
                    ActualizarFormulario();
                    JOptionPane.showMessageDialog(null, "¡Cliente eliminado!");
                }
            }
        }

        if (e.getSource() == geincli.jButton_excel) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de excel", "xls");
            chooser.setFileFilter(filter);
            chooser.setDialogTitle("Guardar archivo");
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    String ruta = chooser.getSelectedFile().toString().concat(".xls");
                    File archivoXLS = new File(ruta);
                    if (archivoXLS.exists()) {
                        archivoXLS.delete();
                    }
                    archivoXLS.createNewFile();
                    Workbook workbook = new HSSFWorkbook();
                    FileOutputStream archivo = new FileOutputStream(archivoXLS);
                    Sheet sheet = workbook.createSheet("clientes");
                    Row headerRow = sheet.createRow(0);
                    headerRow.createCell(0).setCellValue("Id");
                    headerRow.createCell(1).setCellValue("Nombres");
                    headerRow.createCell(2).setCellValue("Apellidos");
                    headerRow.createCell(3).setCellValue("DNI");
                    headerRow.createCell(4).setCellValue("Teléfono");
                    headerRow.createCell(5).setCellValue("Dirección");

                    List<Cliente> clientes = crudCli.obtenerClientes();
                    int rowNum = 1;
                    for (Cliente cli : clientes) {
                        Row row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(cli.getIdCliente());
                        row.createCell(1).setCellValue(cli.getNombre());
                        row.createCell(2).setCellValue(cli.getApellido());
                        row.createCell(3).setCellValue(cli.getDni());
                        row.createCell(4).setCellValue(cli.getTelefono());
                        row.createCell(5).setCellValue(cli.getDireccion());
                    }
                    workbook.write(archivo);
                    archivo.close();
                    Desktop.getDesktop().open(archivoXLS);
                } catch (IOException | NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Ocurrio un problema al generar: "+ex);
                }
            }
        }
    }
}
