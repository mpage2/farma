package com.mycompany.DAO;

import com.mycompany.modelo.DetalleVenta;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.bd.Conexion;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.mycompany.Controlador.*;
import java.sql.Connection;
import java.sql.Statement;
//
public class VentaPDF {

    private String nombreCliente;
    private String dniCliente;
    private String telefonoCliente;
    private String direccionCliente;

    private String fechaActual = "";
    private String nombreArchivoPDFVenta = "";

    //metodo para obtener datos del cliente
    public void DatosCliente(int idCliente) {
        Connection cn = (Connection) Conexion.conectar();
        String sql = "select * from tb_cliente where IdCliente ='" + idCliente + "'";
        Statement st;
        try {
            st = (Statement) cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                nombreCliente = rs.getString("nombre") + " " + rs.getString("apellido");
                dniCliente = rs.getString("DNI");
                telefonoCliente = rs.getString("telefono");
                direccionCliente = rs.getString("direccion");
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener datos del cliente: " + e);
        }
    }

    //metodo para generar factura
    public void generarFacturaPDF(List<DetalleVenta> listaProductos) {
        try {

            //cargar fecha actual
            Date date = new Date();
            fechaActual = new SimpleDateFormat("yyyy/MM/dd").format(date);
            //cambiar formato de fecha
            String fechaNueva = "";
            for (int i = 0; i < fechaActual.length(); i++) {
                if (fechaActual.charAt(i) == '/') {
                    fechaNueva = fechaActual.replace("/", "_");
                }
            }
            nombreArchivoPDFVenta = "Venta_" + nombreCliente + "_" + fechaNueva + ".pdf";

            Document documento = new Document();
            String ruta = System.getProperty("user.home");
            //DIRECCION DONDE VA EL REPORTE
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/"+nombreArchivoPDFVenta));

            documento.open();

            Image img = Image.getInstance(getClass().getResource("/img/LogoEmpresa.jpeg"));
            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE); //agregar una nueva linea
            fecha.add("Factura: 001" + "\nFecha: " + fechaActual + "\n\n");

            PdfPTable Encabezado = new PdfPTable(4);
            Encabezado.setWidthPercentage(100);
            Encabezado.getDefaultCell().setBorder(0);
            //tamaño de las celdas
            float[] ColumnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            Encabezado.setWidths(ColumnaEncabezado);
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);
            //agregar celdas
            Encabezado.addCell(img);

            String ruc = "20606866462";
            String nombre = "Botica Mi Hogar S.A.C";
            String telefono = "999999999";
            String direccion = "Los Olivos";
            String razon = "Estamos comprometidos con la salud de nuestros clientes, brindando productos farmacéuticos de calidad, a precios competitivos.";

            Encabezado.addCell(""); //celda vacía
            Encabezado.addCell("RUC: " + ruc + "\nNOMBRE: " + nombre + "\nTELEFONO: " + telefono + "\nDIRECCION: " + direccion + "\nRAZON SOCIAL: " + razon);
            Encabezado.addCell(fecha);
            documento.add(Encabezado);

            //CUERPO 
            Paragraph Cliente = new Paragraph();
            Cliente.add(Chunk.NEWLINE);
            Cliente.add("Datos del cliente:" + "\n\n");
            documento.add(Cliente);

            //DATOS DEL CLIENTE
            PdfPTable tablaCliente = new PdfPTable(4);
            tablaCliente.setWidthPercentage(100);
            tablaCliente.getDefaultCell().setBorder(0);
            //TAMAÑO DE LAS CELDAS
            float[] ColumnaCliente = new float[]{25f, 45f, 30f, 40f};
            tablaCliente.setWidths(ColumnaCliente);
            tablaCliente.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell cliente1 = new PdfPCell(new Phrase("DNI: ", negrita));
            PdfPCell cliente2 = new PdfPCell(new Phrase("NOMBRE: ", negrita));
            PdfPCell cliente3 = new PdfPCell(new Phrase("TELEFONO: ", negrita));
            PdfPCell cliente4 = new PdfPCell(new Phrase("DIRECCION: ", negrita));
            //quitar border
            cliente1.setBorder(0);
            cliente2.setBorder(0);
            cliente3.setBorder(0);
            cliente4.setBorder(0);
            //AGREGAR CELDAS A LAS TABLAS
            tablaCliente.addCell(cliente1);
            tablaCliente.addCell(cliente2);
            tablaCliente.addCell(cliente3);
            tablaCliente.addCell(cliente4);
            tablaCliente.addCell(dniCliente);
            tablaCliente.addCell(nombreCliente);
            tablaCliente.addCell(telefonoCliente);
            tablaCliente.addCell(direccionCliente);
            //AGREGAR AL DOCUMENTO
            documento.add(tablaCliente);
            
            //ESPACIO EN BLANCO
            Paragraph espacio = new Paragraph();
            espacio.add(Chunk.NEWLINE);
            espacio.add("");
            espacio.setAlignment(Element.ALIGN_CENTER);
            documento.add(espacio);
            
            //AGREGAR LOS PRODUCTOS
            PdfPTable tablaProductos = new PdfPTable(4);
            tablaProductos.setWidthPercentage(100);
            tablaProductos.getDefaultCell().setBorder(0);
            //tamaño de celdas
            float[] ColumnaProducto = new float[]{15f, 50f, 15f, 20f};
            tablaProductos.setWidths(ColumnaProducto);
            tablaProductos.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell producto1 = new PdfPCell(new Phrase("CANTIDAD: ", negrita));
            PdfPCell producto2 = new PdfPCell(new Phrase("DESCRIPCION: ", negrita));
            PdfPCell producto3 = new PdfPCell(new Phrase("PRECIO UNITARIO: ", negrita));
            PdfPCell producto4 = new PdfPCell(new Phrase("PRECIO TOTAL: ", negrita));
            //QUITAR BORDES
            producto1.setBorder(0);
            producto2.setBorder(0);
            producto3.setBorder(0);
            producto4.setBorder(0);
            //agregar colores
            producto1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            producto2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            producto3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            producto4.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //AGREGAR CELDA A LA TABLA
            tablaProductos.addCell(producto1);
            tablaProductos.addCell(producto2);
            tablaProductos.addCell(producto3);
            tablaProductos.addCell(producto4);
            
            for(int i=0;i<listaProductos.size();i++){
                String producto = listaProductos.get(i).getNombre();
                String cantidad = String.valueOf(listaProductos.get(i).getCantidad());
                String precio = String.valueOf(listaProductos.get(i).getPrecioUnitario());
                String total = String.valueOf(listaProductos.get(i).getTotalPagar());
                
                tablaProductos.addCell(cantidad);
                tablaProductos.addCell(producto);
                tablaProductos.addCell(precio);
                tablaProductos.addCell(total);
            }
            //AGREGAR AL DOCUMENTO
            documento.add(tablaProductos);
            
            //total a pagar
            Paragraph info = new Paragraph();
            info.add(Chunk.NEWLINE);
//            info.add("TOTAL A PAGAR: "+InterFacturacion.txt_total_pagar.getText());
            info.setAlignment(Element.ALIGN_RIGHT);
            documento.add(info);
            
            //Resultado final
            Paragraph generado = new Paragraph();
            generado.add(Chunk.NEWLINE);
            generado.add("Compra generada"+"\n");
            generado.add("Muchas gracias! 😁");
            generado.setAlignment(Element.ALIGN_CENTER);
            documento.add(generado);
            
            //CERRAR DOCUMENTO
            documento.close();
           
        } catch (DocumentException | IOException e) {
            System.out.println("Error en: " + e);
        }
    }
}

