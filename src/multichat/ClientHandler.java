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
import java.util.Scanner;

/**
 *
 * @author Shatu
 */
class ClientHandler implements Runnable 
{
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket clientSocket;
    boolean isloggedin;
     
    // constructor
    public ClientHandler(Socket clientSocket, String name, DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.clientSocket = clientSocket;
        this.isloggedin=true;
    }
 
    @Override
    public void run() {
 
        String received;
        while (true) 
        {
            try
            {
          
                received = dis.readUTF();
                System.out.println(received);
                 
                if(received.equals("logout")){
                    this.isloggedin=false;
                    //this.clienySocket.close();   
                    break;
                }
                 
                String [] tokens = received.split("#");
                String type = tokens[0];
                
                if(type.equals("unicast")){
                    String MsgToSend = tokens[1];
                    String recipient = tokens[2];
                    for (ClientHandler mc : Server.list) 
                    {                       
                        if (mc.name.equals(recipient) && mc.isloggedin==true) 
                        {
                            mc.dos.writeUTF(this.name+" : "+MsgToSend);
                            break;
                        }  
                    }
                }
                else if(type.equals("multicast")){
                    String MsgToSend = tokens[1];
                    int count = 0;
                    for(int i=0; i<received.length(); i++){
                        char c = received.charAt(i);
                        if(c == '#'){
                            count++;
                        }
                    }
                    for(int i=2; i<=count; i++)
                    {
                        String recipient = tokens[i];
                        for (ClientHandler mc : Server.list){
                            
                            if (mc.name.equals(recipient) && mc.isloggedin==true) 
                            {
                                mc.dos.writeUTF(this.name+" : "+MsgToSend);
                            }
                        }
                    }
                }
                else if(type.equals("broadcast")){
                    String MsgToSend = tokens[1];
                    for (ClientHandler mc : Server.list){
                            
                        if (mc.isloggedin==true) 
                        {
                            mc.dos.writeUTF(this.name+" : "+MsgToSend);
                        }
                    }
                }
                else if(type.equals("CM") && tokens[1].equals("ActiveList")){
                    for (ClientHandler mc : Server.list){     
                        if (mc.isloggedin==true) 
                        {
                            dos.writeUTF(mc.name);
                        }
                    }
                }
                /*else if(type.equals("CM") && tokens[1].equals("login")){
                    int f = 0;
                    String password = tokens[3];
                    Set <String> keys = Server.registration.keySet();
                    for(String key: keys){
                        if(name.equals(key)){
                            if(password.equals(Server.registration.get(key))){
                                dos.writeUTF("You are logged in!"+ '\n');
                            }
                            else{
                                dos.writeUTF("Incorrect password!"+ '\n');
                            }
                            f = 1;
                            break;
                        }
                    }
                    if(f == 0){
                        for (ClientHandler mc : Server.ar){
                            
                            if (mc.name.equals(name)) 
                            {
                                mc.dos.writeUTF("Registration complete!" + '\n');
                            }
                        }
                        //dos.writeUTF("Registration complete!" + '\n');
                        Server.registration.put(name, password);
                    }
                }*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try
        {
            this.dis.close();
            this.dos.close();
             
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

