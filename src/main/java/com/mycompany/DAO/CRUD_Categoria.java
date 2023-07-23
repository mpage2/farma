/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.DAO;
import com.mycompany.modelo.Categoria;
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
public class CRUD_Categoria {
    

    public boolean guardar(Categoria objeto) {
        boolean respuesta = false;
        Connection cn = com.mycompany.bd.Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement("insert into tb_categoria values (?,?)");
            consulta.setInt(1, 0);
            consulta.setString(2, objeto.getDescripcion());
//            consulta.setInt(3, objeto.getEstado());
            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al guardar categoría" + e);
        }
        return respuesta;
    }

    public boolean existeCategoria(String categoria) {
        boolean respuesta = false;
        String sql = "select descripcion from tb_categoria where descripcion = '" + categoria + "';";
        Statement st;

        try {
            Connection cn = Conexion.conectar();
            st = (Statement) cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                respuesta = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar categoría" + e);
        }
        return respuesta;
    }
    
    public boolean actualizar(Categoria objeto) {
        boolean respuesta = false;
        Connection cn = com.mycompany.bd.Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement("update tb_categoria set descripcion=? where idCategoria= ?;");
//            consulta.setString(1, objeto.getDescripcion());
            
            consulta.setString(1, objeto.getDescripcion());
//            consulta.setInt(2, objeto.getEstado());
            consulta.setInt(2, objeto.getIdCategoria());
            consulta.executeUpdate();
            
            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al actualizar categoría" + e);
        }
        return respuesta;
    }
    public void eliminar(int idCategoria) {
//        boolean respuesta = false;
        Connection cn = com.mycompany.bd.Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement(
                    "delete from tb_categoria where idCategoria= ?;");
            consulta.setInt(1, idCategoria);
            consulta.executeUpdate();
//            
//            if (consulta.executeUpdate() > 0) {
//                respuesta = true;
//            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al eliminar categoría" + e);
        }
//        return respuesta;
    }
    /*
    *
    *metodo para mostrar  todas las categorias registradas
     */
    public void CargarTablaCategorias(JTable tabla) {
        Connection cn = Conexion.conectar();
        String titulos[]={"N°","descripcion"};
        DefaultTableModel model = new DefaultTableModel(null,titulos);
        tabla.setModel(model);
        
        Statement st;

        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery("select idCategoria, descripcion from tb_categoria");
           

            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setIdCategoria(rs.getInt(1));
                cat.setDescripcion(rs.getString(2));
//                cat.setEstado(rs.getInt(3));
                model.addRow(cat.GestionarCategoria());
            }
            cn.close();

        } catch (SQLException e) {
            System.out.println("Error al llenar la tabla de categoria:" + e);
        }

    }
    public Categoria EnviarDatosCategoriaSeleccionada(int idCategoria) {
        Categoria cat = null; //vacio
        Connection cn = Conexion.conectar();        
        Statement st;

        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from tb_categoria where IdCategoria = " + idCategoria);
           

            while (rs.next()) {
                cat = new Categoria();
                cat.setIdCategoria(rs.getInt(1));
                cat.setDescripcion(rs.getString(2));

            }
            cn.close();

        } catch (SQLException e) {
            System.out.println("Error al llenar la tabla de usuarios:" + e);
        }
        return cat;
    }
    
    
    
}
