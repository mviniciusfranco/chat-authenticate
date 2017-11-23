/*
 * To change this license header, choose License Headers dataIn Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template dataIn the editor.
 */
package chat;

import com.sun.corba.se.impl.io.IIOPOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.StringBufferInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author Marcos F
 */
public class Client {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        try {
            String message;
            InetAddress address = InetAddress.getByName("127.0.0.1");
            Socket cliente = new Socket(address, 5555);

            System.out.println("Cliente conectado");

            DataInputStream dataIn = new DataInputStream(cliente.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(cliente.getOutputStream());

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String msgin = "";
            String msgout = "";

            while (!msgin.equals("success login")) {
                System.out.print("Digite a senha: ");

                msgout = br.readLine();
                dataOut.writeUTF(msgout);
                msgin = dataIn.readUTF();
                if (!msgin.equals("success login")) {
                    System.out.println("Senha errada, repita.");
                }
                //System.dataOut.println(msgin);
            }

            System.out.println("Login feito com sucesso!");
            System.out.println("Enviando mensagens...");
            while (!msgout.equals("end")) {

                msgout = br.readLine();
                dataOut.writeUTF(msgout);
                //msgin = dataIn.readUTF();
                //System.dataOut.println(msgin);
            }
            System.out.println("Cliente encerrado");
            cliente.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
