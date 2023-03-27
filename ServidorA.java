/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;

/**
 *
 * @author javie
 */
public class ServidorA {

    public static class ReceptorMulticast extends Thread {

        MulticastSocket socket;

        public ReceptorMulticast(MulticastSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            while (true) {
                try {
                    /*    NetworkInterface interfaz = null;
                    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                    while (interfaces.hasMoreElements()) {
                        interfaz = interfaces.nextElement();
                        if (!interfaz.isLoopback() && interfaz.isUp()) {
                            break;
                        }
                    }
                    InetAddress direccionLocal = interfaz.getInetAddresses().nextElement();

                    byte[] buffer = new byte[1024];
                    DatagramPacket pack = new DatagramPacket(buffer, buffer.length);

                    socket.receive(pack);
                    InetAddress direccionOrigen = pack.getAddress();*/
                    //          if (!direccionLocal.equals(direccionOrigen)) {
                    byte[] mensaje = recibe_mensaje(socket, 1024);
                    String mensaje_parseado = new String(mensaje, "ISO-8859-1");
                    String[] separar_mensaje = mensaje_parseado.split(":");
                    System.out.println(separar_mensaje[0] + "-----------> " + separar_mensaje[1]);
                    System.out.println("Escribir Mensaje:");
                    //           }

                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(ServidorA.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

    public static void main(String[] args) throws IOException {
        // Creamos un MulticastSocket en el puerto y dirección multicast deseada
        String nombre = args[0];
        try ( MulticastSocket socket = new MulticastSocket(5000)) {
            InetSocketAddress grupo = new InetSocketAddress(InetAddress.getByName("239.0.0.0"), 5000);
            NetworkInterface netInter = NetworkInterface.getByName("em1");
            socket.joinGroup(grupo, netInter); // nos unimos al grupo multicast
            Scanner entrada = new Scanner(System.in);
            ReceptorMulticast w = new ReceptorMulticast(socket);

            w.start();

            while (true) {
                System.out.println("Escribir Mensaje: ");
                String mensaje = entrada.nextLine();
                DatagramPacket paquete = new DatagramPacket((nombre + ":" + mensaje).getBytes("ISO-8859-1"), (nombre + ":" + mensaje).getBytes("ISO-8859-1").length, grupo);
                socket.send(paquete);
            }
            // socket.leaveGroup(grupo, netInter);
        }

    }

    static byte[] recibe_mensaje(MulticastSocket socket, int longitud_mensaje) throws IOException {
        int maxBufferSize = 2048;
        byte[] mensajeCompleto = new byte[0];
        byte[] buffer = new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquete);

        // Almacenar el primer fragmento del mensaje
        mensajeCompleto = Arrays.copyOfRange(paquete.getData(), 0, paquete.getLength());

        // Recibir fragmentos adicionales hasta que se haya recibido el mensaje completo
        while (paquete.getLength() == maxBufferSize) {
            // Recibir un nuevo paquete
            socket.receive(paquete);

            // Almacenar el fragmento del mensaje recibido
            mensajeCompleto = Arrays.copyOf(mensajeCompleto, mensajeCompleto.length + paquete.getLength());
            System.arraycopy(paquete.getData(), 0, mensajeCompleto, mensajeCompleto.length - paquete.getLength(), paquete.getLength());
        }
        // Se ha recibido el mensaje completo
        return mensajeCompleto;
    }

    static void envia_mensaje(byte[] buffer, String ip, int puerto) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress grupo = InetAddress.getByName(ip);
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, grupo, puerto);
        socket.send(paquete);
        socket.close();
    }

    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception {
        while (longitud > 0) {
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }
}
