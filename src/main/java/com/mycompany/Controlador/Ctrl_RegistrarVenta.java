/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Controlador;

import com.mycompany.vista.InterFacturacion;
import com.mycompany.modelo.Cliente;
import com.mycompany.modelo.DetalleVenta;
import com.mycompany.modelo.Producto;
import com.mycompany.modelo.CabeceraVenta;
import com.mycompany.DAO.CRUD_Producto;
import com.mycompany.DAO.CRUD_RegistrarVenta;
import com.mycompany.DAO.CRUD_Cliente;
import com.mycompany.DAO.VentaPDF;
import com.mycompany.Principal.Main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class Ctrl_RegistrarVenta implements ActionListener {

    //modelo de datos
    private DefaultTableModel modeloDatosProductos;
    //lista para detalle de venta del producto
    ArrayList<DetalleVenta> listaProductos = new ArrayList<>();
    CRUD_RegistrarVenta crudVen;
    CRUD_Producto crudPro;
//    CabeceraVenta modCab;
//    DetalleVenta modDel;
    Cliente modCli;
//    ProductoFacturacion modProFac;
    InterFacturacion inven;
    CRUD_Cliente crudCli;

//    private int idCliente = 0; //id del cliente
//
    private int idProducto = 0;
    private String nombre = "";
    private String apellido = "";
//    private int cantidadProductoBBDD = 0;
    private double precioUnitario = 0.0;
    private int porcentajeIgv = 0;
//
    private int cantidad = 0; //cantidad de productos a comprar
    private double subtotal = 0.0; //cantidad por precio
    private double descuento = 0.0;
    private double IGV = 0.0;
    private double totalPagar = 0.0;
//
//    //variables para calculo global
    private double subtotalGeneral = 0.0;
    private double descuentoGeneral = 0.0;
    private double IgvGeneral = 0.0;
    private double totalPagarGeneral = 0.0;
//    //fin variables
//
    private int auxIdDetalle = 0; //ID

    public Ctrl_RegistrarVenta(InterFacturacion venta) {

        inven = venta;
        inven.jButton_busca_cliente.addActionListener(this);
        inven.jButton_RegistrarVenta.addActionListener(this);
        inven.jButton_añadir_producto.addActionListener(this);
        inven.jButton_calcular_cambio.addActionListener(this);
        inven.txt_efectivo.setEnabled(false);
        inven.jButton_calcular_cambio.setEnabled(false);
        inven.txt_subtotal.setText("0.0");
        inven.txt_iGV.setText("0.0");
        inven.txt_descuento.setText("0.0");
        inven.txt_total_pagar.setText("0");
        inicializarTablaProductos();
        crudPro = new CRUD_Producto();
         tablaListener();
        List<String> Productos = crudPro.CargarComboProductos();
        for (String combo : Productos) {
            inven.jComboBox_producto.addItem(combo);
        }

//        inven.setVisible(true);
        crudCli = new CRUD_Cliente();
        List<String> Clientes = crudCli.CargarComboCliente();
        for (String cliente : Clientes) {
            inven.jComboBox_cliente.addItem(cliente);
//            System.out.println(cliente);

        }
        inven.setVisible(true);
    }

//    metodo para msotrar la tabla de productos
    private void inicializarTablaProductos() {
        modeloDatosProductos = new DefaultTableModel();
        //añadir columnas
        modeloDatosProductos.addColumn("N°");
        modeloDatosProductos.addColumn("nombre");
        modeloDatosProductos.addColumn("cantidad");
        modeloDatosProductos.addColumn("P. Unitario");
        modeloDatosProductos.addColumn("Subtotal");
        modeloDatosProductos.addColumn("Descuento");
        modeloDatosProductos.addColumn("IGV");
        modeloDatosProductos.addColumn("Total a pagar");
        modeloDatosProductos.addColumn("Accion");
        //pegar dato del modelo tabla
        this.inven.jTable_productos.setModel(modeloDatosProductos);
    }
    int idArrayList = 0;

    void tablaListener() {
        inven.jTable_productos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = inven.jTable_productos.getSelectedRow();
                idArrayList = (int) inven.jTable_productos.getValueAt(fila, 0);
                int opcion = JOptionPane.showConfirmDialog(null, "¿Eliminar producto?");
                //opciones de confirmar dialog = (si = 0 ; no= 1 ; cancel = 2; close = -1)
                switch (opcion) {
                    case 0: //presione si
                        listaProductos.remove(idArrayList - 1);
                        CalcularTotalPagar();
                        listaTablaProductos();
                        break;
                    case 1: //presione no
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void listaTablaProductos() {
        this.modeloDatosProductos.setRowCount(listaProductos.size());
        for (int i = 0; i < listaProductos.size(); i++) {
            this.modeloDatosProductos.setValueAt(i + 1, i, 0);
            this.modeloDatosProductos.setValueAt(listaProductos.get(i).getNombre(), i, 1);
            this.modeloDatosProductos.setValueAt(listaProductos.get(i).getCantidad(), i, 2);
            this.modeloDatosProductos.setValueAt(listaProductos.get(i).getPrecioUnitario(), i, 3);
            this.modeloDatosProductos.setValueAt(listaProductos.get(i).getSubtotal(), i, 4);
            this.modeloDatosProductos.setValueAt(listaProductos.get(i).getDescuento(), i, 5);
            this.modeloDatosProductos.setValueAt(listaProductos.get(i).getIgv(), i, 6);
            this.modeloDatosProductos.setValueAt(listaProductos.get(i).getTotalPagar(), i, 7);
            this.modeloDatosProductos.setValueAt("Eliminar", i, 8); //poner luego boton de eliminar
        }
        //añadir
        inven.jTable_productos.setModel(modeloDatosProductos);
    }

    /*metodo para validar que usuario no ingrese caracteres no numericos*/
    private boolean validar(String valor) {
        try {
            int num = Integer.parseInt(valor);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("");
            return false;
        }
    }

    /*metodo para calcular Iva*/
    private double CalcularIgv(double precio, int porcentajeIva) {
        int p_igv = porcentajeIva;
        switch (p_igv) {
            case 0:
                IGV = 0.0;
                break;
            case 18:
                IGV = (precio * cantidad) * 0.18;
                break;
            default:
                break;
        }
        return IGV;
    }

    private void CalcularTotalPagar() {
        subtotalGeneral = 0;
        descuentoGeneral = 0;
        IgvGeneral = 0;
        totalPagarGeneral = 0;
//
        for (DetalleVenta elemento : listaProductos) {

            subtotalGeneral += elemento.getSubtotal();
            descuentoGeneral += elemento.getDescuento();
            IgvGeneral += elemento.getIgv();
            totalPagarGeneral += elemento.getTotalPagar();
        }
        //redondear
        subtotalGeneral = (double) Math.round(subtotalGeneral * 100) / 100;
        IgvGeneral = (double) Math.round(IgvGeneral * 100) / 100;
        descuentoGeneral = (double) Math.round(descuentoGeneral * 100) / 100;
        totalPagarGeneral = (double) Math.round(totalPagarGeneral * 100) / 100;

        //enviar datos a la vista
        inven.txt_subtotal.setText(String.valueOf(subtotalGeneral));
        inven.txt_iGV.setText(String.valueOf(IgvGeneral));
        inven.txt_descuento.setText(String.valueOf(descuentoGeneral));
        inven.txt_total_pagar.setText(String.valueOf(totalPagarGeneral));
    }

    private boolean validarDouble(String valor) {
        try {
            double num = Double.parseDouble(valor);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("");
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == inven.jButton_busca_cliente) {
//
            String clienteBuscar = inven.txt_cliente_buscar.getText().trim();
            if (clienteBuscar == null) {
                JOptionPane.showMessageDialog(null, "¡Ingrese un DNI a buscar!");
            } else {
                crudCli = new CRUD_Cliente();
                modCli = new Cliente();

                modCli = crudCli.buscarClientePorDNI(clienteBuscar);
                String cliente = modCli.getNombre() + " " + modCli.getApellido();
                inven.jComboBox_cliente.setSelectedItem(cliente);
            }

        }
        if (e.getSource() == inven.jButton_añadir_producto) {
            String combo = inven.jComboBox_producto.getSelectedItem().toString();
            //validar seleccion de producto
            if (combo.equalsIgnoreCase("Seleccione producto:")) {
                JOptionPane.showMessageDialog(null, "¡Seleccione un producto!");
            } else {
                String nombreProducto = inven.jComboBox_producto.getSelectedItem().toString();
                crudPro = new CRUD_Producto();
                Producto productoBD = crudPro.obtenerDatosProductoPorNombre(nombreProducto);
                DetalleVenta detalleVenta = new DetalleVenta();
                //valida que ingrese cantidad
                if (!inven.txt_cantidad.getText().isEmpty()) {
                    //validad que el usuario no ingrese caracteres no numéricos
                    boolean validacion = validar(inven.txt_cantidad.getText());
                    if (validacion == true) {
                        //validar cantidad sea mayor a 0
                        if (Integer.parseInt(inven.txt_cantidad.getText()) > 0) {
                            cantidad = Integer.parseInt(inven.txt_cantidad.getText());
                            int idProducto = productoBD.getIdProducto();
                            nombre = productoBD.getNombre();
                            int cantidadProductoBBDD = productoBD.getCantidad();
                            precioUnitario = productoBD.getPrecio();
                            porcentajeIgv = productoBD.getPorcentajeIgv();
                            this.CalcularIgv(precioUnitario, porcentajeIgv);

                            //validar cantidad de producto seleccionado no sea mayor al stock de bd
                            if (cantidad <= cantidadProductoBBDD) {
//                                Producto productoTabla = productoBD;
//                                productoTabla.setCantidad(cantidad);
                                subtotal = precioUnitario * cantidad;
//                            this.CalcularIgv(precioUnitario, porcentajeIgv);
                                totalPagar = subtotal + IGV + descuento;
                                //redondear
                                subtotal = (double) Math.round(subtotal * 100) / 100;
                                IGV = (double) Math.round(IGV * 100) / 100;
                                descuento = (double) Math.round(descuento * 100) / 100;
                                totalPagar = (double) Math.round(totalPagar * 100) / 100;

                                detalleVenta.setIdProducto(idProducto);
                                detalleVenta.setNombre(nombre);

                                detalleVenta.setCantidad(Integer.parseInt(inven.txt_cantidad.getText()));
                                detalleVenta.setPrecioUnitario(precioUnitario);

                                detalleVenta.setSubtotal(subtotal);
                                detalleVenta.setIgv(IGV);
                                detalleVenta.setTotalPagar(totalPagar);

                                auxIdDetalle++;
                                listaProductos.add(detalleVenta);
                                JOptionPane.showMessageDialog(null, "Producto agregado");

                                modeloDatosProductos.addRow(new Object[]{
                                    auxIdDetalle,
                                    detalleVenta.getNombre(),
                                    detalleVenta.getCantidad(),
                                    detalleVenta.getPrecioUnitario(),
                                    detalleVenta.getSubtotal(),
                                    detalleVenta.getDescuento(),
                                    detalleVenta.getIgv(),
                                    detalleVenta.getTotalPagar(),
                                    1
                                });

                                inven.txt_cantidad.setText("");//limpiar el campo
                                this.CalcularTotalPagar();
                                inven.txt_efectivo.setEnabled(true);
                                inven.jButton_calcular_cambio.setEnabled(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "La cantidad supera el stock");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "No puede ser 0 ni negativa");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No se permiten caracteres no numéricos");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese la cantidad de productos");
                }
            }
            //llamar metodo
            this.listaTablaProductos();
        }
        if (e.getSource() == inven.jButton_calcular_cambio) {
            if (!inven.txt_efectivo.getText().isEmpty()) {
                //validar que el usuario no ingrese otros caracteres no numericos
                boolean validacion = validarDouble(inven.txt_efectivo.getText());
                if (validacion == true) {
                    //validar efectivo sea mayor a 0
                    double efc = Double.parseDouble(inven.txt_efectivo.getText().trim());
                    double top = Double.parseDouble(inven.txt_total_pagar.getText().trim());
                    if (efc < top) {
                        JOptionPane.showMessageDialog(null, "Dinero en efecto no es suficiente");
                    } else {
                        Double cambio = (efc - top);
                        double cambi = (double) Math.round(cambio * 100d) / 100;
                        String camb = String.valueOf(cambi);
                        inven.txt_cambio.setText(camb);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se admiten caracteres no numéricos");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese dinero en efectivo");
            }
        }
        if (e.getSource() == inven.jButton_RegistrarVenta) {
            CabeceraVenta cabeceraVenta = new CabeceraVenta();
            DetalleVenta detalleVenta = new DetalleVenta();
            crudVen = new CRUD_RegistrarVenta();

            String fechaActual = "";
            Date date = new Date();
            fechaActual = new SimpleDateFormat("yyyy/MM/dd").format(date);

            if (!inven.jComboBox_cliente.getSelectedItem().equals("Seleccione cliente:")) {
                if (listaProductos.size() > 0) {
                    String clienteSeleccionado = inven.jComboBox_cliente.getSelectedItem().toString();
                    //metodo para obtener id de cliente
                    int idCliente = crudCli.obtenerIdClientePorNombres(clienteSeleccionado);
                    //registrar cabecera venta
                    cabeceraVenta.setIdCabeceraventa(0);
                    cabeceraVenta.setIdCliente(idCliente);
                    cabeceraVenta.setValorPagar(Double.parseDouble(inven.txt_total_pagar.getText()));
                    cabeceraVenta.setFechaVenta(fechaActual);
                    cabeceraVenta.setEstado(1);
                    if (crudVen.guardar(cabeceraVenta)) {
                        //generar la factura
                        VentaPDF pdf = new VentaPDF();
                        pdf.DatosCliente(idCliente);
                        pdf.generarFacturaPDF(listaProductos);
                        //guardar detalle
                        for (DetalleVenta elemento : listaProductos) {
                            detalleVenta.setIdDetalleVenta(0);
                            detalleVenta.setIdCabeceraVenta(0);
                            detalleVenta.setIdProducto(elemento.getIdProducto());
                            detalleVenta.setCantidad(elemento.getCantidad());
                            detalleVenta.setPrecioUnitario(elemento.getPrecioUnitario());
                            detalleVenta.setSubtotal(elemento.getSubtotal());
                            detalleVenta.setDescuento(elemento.getDescuento());
                            detalleVenta.setIgv(elemento.getIgv());
                            detalleVenta.setTotalPagar(elemento.getTotalPagar());
                            detalleVenta.setEstado(1);
                            crudVen.guardarDetalle(detalleVenta);
                            crudPro.RestarStockProductos(elemento.getIdProducto(), elemento.getCantidad());
                        }
                        JOptionPane.showMessageDialog(null, "¡Venta registrada!");
                        inven.txt_subtotal.setText("0.0");
                        inven.txt_iGV.setText("0.0");
                        inven.txt_descuento.setText("0.0");
                        inven.txt_total_pagar.setText("0.0");
                        inven.txt_efectivo.setText("0.0");
                        inven.txt_cambio.setText("0.0");
                        auxIdDetalle = 1;
                        //vaciamos la lista
                        listaProductos.clear();
//                        listaTablaProductos();

                    } else {
                        JOptionPane.showMessageDialog(null, "Error al guardar cabecera");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un producto");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un cliente");
            }
        }

    }

}

//
//
//public class Ctrl_RegistrarVenta implements ActionListener {
//
//    //modelo de datos
//    private DefaultTableModel modeloDatosProductos;
//    //lista para detalle de venta del producto
//    List<ProductoFacturacion> listaProductos = new ArrayList<>();
//    CRUD_RegistrarVenta crudVen;
//    CRUD_Producto crudPro;
//    CabeceraVenta modCab;
//    DetalleVenta modDel;
//    Cliente modCli;
//    ProductoFacturacion modProFac;
//    InterFacturacion inven;
//    CRUD_Cliente crudCli;
////
////    private int idCliente = 0; //id del cliente
////
//    private int idProducto = 0;
//    private String nombre = "";
//    private String apellido = "";
////    private int cantidadProductoBBDD = 0;
////    private double precioUnitario = 0.0;
////    private int porcentajeIgv = 0;
////
//    private int cantidad = 0; //cantidad de productos a comprar
////    private double subtotal = 0.0; //cantidad por precio
//    private double descuento = 0.0;
//    private double IGV = 0.0;
////    private double totalPagar = 0.0;
////
////    //variables para calculo global
////    private double subtotalGeneral = 0.0;
////    private double descuentoGeneral = 0.0;
////    private double IgvGeneral = 0.0;
////    private double totalPagarGeneral = 0.0;
////    //fin variables
////
//    private int auxIdDetalle = 0; //ID
//
//    public Ctrl_RegistrarVenta(InterFacturacion venta) {
//
//        inven = venta;
//        inven.jButton_busca_cliente.addActionListener(this);
//        inven.jButton_RegistrarVenta.addActionListener(this);
//        inven.jButton_añadir_producto.addActionListener(this);
//        inven.jButton_calcular_cambio.addActionListener(this);
//        inven.txt_efectivo.setEnabled(false);
//        inven.jButton_calcular_cambio.setEnabled(false);
//        inven.txt_subtotal.setText("0.0");
//        inven.txt_iGV.setText("0.0");
//        inven.txt_descuento.setText("0.0");
//        inven.txt_total_pagar.setText("0");
//        inicializarTablaProductos();
//        crudPro = new CRUD_Producto();
//        List<String> Productos = crudPro.CargarComboProductos();
//        for (String combo : Productos) {
//            inven.jComboBox_producto.addItem(combo);
//        }
////        inven.setVisible(true);
//        crudCli = new CRUD_Cliente();
//        List<String> Clientes = crudCli.CargarComboCliente();
//        for (String cliente : Clientes) {
//            inven.jComboBox_cliente.addItem(cliente);
////            System.out.println(cliente);
//
//        }
//        inven.setVisible(true);
//    }
//
////    metodo para msotrar la tabla de productos
//    private void inicializarTablaProductos() {
//        modeloDatosProductos = new DefaultTableModel();
//        //añadir columnas
//        modeloDatosProductos.addColumn("N°");
//        modeloDatosProductos.addColumn("nombre");
//        modeloDatosProductos.addColumn("cantidad");
//        modeloDatosProductos.addColumn("P. Unitario");
//        modeloDatosProductos.addColumn("Subtotal");
//        modeloDatosProductos.addColumn("Descuento");
//        modeloDatosProductos.addColumn("IGV");
//        modeloDatosProductos.addColumn("Total a pagar");
//        modeloDatosProductos.addColumn("Accion");
//        //pegar dato del modelo tabla
//        this.inven.jTable_productos.setModel(modeloDatosProductos);
//    }
////    private void listaTablaProductos() {
////        this.modeloDatosProductos.setRowCount(listaProductos.size());
////        for (int i = 0; i < listaProductos.size(); i++) {
////            this.modeloDatosProductos.setValueAt(i + 1, i, 0);
////            this.modeloDatosProductos.setValueAt(listaProductos.get(i).getNombre(), i, 1);
////            this.modeloDatosProductos.setValueAt(listaProductos.get(i).getCantidad(), i, 2);
////            this.modeloDatosProductos.setValueAt(listaProductos.get(i).getPrecioUnitario(), i, 3);
////            this.modeloDatosProductos.setValueAt(listaProductos.get(i).getSubtotal(), i, 4);
////            this.modeloDatosProductos.setValueAt(listaProductos.get(i).getDescuento(), i, 5);
////            this.modeloDatosProductos.setValueAt(listaProductos.get(i).getIgv(), i, 6);
////            this.modeloDatosProductos.setValueAt(listaProductos.get(i).getTotalPagar(), i, 7);
////            this.modeloDatosProductos.setValueAt("Eliminar", i, 8); //poner luego boton de eliminar
////        }
////        //añadir
////        inven.jTable_productos.setModel(modeloDatosProductos);
////    }
//
//    /*metodo para validar que usuario no ingrese caracteres no numericos*/
//    private boolean validar(String valor) {
//        try {
//            int num = Integer.parseInt(valor);
//            return true;
//        } catch (NumberFormatException e) {
//            System.out.println("");
//            return false;
//        }
//    }
//
//    /*metodo para calcular Iva*/
//    private double CalcularIgv(double precio, int porcentajeIva) {
//        int p_igv = porcentajeIva;
//        switch (p_igv) {
//            case 0:
//                IGV = 0.0;
//                break;
//            case 18:
//                IGV = precio * 0.18;
//                break;
//            default:
//                break;
//        }
//        return IGV;
//    }
//
//    private void CalcularTotalPagar() {
//        double subtotalGeneral = 0;
//        double descuentoGeneral = 0;
//        double IgvGeneral = 0;
//        double totalPagarGeneral = 0;
//
//        for (ProductoFacturacion elemento : listaProductos) {
//            subtotalGeneral += elemento.getSubtotal();
//            descuentoGeneral += elemento.getDescuento();
//            IgvGeneral += elemento.getMontoIgv();
//            totalPagarGeneral += elemento.getMontoTotal();
//        }
//        //redondear
//        subtotalGeneral = (double) Math.round(subtotalGeneral * 100) / 100;
//        IgvGeneral = (double) Math.round(IgvGeneral * 100) / 100;
//        descuentoGeneral = (double) Math.round(descuentoGeneral * 100) / 100;
//        totalPagarGeneral = (double) Math.round(totalPagarGeneral * 100) / 100;
//
//        //enviar datos a la vista
//        inven.txt_subtotal.setText(String.valueOf(subtotalGeneral));
//        inven.txt_iGV.setText(String.valueOf(IgvGeneral));
//        inven.txt_descuento.setText(String.valueOf(descuentoGeneral));
//        inven.txt_total_pagar.setText(String.valueOf(totalPagarGeneral));
//    }
//
//    private boolean validarDouble(String valor) {
//        try {
//            double num = Double.parseDouble(valor);
//            return true;
//        } catch (NumberFormatException e) {
//            System.out.println("");
//            return false;
//        }
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == inven.jButton_busca_cliente) {
////
//            String clienteBuscar = inven.txt_cliente_buscar.getText().trim();
//            if (clienteBuscar == null) {
//                JOptionPane.showMessageDialog(null, "¡Ingrese un DNI a buscar!");
//            } else {
//                crudCli = new CRUD_Cliente();
//                modCli = new Cliente();
//
//                modCli = crudCli.buscarClientePorDNI(clienteBuscar);
//                String cliente = modCli.getNombre() + " " + modCli.getApellido();
//                inven.jComboBox_cliente.setSelectedItem(cliente);
//            }
//
//        }
//        if (e.getSource() == inven.jButton_añadir_producto) {
//            String combo = inven.jComboBox_producto.getSelectedItem().toString();
//            //validar seleccion de producto
//            if (combo.equalsIgnoreCase("Seleccione producto:")) {
//                JOptionPane.showMessageDialog(null, "¡Seleccione un producto!");
//            } else {
//                String nombreProducto = inven.jComboBox_producto.getSelectedItem().toString();
//                crudPro = new CRUD_Producto();
//                Producto productoBD = crudPro.obtenerDatosProductoPorNombre(nombreProducto);
//                modProFac = new ProductoFacturacion();
//                //valida que ingrese cantidad
//                if (!inven.txt_cantidad.getText().isEmpty()) {
//                    //validad que el usuario no ingrese caracteres no numéricos
//                    boolean validacion = validar(inven.txt_cantidad.getText());
//                    if (validacion == true) {
//                        //validar cantidad sea mayor a 0
//                        if (Integer.parseInt(inven.txt_cantidad.getText()) > 0) {
//                            cantidad = Integer.parseInt(inven.txt_cantidad.getText());
//                            int stock = productoBD.getCantidad();
//                            //validar cantidad de producto seleccionado no sea mayor al stock de bd
//                            if (cantidad <= stock) {
//                                Producto productoTabla = productoBD;
//                                productoTabla.setCantidad(cantidad);
//                                modProFac.setProducto(productoTabla);
//                                double precioUnitario = productoBD.getPrecio();
//                                double subtotal = precioUnitario * cantidad;
//                                double montoIgv = CalcularIgv(subtotal, productoBD.getPorcentajeIgv());
//                                double totalPagar = subtotal + montoIgv;
//                                //redondear
//                                subtotal = (double) Math.round(subtotal * 100) / 100;
//                                montoIgv = (double) Math.round(IGV * 100) / 100;
//
//                                totalPagar = (double) Math.round(totalPagar * 100) / 100;
//
//                                modProFac.setSubtotal(subtotal);
//                                modProFac.setMontoIgv(montoIgv);
//                                modProFac.setMontoTotal(totalPagar);
//
//                                auxIdDetalle++;
//                                listaProductos.add(modProFac);
//                                JOptionPane.showMessageDialog(null, "Producto agregado");
//
//                                modeloDatosProductos.addRow(new Object[]{
//                                    auxIdDetalle,
//                                    modProFac.getProducto().getNombre(),
//                                    modProFac.getProducto().getCantidad(),
//                                    modProFac.getProducto().getPrecio(),
//                                    modProFac.getSubtotal(),
//                                    modProFac.getDescuento(),
//                                    modProFac.getMontoIgv(),
//                                    modProFac.getMontoTotal(),
//                                    1
//                                });
//
//                                inven.txt_cantidad.setText("");//limpiar el campo
//                                CalcularTotalPagar();
//                                inven.txt_efectivo.setEnabled(true);
//                                inven.jButton_calcular_cambio.setEnabled(true);
//                            } else {
//                                JOptionPane.showMessageDialog(null, "La cantidad supera el stock");
//                            }
//                        } else {
//                            JOptionPane.showMessageDialog(null, "No puede ser 0 ni negativa");
//                        }
//                    } else {
//                        JOptionPane.showMessageDialog(null, "No se permiten caracteres no numéricos");
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(null, "Ingrese la cantidad de productos");
//                }
//            }
//            //llamar metodo
////            this.listaTablaProductos();
//        }
//        if (e.getSource() == inven.jButton_calcular_cambio) {
//            if (!inven.txt_efectivo.getText().isEmpty()) {
//                //validar que el usuario no ingrese otros caracteres no numericos
//                boolean validacion = validarDouble(inven.txt_efectivo.getText());
//                if (validacion == true) {
//                    //validar efectivo sea mayor a 0
//                    double efc = Double.parseDouble(inven.txt_efectivo.getText().trim());
//                    double top = Double.parseDouble(inven.txt_total_pagar.getText().trim());
//                    if (efc < top) {
//                        JOptionPane.showMessageDialog(null, "Dinero en efecto no es suficiente");
//                    } else {
//                        Double cambio = (efc - top);
//                        double cambi = (double) Math.round(cambio * 100d) / 100;
//                        String camb = String.valueOf(cambi);
//                        inven.txt_cambio.setText(camb);
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(null, "No se admiten caracteres no numéricos");
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "Ingrese dinero en efectivo");
//            }
//        }
//        if (e.getSource() == inven.jButton_RegistrarVenta) {
//            CabeceraVenta cabeceraVenta = new CabeceraVenta();
//            DetalleVenta detalleVenta = new DetalleVenta();
//            crudVen = new CRUD_RegistrarVenta();
//
//            String fechaActual = "";
//            Date date = new Date();
//            fechaActual = new SimpleDateFormat("yyyy/MM/dd").format(date);
//
//            if (!inven.jComboBox_cliente.getSelectedItem().equals("Seleccione cliente:")) {
//                if (listaProductos.size() > 0) {
//                    String clienteSeleccionado = inven.jComboBox_cliente.getSelectedItem().toString();
//                    //metodo para obtener id de cliente
//                    int idCliente = crudCli.obtenerIdClientePorNombres(clienteSeleccionado);
//                    //registrar cabecera venta
//                    cabeceraVenta.setIdCabeceraventa(0);
//                    cabeceraVenta.setIdCliente(idCliente);
//                    cabeceraVenta.setValorPagar(Double.parseDouble(inven.txt_total_pagar.getText()));
//                    cabeceraVenta.setFechaVenta(fechaActual);
////                    cabeceraVenta.setEstado(1);
//                    if (crudVen.guardar(cabeceraVenta)) {
//                        JOptionPane.showMessageDialog(null, "¡Venta registrada!");
//                        //generar la factura
//                        VentaPDF pdf = new VentaPDF();
//                        pdf.DatosCliente(idCliente);
//                        pdf.generarFacturaPDF(listaProductos);
//                        //guardar detalle
//                        for (ProductoFacturacion elemento : listaProductos) {
//                            detalleVenta.setIdDetalleVenta(0);
//                            detalleVenta.setIdCabeceraVenta(0);
//                            detalleVenta.setIdProducto(elemento.getProducto().getIdProducto());
//                            detalleVenta.setCantidad(elemento.getProducto().getCantidad());
//                            detalleVenta.setPrecioUnitario(elemento.getProducto().getPrecio());
//                            detalleVenta.setSubtotal(elemento.getSubtotal());
//                            detalleVenta.setDescuento(elemento.getDescuento());
//                            detalleVenta.setIgv(elemento.getMontoIgv());
//                            detalleVenta.setTotalPagar(elemento.getMontoTotal());
////                            detalleVenta.setEstado(1);
//                            crudVen.guardarDetalle(detalleVenta);
//                            crudPro.RestarStockProductos(elemento.getProducto().getIdProducto(), elemento.getProducto().getCantidad());
//                        }
//                        inven.txt_subtotal.setText("0.0");
//                        inven.txt_iGV.setText("0.0");
//                        inven.txt_descuento.setText("0.0");
//                        inven.txt_total_pagar.setText("0.0");
//                        inven.txt_efectivo.setText("0.0");
//                        inven.txt_cambio.setText("0.0");
//                        auxIdDetalle = 1;
//                        //vaciamos la lista
//                        listaProductos.clear();
////                        listaTablaProductos();
//                        
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Error al guardar cabecera");
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(null, "Seleccione un producto");
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "Seleccione un cliente");
//            }
//        }
//
//    }
//
//}
