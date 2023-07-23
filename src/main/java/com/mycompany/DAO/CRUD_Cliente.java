package com.mycompany.DAO;

import com.mycompany.modelo.Cliente;
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

public class CRUD_Cliente {

//    int idUsuario = 0;
//    InterGestionarUsuario geinusu;

    /*
    * **************************************
        MÉTODO PARA GUARDAR NUEVO CLIENTE
    * **************************************
     */
    public boolean guardar(Cliente objeto) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement("insert into tb_cliente values (?,?,?,?,?,?)");
            consulta.setInt(1, 0); //id
            consulta.setString(2, objeto.getNombre());
            consulta.setString(3, objeto.getApellido());
            consulta.setString(4, objeto.getDni());
            consulta.setString(5, objeto.getTelefono());
            consulta.setString(6, objeto.getDireccion());
//            consulta.setInt(7, objeto.getEstado());

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al guardar cliente" + e);
        }
        return respuesta;
    }


    /*
    * ************************************************************
        MÉTODO PARA CONSULTAR SI EL CLIENTE YA EXISTE EN LA BD
    * ************************************************************
     */
    public boolean existeCliente(String dni) {
        boolean respuesta = false;
        String sql = "select DNI from tb_cliente where DNI = '" + dni + "';";
        Statement st;
        try {
            Connection cn = Conexion.conectar();
            st = (Statement) cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                respuesta = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar cliente" + e);
        }
        return respuesta;
    }

    /*
    * ********************************************
            MÉTODO PARA ACTUALIZAR CLIENTE
    * ********************************************
     */
    public boolean actualizar(Cliente objeto) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement(
                    "update tb_cliente set nombre =?, apellido=?, DNI=?, telefono =?, direccion=? where IdCliente= ?;");
            consulta.setString(1, objeto.getNombre());
            consulta.setString(2, objeto.getApellido());
            consulta.setString(3, objeto.getDni());
            consulta.setString(4, objeto.getTelefono());
            consulta.setString(5, objeto.getDireccion());
//            consulta.setInt(6, objeto.getEstado());
            consulta.setInt(6, objeto.getIdCliente());
            consulta.executeUpdate();

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente" + e);
        }
        return respuesta;
    }

    /*
    * ********************************************
            MÉTODO PARA ELIMINAR CLIENTE
    * ********************************************
     */
    public void eliminar(int idCliente) {
//        boolean respuesta = false;
        Connection cn = com.mycompany.bd.Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement(
                    "delete from tb_cliente where IdCliente = ?;");
            consulta.setInt(1, idCliente); //actualizamos el parametro con el codigo
            consulta.executeUpdate(); //actualizamos y ejecutamos la consulta.
            cn.close();//cerramos la conexion.
//            consulta.executeUpdate();
//            if (consulta.executeUpdate() > 0) {
//                respuesta = true;
//            }
//            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario" + e);
        }
//        return respuesta;
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

    public Cliente buscarClientePorDNI(String clienteBuscar) {
        try {
            String sql = "select nombre, apellido from tb_cliente where DNI = '" + clienteBuscar + "'";
            Connection cn = (Connection) Conexion.conectar();
            Statement st;
            st = (Statement) cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            Cliente cliente = new Cliente();
            while (rs.next()) {
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
            }
            return cliente;
        } catch (SQLException e) {
            System.out.println("!Error al buscar cliente!, " + e);
        }
        return null;
    }

    public void CargarTablaCliente(JTable tabla) {
        Connection cn = Conexion.conectar();
        String titulos[] = {"N°", "nombre", "apellido", "DNI", "telefono", "dirección"};
        DefaultTableModel model = new DefaultTableModel(null, titulos);
        tabla.setModel(model);

        Statement st;

        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from tb_cliente");

            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setIdCliente(rs.getInt(1));
                cli.setNombre(rs.getString(2));
                cli.setApellido(rs.getString(3));
                cli.setDni(rs.getString(4));
                cli.setTelefono(rs.getString(5));
                cli.setDireccion(rs.getString(6));
//                cli.setEstado(rs.getInt(7));
                model.addRow(cli.GestionarCliente());
            }
            cn.close();

        } catch (SQLException e) {
            System.out.println("Error al llenar la tabla de cliente:" + e);
        }
    }
    
    public List<Cliente> obtenerClientes(){
        Connection cn = Conexion.conectar();
        Statement st;
        List<Cliente> clientes = new ArrayList();
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from tb_cliente");
            
            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setIdCliente(rs.getInt(1));
                cli.setNombre(rs.getString(2));
                cli.setApellido(rs.getString(3));
                cli.setDni(rs.getString(4));
                cli.setTelefono(rs.getString(5));
                cli.setDireccion(rs.getString(6));
                clientes.add(cli);
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener los clientes: " + e);
        }
        System.out.println(clientes.size());
        return clientes;
    }

    public Cliente EnviarDatosClienteSeleccionado(int idCliente) {
        Cliente cli = null; //vacio
        Connection cn = Conexion.conectar();
//        String titulos[]={"N°","nombre","apellido","usuario","password","telefono","estado"};
//        DefaultTableModel model = new DefaultTableModel(null,titulos);
//        tabla.setModel(model);

        Statement st;

        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from tb_cliente where IdCliente = " + idCliente);

            while (rs.next()) {
                cli = new Cliente();
                cli.setIdCliente(rs.getInt(1));
                cli.setNombre(rs.getString(2));
                cli.setApellido(rs.getString(3));
                cli.setDni(rs.getString(4));
                cli.setDireccion(rs.getString(5));
                cli.setTelefono(rs.getString(6));
                //usu.setEstado(rs.getInt(7));
//                model.addRow(usu.GestionarUsuario());
            }
            cn.close();

        } catch (SQLException e) {
            System.out.println("Error al llenar la tabla de usuarios:" + e);
        }
        return cli;
    }

    public int obtenerIdClientePorNombres(String nombres) {
        try {
            String sql = "select * from tb_cliente where concat(nombre, ' ', apellido) = '" + nombres + "'";
            Connection cn = Conexion.conectar();
            Statement st;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt("IdCliente");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener id del cliente, " + e);
        }
        return 0;
    }

}
