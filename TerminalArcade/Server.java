package TerminalArcade;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server{
    public class ClientHandler implements Runnable {

        public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
        private Socket socket;
        private BufferedReader in;
        private BufferedWriter out;
        private String clientUsername;
    
        public ClientHandler(Socket socket) {
            try {
                this.socket = socket;
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.out= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.clientUsername = in.readLine();
                clientHandlers.add(this);
                broadcast("SERVER: " + clientUsername + " has entered the chat!");
            } catch (IOException e) {
                close(socket, in, out);
            }
        }
        @Override
        public void run() {
            String messageFromClient;
            while (socket.isConnected()) {
                try {
                    messageFromClient = in.readLine();
                    broadcast(messageFromClient);
                } catch (IOException e) {
                    close(socket, in, out);
                    break;
                }
            }
        }
        public void broadcast(String messageToSend) {
            for (ClientHandler clientHandler : clientHandlers) {
                try {
                    if (!clientHandler.clientUsername.equals(clientUsername)) {
                        clientHandler.out.write(messageToSend);
                        clientHandler.out.newLine();
                        clientHandler.out.flush();
                    }
                } catch (IOException e) {
                    close(socket, in, out);
                }
            }
        }
    
        public void removeClientHandler() {
            clientHandlers.remove(this);
            broadcast("SERVER: " + clientUsername + " has left the chat!");
        }
    
        public void close(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
            removeClientHandler();
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final ServerSocket serverSocket;
    private ClientHandler clientHandler;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    while (!serverSocket.isClosed()) {
                        Socket socket = serverSocket.accept();
                        System.out.println("A new client has connected!");
                        clientHandler = new ClientHandler(socket);
                        Thread thread = new Thread(clientHandler);
                        
                        thread.start();
                    }
                } catch (IOException e) {
                    closeServerSocket();
                }
            }
        }).start();
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(65432);
        Server server = new Server(serverSocket);
        System.out.println("SERVER ON");
        server.start();
        Socket socket = new Socket("127.0.0.1", 65432);
        Client client = new Client(socket, "SERVER");
        client.listen();
        client.send();
    }

}