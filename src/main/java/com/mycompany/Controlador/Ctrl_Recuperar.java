package com.mycompany.Controlador;

import com.mycompany.vista.FrmRecuperar;
import com.mycompany.modelo.Usuario;
import com.mycompany.DAO.CRUD_Usuario;
import com.mycompany.Principal.Main;
import static com.mycompany.Principal.Main.geincat;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Ctrl_Recuperar implements ActionListener {

    CRUD_Usuario crudUsu;

    FrmRecuperar recuperar;
    private static final String Algoritmo = "AES";
    String original_valor = "mi_clave_oculta";
    // Variables para el envio de correo
    private static String remitente = "huamanisabel93@gmail.com";
    private static String clave_remitente = "pwurdmqfvgjxrati";
    public static String destinatario;
    private String emailTo;
    private String titulo;
    private String contenido;
    String clave;
    private Properties pro;
    private Session session;
    private MimeMessage correo;

    public Ctrl_Recuperar(FrmRecuperar rec) {
        recuperar = rec;
        recuperar.jButton_enviar.addActionListener(this);
        Iniciar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == recuperar.jButton_enviar) {
            crudUsu = new CRUD_Usuario();
            String nombreUsuario = recuperar.txt_recUsuario.getText().trim();
            System.out.println(nombreUsuario);
//            usuario.setCorreo(recuperar.txt_recUsuario.getText().trim());
            Usuario usuario = crudUsu.RecuperacionCuenta(nombreUsuario);
            if (usuario!=null) {
                destinatario = usuario.getCorreo();
                emailTo = destinatario;
                clave = usuario.getPassword();
                titulo = "Solicitud de Recuperacion de clave";
                
                contenido = "Tu contraseña es: " + clave;

                //SMTP
                pro.put("mail.smtp.host", "smtp.gmail.com");
                pro.put("mail.smtp.ssl.trust", "smtp.gmail.com");
                pro.setProperty("mail.smtp.starttls.enable", "true");
                pro.setProperty("mail.smtp.port", "587");
                pro.setProperty("mail.smtp.user", remitente);
                pro.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
                pro.setProperty("mail.smtp.auth", "true");

                session = Session.getDefaultInstance(pro);
                try {
                    correo = new MimeMessage(session);
                    correo.setFrom(new InternetAddress(remitente));
                    correo.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
                    correo.setSubject(titulo);
                    correo.setText(contenido, "ISO-8859-1", "html");
                } catch (Exception ex) {
                    ex.printStackTrace();
//                return false;
                }

                Send();

            } else {
                JOptionPane.showMessageDialog(null, "No existe el usuario");
            }
        }

    }

    public void Iniciar() {
        pro = new Properties();
    }

    public void Send() {
        try {
            Transport transport = session.getTransport("smtp");
            transport.connect(remitente, clave_remitente);
            transport.sendMessage(correo, correo.getRecipients(Message.RecipientType.TO));
            transport.close();
            recuperar.txtenvio.setText(destinatario);
            JOptionPane.showMessageDialog(null, "Correo Enviado");

        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error al enviar el correo");
        }
    }
}
