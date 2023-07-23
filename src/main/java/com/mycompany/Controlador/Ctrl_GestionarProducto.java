package com.mycompany.Controlador;

import com.mycompany.vista.InterGestionarProducto;
import com.mycompany.modelo.Producto;
import com.mycompany.DAO.CRUD_Producto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.util.List;

public class Ctrl_GestionarProducto implements ActionListener {

    CRUD_Producto crudPro;
    Producto modPro;
    InterGestionarProducto geinpro;
    int codigoProducto = 0;

    public Ctrl_GestionarProducto(InterGestionarProducto gepro) {
        geinpro = gepro;
        geinpro.jButton_buscar.addActionListener(this);
        geinpro.jButton_actualizar.addActionListener(this);
        geinpro.jButton_eliminar.addActionListener(this);

        crudPro = new CRUD_Producto();
        tablaListener();
        crudPro.CargarTablaProducto(geinpro.jTable_productos);
        List<String> Categorias = crudPro.CargarComboCategorias();
        for (String categoria : Categorias) {
            geinpro.jComboBox_categoria.addItem(categoria);

        }
        geinpro.setVisible(true);

    }

    void ActualizarFormulario() {
        crudPro = new CRUD_Producto();
        crudPro.CargarTablaProducto(geinpro.jTable_productos);
        List<String> Categorias = crudPro.CargarComboCategorias();
        for (String categoria : Categorias) {
            geinpro.jComboBox_categoria.addItem(categoria);

        }
    }

    void tablaListener() {
        geinpro.jTable_productos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = geinpro.jTable_productos.getSelectedRow();
                codigoProducto = (int) geinpro.jTable_productos.getValueAt(fila, 0);

            }
        });
    }

    /*
    * ***********************************************************
    *    METODO PARA LIMPIAR
    * ***********************************************************
     */
    void Limpiar() {
        geinpro.txt_nombre.setText("");
        geinpro.txt_cantidad.setText("");
        geinpro.txt_precio.setText("");
        geinpro.txt_descripcion.setText("");
        geinpro.jComboBox_igv.setSelectedItem("Seleccione IGV:");
        geinpro.jComboBox_categoria.setSelectedItem("Seleccione categoría:");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == geinpro.jButton_buscar) {

            int idProducto = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el Id del producto a buscar"));
            crudPro = new CRUD_Producto();
            modPro = crudPro.EnviarDatosProductoSeleccionado(idProducto);

            if (modPro == null) {
                JOptionPane.showMessageDialog(null, "El ID " + idProducto + " no existe en la tabla producto.");
            } else {
                geinpro.txt_id.setText(Integer.toString(modPro.getIdProducto()));
                geinpro.txt_nombre.setText(modPro.getNombre());
                geinpro.txt_cantidad.setText(Integer.toString(modPro.getCantidad()));
                geinpro.txt_precio.setText(Double.toString(modPro.getPrecio()));
                geinpro.txt_descripcion.setText(modPro.getDescripcion());
                
                switch (modPro.getPorcentajeIgv()) {
                    case 18:
                        geinpro.jComboBox_igv.setSelectedIndex(2);
                        break ;
                    case 0:
                        geinpro.jComboBox_igv.setSelectedIndex(1);
                        break;
                    default:
                        geinpro.jComboBox_igv.setSelectedIndex(0);
                }
                geinpro.jComboBox_categoria.setSelectedItem(modPro.getCategoria());
            }
        }
         if (e.getSource() == geinpro.jButton_actualizar) {
             Producto producto = new Producto();
        crudPro = new CRUD_Producto();
        String IGV = "";
        String categoria = "";
        IGV = geinpro.jComboBox_igv.getSelectedItem().toString().trim();
        categoria = geinpro.jComboBox_categoria.getSelectedItem().toString().trim();

        
        
        if (geinpro.txt_nombre.getText().isEmpty() || geinpro.txt_cantidad.getText().isEmpty() || geinpro.txt_precio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Complete todos los campos obligatorios");
        } else {
            if (IGV.equalsIgnoreCase("Seleccione IGV:")) {
                JOptionPane.showMessageDialog(null, "Seleccione IGV.");
            } else {
                if (categoria.equalsIgnoreCase("Seleccione Categoria:")) {
                    JOptionPane.showMessageDialog(null, "Seleccione categoria");
                } else {
                    try {
                        producto.setNombre(geinpro.txt_nombre.getText().trim());
                        producto.setCantidad(Integer.parseInt(geinpro.txt_cantidad.getText().trim()));
                        String precioTXT = "";
                        double Precio = 0.0;
                        precioTXT = geinpro.txt_precio.getText().trim();
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

                        producto.setDescripcion(geinpro.txt_descripcion.getText().trim());
                        if (IGV.equalsIgnoreCase("No")) {
                            producto.setPorcentajeIgv(0);
                        } else if (IGV.equalsIgnoreCase("18%")) {
                            producto.setPorcentajeIgv(18);
                        }

                        //cargar método con idCategoria
                        int categoriaId = crudPro.IdCategoria(geinpro.jComboBox_categoria.getSelectedItem().toString());
                        producto.setIdCategoria(categoriaId);
//                        producto.setEstado(1);
                        
                        producto.setNombre(geinpro.txt_nombre.getText().trim());
                        if (!crudPro.existeProducto(geinpro.txt_nombre.getText().trim())) {
                            producto.setNombre(geinpro.txt_nombre.getText().trim());
                            producto.setIdProducto(Integer.parseInt(geinpro.txt_id.getText()));
//                            producto.setEstado(1);
                            if (crudPro.actualizar(producto)) {
                                JOptionPane.showMessageDialog(null, "Registro actualizado");
                                this.Limpiar();
                                this.ActualizarFormulario();
                                this.geinpro.jComboBox_igv.setSelectedItem("Seleccione IGV:");
                                this.Limpiar();
                            } else {
                                JOptionPane.showMessageDialog(null, "Error al Actualizar");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "El producto ya existe");
                        }
                    } catch (HeadlessException | NumberFormatException ex) {
                        System.out.println("Error en: " + ex);
                    }
                }
            }
        }
         }
