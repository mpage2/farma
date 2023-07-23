package com.mycompany.DAO;

import com.mycompany.modelo.DetalleVenta;
import com.mycompany.modelo.CabeceraVenta;
import java.sql.Connection;
import com.mycompany.bd.Conexion;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CRUD_RegistrarVenta {
    public static int idCabeceraRegistrada;
    java.math.BigDecimal iDColVar;

//    int idUsuario = 0;
//    InterGestionarUsuario geinusu;

    /*
    * **************************************
        MÉTODO PARA GUARDAR LA CABECERA DE VENTA
    * **************************************
     */
    public boolean guardar(CabeceraVenta objeto) {
        boolean respuesta = false;
        Connection cn = com.mycompany.bd.Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement("insert into tb_Cabecera_venta values (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            consulta.setInt(1, 0); //id
            consulta.setInt(2, objeto.getIdCliente());
            consulta.setDouble(3, objeto.getValorPagar());
            consulta.setString(4, objeto.getFechaVenta());
            consulta.setInt(5, objeto.getEstado());
            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            
            ResultSet rs = consulta.getGeneratedKeys();
            while(rs.next()){
                iDColVar= rs.getBigDecimal(1);
                this.idCabeceraRegistrada = iDColVar.intValue();
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al guardar cabecera de venta" + e);
        }
        return respuesta;
    }
    
    /*
    * ***************************************
        MÉTODO PARA GUARDAR DETALLE DE VENTA
    * ****************************************
     */
    public boolean guardarDetalle(DetalleVenta objeto) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement("insert into tb_detalle_venta (IdCabeceraVenta,IdProducto,cantidad,precioUnitario,subtotal,descuento,igv,totalPagar,estado) values (?,?,?,?,?,?,?,?,?)");
            consulta.setInt(1, this.idCabeceraRegistrada);
            consulta.setInt(2, objeto.getIdProducto());
            consulta.setInt(3, objeto.getCantidad());
            consulta.setDouble(4, objeto.getPrecioUnitario());
            consulta.setDouble(5, objeto.getSubtotal());
            consulta.setDouble(6, objeto.getDescuento());
            consulta.setDouble(7, objeto.getIgv());
            consulta.setDouble(8, objeto.getTotalPagar());
            consulta.setInt(9, objeto.getEstado());

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
                
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al guardar detalle de venta" + e);
        }
        return respuesta;
    }

    
    
    
   public int obtenerIdClientePorCliente(String idcliente) {
        try {
            String sql = "select IdCliente, concat(nombre,' ', apellido) as cliente from tb_cliente "
                            + "where concat(nombre,' ', apellido) = '" + idcliente + "'";
            Connection cn = Conexion.conectar();
            Statement st;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt("IdCliente");
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar id del cliente, " + e);
        }
        return 0;
    }
    
    

    /*
    * ********************************************
            MÉTODO PARA ACTUALIZAR PRODUCTO
    * ********************************************
     */
    public boolean actualizar(CabeceraVenta objeto) {
        boolean respuesta = false;
        java.sql.Connection cn = Conexion.conectar();
        System.out.println(objeto.getIdCliente());
        System.out.println(objeto.getEstado());
        System.out.println(objeto.getIdCabeceraventa());
        try {
            java.sql.PreparedStatement consulta = cn.prepareStatement(
                    "update tb_Cabecera_venta set IdCliente =? , estado = ? "
                            + "where IdCabeceraVenta= ?;");
            
            
            
            consulta.setInt(1, objeto.getIdCliente());
            consulta.setInt(2, objeto.getEstado());
            
            consulta.setInt(3, objeto.getIdCabeceraventa());
            
            consulta.executeUpdate();
        

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al actualizar cabecera de venta" + e);
        }
        return respuesta;
    }
 
    public List<CabeceraVenta> CargarTablaVentas() {
        Connection cn = Conexion.conectar();
        
        Statement st;

        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery("select cv.IdCabeceraVenta as id, concat(c.nombre, ' ', c.apellido) as cliente, "
                    + "cv.valorPagar as total, cv.fechaVenta as fecha, cv.estado from tb_Cabecera_venta as cv, tb_cliente as c where cv.IdCliente = c.IdCliente");
           
            List<CabeceraVenta> datosTabla = new ArrayList();

            while (rs.next()) {
                CabeceraVenta ven = new CabeceraVenta();
                ven.setIdCabeceraventa(rs.getInt(1));
                ven.setNombresCliente(rs.getString(2));
                ven.setValorPagar(rs.getDouble(3));
                ven.setFechaVenta(rs.getString(4));
                ven.setEstado(rs.getInt(5));
               
                datosTabla.add(ven);
            }
            cn.close();
            return datosTabla;


        } catch (SQLException e) {
            System.out.println("Error al llenar la tabla de ventas:" + e);
            return new ArrayList();
        }

    }
public CabeceraVenta EnviarDatosVentaSeleccionada(int idVenta) {
        CabeceraVenta cabeceraVenta = null; //vacio
        Connection cn = Conexion.conectar();
        Statement st;

        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery("select cv.IdCabeceraVenta, cv.IdCliente, concat(c.nombre,' ', c.apellido) as cliente, "
                    + "cv.valorPagar, cv.fechaVenta, cv.estado from tb_Cabecera_venta as cv, tb_cliente as c "
                    + "where cv.IdCliente = c.IdCliente and cv.IdCabeceraVenta = " + idVenta );

            if (rs.next()) {
                cabeceraVenta = new CabeceraVenta();
                cabeceraVenta.setIdCabeceraventa(rs.getInt(1));
                cabeceraVenta.setIdCliente(rs.getInt(2));
                cabeceraVenta.setNombresCliente(rs.getString(3));
                cabeceraVenta.setValorPagar(rs.getDouble(4));
                cabeceraVenta.setFechaVenta(rs.getString(5));
                cabeceraVenta.setEstado(rs.getInt(6));
           
            }
            cn.close();

        } catch (SQLException e) {
            System.out.println("Error al llenar la tabla de ventas:" + e);
        }
        return cabeceraVenta;
    }
/**
     * *************************************
     * MÉTODO PARA CARGAR CLIENTE *************************************
     */
    public List<String> CargarComboCliente() {
        Connection cn = (Connection) Conexion.conectar();
        Statement st;
        try {
            st = (Statement) cn.createStatement();
            ResultSet rs = st.executeQuery("select * from tb_cliente");
            List<String> listaCliente = new ArrayList<>();
            while (rs.next()) {
                listaCliente.add(rs.getString("nombre") + " " + rs.getString("apellido"));
            }
            return listaCliente;
        } catch (SQLException ex) {
            System.out.println("Error al cargar cliente" + ex);

        }
        return null;
    }
 

}
