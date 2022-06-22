package TerminalArcade;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String username;

    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch(IOException e){
            close(socket, in, out);
        }
    }

    public void send(){
        try{
            out.write(username);
            out.newLine();
            out.flush();

            Scanner scan = new Scanner(System.in);
            while(socket.isConnected()){
                String msg = scan.nextLine();
                out.write(username + ": " + msg);
                out.newLine();
                out.flush();
            }
            scan.close();
        }catch (IOException e){
            close(socket, in, out);
        }
    }

    public void listen(){
        new Thread(new Runnable() {
            @Override
            public void run(){
                String msgAll;
                while(socket.isConnected()){
                    try{
                        msgAll = in.readLine();
                        System.out.println(msgAll);
                    } catch(IOException e){
                        close(socket, in, out);
                    }
                }
            }
        }).start();
    }

    public void close(Socket socket, BufferedReader in, BufferedWriter out){
        try{
            if (in != null){
                in.close();
            }
            if(out != null){
                out.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scan.nextLine();
        Socket socket = new Socket("127.0.0.1", 65432);
        Client client = new Client(socket, username);
        client.listen();
        client.send();
        scan.close();
    }
}
    
