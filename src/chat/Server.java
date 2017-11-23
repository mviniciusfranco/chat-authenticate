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
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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

                int length = in.readInt();

                byte[] message = new byte[length];
                
                if (length > 0) {
                    message = new byte[length];
                    in.readFully(message, 0, message.length); // read the message
                }

                //msgin = in.readUTF();
                String decodedtext = new TripleDESTest().decrypt(message);

                

                if (!decodedtext.equals("end")) {
                    System.out.println(decodedtext);
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

    public String decrypt(byte[] message) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digestOfPassword = md.digest("HG58YZ3CR9"
                .getBytes("utf-8"));
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8;) {
            keyBytes[k++] = keyBytes[j++];
        }

        final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        decipher.init(Cipher.DECRYPT_MODE, key, iv);

        // final byte[] encData = new
        // sun.misc.BASE64Decoder().decodeBuffer(message);
        final byte[] plainText = decipher.doFinal(message);

        return new String(plainText, "UTF-8");
    }
}
