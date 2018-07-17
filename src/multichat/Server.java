/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multichat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author Shatu
 */
public class Server {
    
    static Vector<ClientHandler> list = new Vector<>();
    static Map<String, String> registration = new HashMap<>();
    static{
    registration.put("shatu", "12345");
    registration.put("jitu", "1234");
    }
    
    static String name = "";
    
    public static void main(String[] args) throws IOException 
    {
       
        ServerSocket serverSocket = new ServerSocket(7394);
         
        Socket clientSocket;
        
        while (true) 
        {
            clientSocket = serverSocket.accept();
 
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
            
            String received = dis.readUTF();
                 
            System.out.println(received);
            String [] tokens = received.split("#");
            String type = tokens[0];
            //String name;
            if(type.equals("CM") && tokens[1].equals("login")){
                name = tokens[2]; 
                int flag = 0;
                String password = tokens[3];
                Set <String> keys = Server.registration.keySet();
                for(String key : keys){
                    if(name.equals(key)){
                        if(password.equals(Server.registration.get(key))){
                            dos.writeUTF("You are logged in!"+ '\n');
                            //ClientHandler ch = new ClientHandler(clientSocket, name, dis, dos);
                        }
                        else{
                            dos.writeUTF("Incorrect password!"+ '\n');
                            //clientSocket.close();
                        }
                        flag = 1;
                        break;
                    }
                }
                if(flag == 0){
                    dos.writeUTF("Registration complete!" + '\n');
                    Server.registration.put(name, password);
                    //ClientHandler ch = new ClientHandler(clientSocket, name, dis, dos);
                }
            }
            
            ClientHandler ch = new ClientHandler(clientSocket, name, dis, dos);
            
            Thread thread = new Thread(ch);
             
            list.add(ch);
 
            thread.start();
 
        }
    }    
}
