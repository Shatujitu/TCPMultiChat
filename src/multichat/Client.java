/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multichat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author Shatu
 */
public class Client {
    public static void main(String args[]) throws UnknownHostException, IOException 
    {
        Scanner scn = new Scanner(System.in);
         
        Socket socket = new Socket("localhost", 7394);
         
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
      
        System.out.println("Input format for Login: CM#login#Name#Password");
        System.out.println("Input format for Active List: CM#ActiveList");
        System.out.println("Input format for Unicast: unicast#Message#Recevier Name");
        System.out.println("Input format for Multicast: multicast#Message#Recevier Name");
        System.out.println("Input format for Broadcast: broadcast#Message#Recevier Name" + '\n');
        
        Thread sendMessage = new Thread(new Runnable() 
        {
            @Override
            public void run() {
                while (true) {
 
                    String msg = scn.nextLine();
                     
                    try {
                        
                        dos.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
         
        Thread readMessage = new Thread(new Runnable() 
        {
            @Override
            public void run() {
 
                while (true) {
                    try {
                       
                        String msg = dis.readUTF();
                        System.out.println(msg);
                        
                    } catch (IOException e) {
 
                        e.printStackTrace();
                    }
                }
            }
        });
 
        sendMessage.start();
        readMessage.start();
    
    }
    
}