//        if (e.getSource() == geinpro.jButton_actualizar) {
//            Producto producto = new Producto();
//            crudPro = new CRUD_Producto();
//            String IGV = "";
//            String categoria = "";
//            IGV = geinpro.jComboBox_igv.getSelectedItem().toString().trim();
//            categoria = geinpro.jComboBox_categoria.getSelectedItem().toString().trim();
//
//            if (geinpro.txt_id.getText().isEmpty() || geinpro.txt_nombre.getText().isEmpty() || geinpro.txt_cantidad.getText().isEmpty() || geinpro.txt_precio.getText().isEmpty() || geinpro.txt_descripcion.getText().isEmpty()) {
//                JOptionPane.showMessageDialog(null, "Complete todos los campos obligatorios");
//            } else {
//                if (IGV.equalsIgnoreCase("Seleccione IGV:")) {
//                    JOptionPane.showMessageDialog(null, "Seleccione IGV.");
//                } else {
//                    if (categoria.equalsIgnoreCase("Seleccione Categoria:")) {
//                        JOptionPane.showMessageDialog(null, "Seleccione categoria");
//                    } else {
//                        try {
//                            producto.setNombre(geinpro.txt_nombre.getText().trim());
//                            producto.setCantidad(Integer.parseInt(geinpro.txt_cantidad.getText().trim()));
//                            String precioTXT = "";
//                            double Precio = 0.0;
//                            precioTXT = geinpro.txt_precio.getText().trim();
//                            boolean aux = false;
//                            /*
//                            * Si el usuario ingresa (,) como punto decimal, lo transformamos a punto.
//                             */
//                            for (int i = 0; i < precioTXT.length(); i++) {
//                                if (precioTXT.charAt(i) == ',') {
//                                    String precioNuevo = precioTXT.replace(",", ".");
//                                    Precio = Double.parseDouble(precioNuevo);
//                                    aux = true;
//                                }
//                            }
//                            //evaluar 
//                            if (aux == true) {
//                                producto.setPrecio(Precio);
//                            } else {
//                                Precio = Double.parseDouble(precioTXT);
//                                producto.setPrecio(Precio);
//                            }
//
//                            producto.setDescripcion(geinpro.txt_descripcion.getText().trim());
//                            if (IGV.equalsIgnoreCase("No")) {
//                                producto.setPorcentajeIgv(0);
//                            } else if (IGV.equalsIgnoreCase("18%")) {
//                                producto.setPorcentajeIgv(18);
//                            }
//
//                            //cargar método con idCategoria
//                            int categoriaId = crudPro.IdCategoria(geinpro.jComboBox_categoria.getSelectedItem().toString());
//                            producto.setIdCategoria(categoriaId);
////                            producto.setEstado(1);
//
//                            producto.setNombre(geinpro.txt_nombre.getText().trim());
//                            if (!crudPro.existeProducto(geinpro.txt_nombre.getText().trim())) {
//                                producto.setNombre(geinpro.txt_nombre.getText().trim());
////                                producto.setEstado(1);
//                                if (crudPro.actualizar(producto)) {
//                                    JOptionPane.showMessageDialog(null, "Registro actualizado");
//                                    this.Limpiar();
//                                    this.ActualizarFormulario();
//
//                                    switch (modPro.getPorcentajeIgv()) {
//                                        case 18:
//                                            geinpro.jComboBox_igv.setSelectedIndex(2);
//                                            break;
//                                        case 0:
//                                            geinpro.jComboBox_igv.setSelectedIndex(1);
//                                            break;
//                                        default:
//                                            geinpro.jComboBox_igv.setSelectedIndex(0);
//                                    };
//                                    
//                                } else {
//                                    JOptionPane.showMessageDialog(null, "Error al Actualizar");
//                                }
//                            } else {
//                                JOptionPane.showMessageDialog(null, "El producto ya existe");
//                            }
//                        } catch (HeadlessException | NumberFormatException ex) {
//                            System.out.println("Error en: " + ex);
//                        }
//                    }
//                }
//            }
//        }
        if (e.getSource() == geinpro.jButton_eliminar) {
            if (codigoProducto == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione un producto");
            } else {

                int respuesta = JOptionPane.showConfirmDialog(null,
                        "Esta seguro de eliminar al producto?", "Confirmación...", JOptionPane.YES_NO_CANCEL_OPTION);
                if (respuesta == 0) {
                    crudPro = new CRUD_Producto();
                    crudPro.eliminar(codigoProducto);
                    ActualizarFormulario();
                    JOptionPane.showMessageDialog(null, "¡Producto eliminado!");
                }
            }

        }
    }

}
