/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Marcos F
 */
public class Server {

    static int port = 5555;

    public static void main(String[] args) throws IOException {
        try {

            ServerSocket server = new ServerSocket(port);
            System.out.println("Servidor iniciado ");


            Socket cliente = server.accept();
            System.out.println("Cliente conectado: " + cliente.getInetAddress());

            DataInputStream in = new DataInputStream(cliente.getInputStream());
            DataOutputStream out = new DataOutputStream(cliente.getOutputStream());

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String msgin = "";
            String msgout = "";

            FileInputStream fl = new FileInputStream("senhas.txt");
            
            FileReader fileReader = new FileReader("senhas.txt");
            
            BufferedReader brFileReader = new BufferedReader(new InputStreamReader(fl));
            String line = brFileReader.readLine();

            while (true) {
                System.out.println(line);
                if (line == null) {
                    System.out.println("Senha digitada incorreta");
                    fl.getChannel().position(0);
                    brFileReader = new BufferedReader(new InputStreamReader(fl));
                    out.writeUTF("Senha incorreta");
                    out.flush();
                    line = brFileReader.readLine();

                }

                msgin = in.readUTF();
                
                if (line.equals(msgin)) {
                    System.out.println("Login do cliente feito com sucesso");
                    out.writeUTF("success login");
                    out.flush();
                    break;
                }

                line = brFileReader.readLine();
            }

//            while (!msgin.equals("senha")) {
//                msgin = in.readUTF();
//                //msgout = br.readLine();
//                //out.writeUTF(msgout);
//                //out.flush();
//            }


            while (!msgin.equals("end")) {
                //recebe mensagens

                msgin = in.readUTF();
                if (!msgin.equals("end")) {
                    System.out.println(msgin);
                }
                //msgout = br.readLine();
                //out.writeUTF(msgout);
                //out.flush();
            }
            out.writeUTF("end");
            out.flush();
            cliente.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
