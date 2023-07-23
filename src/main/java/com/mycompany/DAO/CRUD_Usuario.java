package com.mycompany.DAO;

import com.mycompany.modelo.Usuario;
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

public class CRUD_Usuario {

//    int idUsuario = 0;
//    InterGestionarUsuario geinusu;

    /*
    * **************************************
        MÉTODO PARA GUARDAR NUEVO USUARIO
    * **************************************
     */
    public boolean guardar(Usuario objeto) {
        boolean respuesta = false;
        Connection cn = com.mycompany.bd.Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement("insert into tb_usuario values (?,?,?,?,?,?,?,?)");
            consulta.setInt(1, 0); //id
            consulta.setString(2, objeto.getNombre());
            consulta.setString(3, objeto.getApellido());
            consulta.setString(4, objeto.getUsuario());
            consulta.setString(5, objeto.getPassword());
            consulta.setString(6, objeto.getTelefono());
            consulta.setString(7, objeto.getCorreo());
            consulta.setString(8, objeto.getEstado());

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al guardar usuario" + e);
        }
        return respuesta;
    }

    /*
    * ************************************************************
        MÉTODO PARA CONSULTAR SI EL PRODUCTO YA EXISTE EN LA BD
    * ************************************************************
     */
    public boolean existeUsuario(String usuario) {
        boolean respuesta = false;
        String sql = "select usuario from tb_usuario where usuario = '" + usuario + "';";
        Statement st;
        try {
            Connection cn = Conexion.conectar();
            st = (Statement) cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                respuesta = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar usuario" + e);
        }
        return respuesta;
    }

    //metodo iniciar sesion
    public int loginUser(Usuario objeto) {

        Connection cn = Conexion.conectar();
        String sql = "select IdUsuario from tb_usuario where usuario = '" + objeto.getUsuario() + "' and password= '" + objeto.getPassword() + "'";
        Statement st;
        int idUsuario = -1;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                idUsuario = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error al iniciar sesion: "+e);
        }
        return idUsuario;
    }

    public Usuario RecuperacionCuenta(String nombreUsuario) {

        Connection cn = Conexion.conectar();
        String sql = "select correo, password from tb_usuario where usuario = '" + nombreUsuario + "'";
        Statement st;
        Usuario user = new Usuario();
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                user.setCorreo(rs.getString(1));
                user.setPassword(rs.getString(2));
            }

        } catch (SQLException e) {
            System.out.println("Error al recuperar contrasena");
        }
        return user;
    }

    /*
    * ********************************************
            MÉTODO PARA ACTUALIZAR USUARIO
    * ********************************************
     */
    public boolean actualizar(Usuario objeto) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement(
                    "update tb_usuario set nombre =?, apellido=?, usuario=?, password =?, telefono=?, correo=? where IdUsuario= ?;");

            consulta.setString(1, objeto.getNombre());
            consulta.setString(2, objeto.getApellido());
            consulta.setString(3, objeto.getUsuario());
            consulta.setString(4, objeto.getPassword());
            consulta.setString(5, objeto.getTelefono());
            consulta.setString(6, objeto.getCorreo());
//            consulta.setString(7, objeto.getEstado());
            consulta.setInt(7, objeto.getIdUsuario());
            consulta.executeUpdate();

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario" + e);
        }
        return respuesta;
    }

    /*
    * ********************************************
            MÉTODO PARA ELIMINAR USUARIO
    * ********************************************
     */
    public void eliminar(int idUsuario) {
//        boolean respuesta = false;
        Connection cn = com.mycompany.bd.Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement(
                    "delete from tb_usuario where IdUsuario = ?;");
            consulta.setInt(1, idUsuario); //actualizamos el parametro con el codigo
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

    public void CargarTablaUsuarios(JTable tabla) {
        Connection cn = Conexion.conectar();
        String titulos[] = {"N°", "nombre", "apellido", "usuario", "password", "telefono", "correo", "estado"};
        DefaultTableModel model = new DefaultTableModel(null, titulos);
        tabla.setModel(model);

        Statement st;

        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from tb_usuario");

            while (rs.next()) {
                Usuario usu = new Usuario();
                usu.setIdUsuario(rs.getInt(1));
                usu.setNombre(rs.getString(2));
                usu.setApellido(rs.getString(3));
                usu.setUsuario(rs.getString(4));
                usu.setPassword(rs.getString(5));
                usu.setTelefono(rs.getString(6));
                usu.setCorreo(rs.getString(7));
                usu.setEstado(rs.getString(8));
                model.addRow(usu.GestionarUsuario());
            }
            cn.close();

        } catch (SQLException e) {
            System.out.println("Error al llenar la tabla de usuarios:" + e);
        }

    }

    public boolean esUsuarioAdmin(int idUsuario) {
        Connection cn = Conexion.conectar();
        Statement st;
         try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery("select estado from tb_usuario where IdUsuario = " + idUsuario);

            if (rs.next()) {
                String estado = rs.getString(1);
                System.out.println(estado.equals("Administrador"));
                return estado.equals("Administrador");
            }
            cn.close();

        } catch (SQLException e) {
            System.out.println("Error al llenar la tabla de usuarios:" + e);
        }
         return false;
    }

    public Usuario EnviarDatosUsuarioSeleccionado(int idUsuario) {
        Usuario usu = null; //vacio
        Connection cn = Conexion.conectar();
//        String titulos[]={"N°","nombre","apellido","usuario","password","telefono","estado"};
//        DefaultTableModel model = new DefaultTableModel(null,titulos);
//        tabla.setModel(model);

        Statement st;

        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from tb_usuario where IdUsuario = " + idUsuario);

            while (rs.next()) {
                usu = new Usuario();
                usu.setIdUsuario(rs.getInt(1));
                usu.setNombre(rs.getString(2));
                usu.setApellido(rs.getString(3));
                usu.setUsuario(rs.getString(4));
                usu.setPassword(rs.getString(5));
                usu.setTelefono(rs.getString(6));
                usu.setCorreo(rs.getString(7));
//                usu.setEstado(rs.getString(8));
//                model.addRow(usu.GestionarUsuario());
            }
            cn.close();

        } catch (SQLException e) {
            System.out.println("Error al llenar la tabla de usuarios:" + e);
        }
        return usu;
    }

}
