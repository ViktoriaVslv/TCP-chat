package lab12;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;


public class Server {

    private ArrayList<User> clients =  new ArrayList<>();
    private ServerSocket server;

    public  Server() {
        try {
            server = new ServerSocket(1234);
            System.out.println("Server is running.");
            while (true) {
                Socket client = server.accept();
                User user = new User(client, clients);
                clients.add(user);
                user.start();
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            close();
        }
    }

    private void close() {
        try {
            server.close();
            synchronized (clients) {
                for (User client : clients) {
                    client.close();
                }
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}