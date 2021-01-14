/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import RSA.RSA;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;
import java.math.*;
import java.net.*;

/**
 *
 * @author estre
 */
public class Cliente {

    public static void main(String[] args) {
        MarcoCliente mimarco = new MarcoCliente();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class MarcoCliente extends JFrame {

    public MarcoCliente() {
        setBounds(600, 300, 280, 350);
        LaminaMarcoCliente milamina = new LaminaMarcoCliente();
        add(milamina);
        setVisible(true);
    }
}

class LaminaMarcoCliente extends JPanel implements Runnable {

    public LaminaMarcoCliente() {
        nick = new JTextField(5);
        add(nick);

        JLabel texto = new JLabel("-CHAT-");
        add(texto);

        ip = new JTextField(8);
        add(ip);
        campochat = new JTextArea(12, 20);
        add(campochat);
        campo1 = new JTextField(20);
        add(campo1);
        EnviaTexto mievento = new EnviaTexto();
        miboton = new JButton("Enviar");
        miboton.addActionListener(mievento);
        add(miboton);
        Thread mihilo = new Thread(this);
        mihilo.start();

    }

    @Override
    public void run() {
        try {
            ServerSocket servidor_cliente = new ServerSocket(9090);
            Socket cliente;
            PaqueteEnvio paqueteRecibido;

            RSA rsa = new RSA(1024);

            while (true) {
                cliente = servidor_cliente.accept();
                ObjectInputStream flujoEntrada = new ObjectInputStream(cliente.getInputStream());
                paqueteRecibido = (PaqueteEnvio) flujoEntrada.readObject();

                rsa.setD(paqueteRecibido.getD());
                rsa.setN(paqueteRecibido.getN());
                String recuperarTextoPlano = rsa.descencripta(paqueteRecibido.getMensaje());

                campochat.append("\n" + paqueteRecibido.getNick() + ": " + recuperarTextoPlano);

            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    private class EnviaTexto implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            campochat.append("\n" + nick.getText() + ": " + campo1.getText());

            RSA rsa = new RSA(1024);
            rsa.generaPrimos();
            rsa.generaClaves();

            BigInteger[] textoCifrado = rsa.encripta(campo1.getText());
            
            try {
                Socket misocket = new Socket("127.0.0.1", 9999);
                PaqueteEnvio datos = new PaqueteEnvio();

                datos.setNick(nick.getText());
                datos.setIp(ip.getText());
                datos.setMensaje(textoCifrado);
                datos.setD(rsa.damed());
                datos.setN(rsa.damen());

                ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());
                paquete_datos.writeObject(datos);
                misocket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private final JTextField campo1;
    private final JTextField nick;
    private final JTextField ip;
    private final JTextArea campochat;
    private final JButton miboton;

}

class PaqueteEnvio implements Serializable {

    private String nick, ip;
    private BigInteger n, d;
    private BigInteger[] mensaje;

    public BigInteger getN() {
        return n;
    }

    public void setN(BigInteger n) {
        this.n = n;
    }

    public BigInteger getD() {
        return d;
    }

    public void setD(BigInteger d) {
        this.d = d;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public BigInteger[] getMensaje() {
        return mensaje;
    }

    public void setMensaje(BigInteger[] mensaje) {
        this.mensaje = mensaje;
    }

}
