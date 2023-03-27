package com.mycompany.servidor;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * @author javie
 */
public class Servidor {

    public static void main(String[] args) throws IOException {
        Double dato;
        ServerSocket servidor = new ServerSocket(50000);
        try ( Socket conexion = servidor.accept()) {
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());

            byte[] a = new byte[10000 * 8];
            read(entrada, a, 0, 10000 * 8);
            ByteBuffer b = ByteBuffer.wrap(a);
            for (int i = 0; i < 10000; i++) {
                System.out.println(b.getDouble());
            }
        
            /*   for(int i = 0; i<10000; i++){
                dato = entrada.readDouble();
                System.out.println(dato);
            }
           int n = entrada.readInt();
            System.out.println(n);
            double x = entrada.readDouble();
            System.out.println(x);
            byte[] buffer = new byte[4];
            read(entrada, buffer,0,4);
            System.out.println(new String(buffer,"UTF-8"));
            System.out.println(Arrays.toString("Hola desde el servidor".getBytes()));
            salida.write("Hola".getBytes());
            byte[] a = new byte[5*8];
            read(entrada, a, 0, 5*8);
            ByteBuffer b = ByteBuffer.wrap(a);
            
            for (int i  = 0; i<5; i++){
                System.out.println(b.getDouble());
            }*/
        }
    }

    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws IOException {
        while (longitud > 0) {
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }
}
