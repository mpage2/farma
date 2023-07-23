package com.mycompany.Principal;
//libreria

import com.mycompany.vista.FrmMenu;
import com.mycompany.vista.InterActualizarStock;
import com.mycompany.vista.InterGestionarVenta;
import com.mycompany.vista.InterGestionarCliente;
import com.mycompany.vista.InterCliente;
import com.mycompany.vista.InterGestionarUsuario;
import com.mycompany.vista.FrmRecuperar;
import com.mycompany.vista.InterGestionarCategoria;
import com.mycompany.vista.InterCategoria;
import com.mycompany.vista.InterFacturacion;
import com.mycompany.vista.InterProducto;
import com.mycompany.vista.InterGestionarProducto;
import com.mycompany.vista.InterUsuario;
import com.mycompany.vista.FrmLogin;
import com.mycompany.Controlador.*;
//import com.formdev.flatlaf.*;
import com.mycompany.DAO.Reportes;
public class Main {
 
    public static FrmLogin ini;
    public static Ctrl_Inicio cini;
    
    public static FrmRecuperar rec;
    public static Ctrl_Recuperar crec;

    public static Ctrl_Menu cmenu;
    public static FrmMenu menu;
    
    public static Ctrl_Usuario cinusu;
    public static InterUsuario inusu;
    public static Ctrl_GestionarUsuario cgeinusu;
    public static InterGestionarUsuario geinusu;
    
    public static Ctrl_Producto cinpro;
    public static InterProducto inpro;
    public static Ctrl_GestionarProducto cgeinpro;
    public static InterGestionarProducto geinpro;
    public static Ctrl_ActualizarStock cactstock;
    public static InterActualizarStock actstock;
    
    public static Ctrl_Cliente cincli;
    public static InterCliente incli;
    public static Ctrl_GestionarCliente cgeincli;
    public static InterGestionarCliente geincli;
    
    
    
    public static Ctrl_Categoria cincat;
    public static InterCategoria incat;
    public static Ctrl_GestionarCategoria cgeincat;
    public static InterGestionarCategoria geincat;
    
    public static Ctrl_RegistrarVenta cinven;
    public static InterFacturacion inven;
    public static Ctrl_GestionarVenta cgeinven;
    public static InterGestionarVenta geinven;
    
    public static Reportes rep;
    
    
    public static void main(String[] args) { 
//        Light.setup();
        ini = new FrmLogin();
        ini.setVisible(true);
        cini =  new Ctrl_Inicio(ini);
  
        
    }
    
}