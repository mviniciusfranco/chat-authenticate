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
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author Marcos F
 */
public class Client {

    public byte[] encrypt(String message) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digestOfPassword = md.digest("HG58YZ3CR9"
                .getBytes("utf-8"));
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8;) {
            keyBytes[k++] = keyBytes[j++];
        }

        final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        final byte[] plainTextBytes = message.getBytes("utf-8");
        final byte[] cipherText = cipher.doFinal(plainTextBytes);
        // final String encodedCipherText = new sun.misc.BASE64Encoder()
        // .encode(cipherText);

        return cipherText;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        BufferedReader str = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            System.out.println("Digite o IP do servidor");
            String message;
            message = str.readLine();
            InetAddress address = InetAddress.getByName(message);
            
            System.out.println("Digite a porta do servidor");
            Integer porta = Integer.parseInt(str.readLine());
            
            Socket cliente = new Socket(address, porta);

            System.out.println("Cliente conectado");

            DataInputStream dataIn = new DataInputStream(cliente.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(cliente.getOutputStream());

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String msgin = "";
            String msgout = "";

            while (!msgin.equals(
                    "success login")) {
                System.out.print("Digite a senha: ");

                msgout = br.readLine();
                dataOut.writeUTF(msgout);
                msgin = dataIn.readUTF();
                if (!msgin.equals("success login")) {
                    System.out.println("Senha errada, repita.");
                }
                //System.dataOut.println(msgin);
            }

            System.out.println(
                    "Login feito com sucesso!");
            System.out.println(
                    "Enviando mensagens...");
            
            while (!msgout.equals(
                    "end")) {

                msgout = br.readLine();
                byte[] codedtext = new TripleDESTest().encrypt(msgout);
                
                dataOut.writeInt(codedtext.length);
                dataOut.write(codedtext);
                
                //descomentar para basico
                //dataOut.writeUTF(msgout);
                
                //msgin = dataIn.readUTF();
                //System.dataOut.println(msgin);
            }

            System.out.println(
                    "Cliente encerrado");
            cliente.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
