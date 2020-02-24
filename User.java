package lab12;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class User extends Thread {

    private String name;
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private final ArrayList<User> clients;
    private  ArrayList<String> phone;

    public User(Socket socket, ArrayList<User> clients) {
        this.socket = socket;
        this.clients = clients;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter( socket.getOutputStream(),true);
            phone =new ArrayList<>();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            close();
        }
    }

    public  void run() {
        try {
            name = reader.readLine();
            synchronized (clients) {
                for (User client : clients) {
                    client.writer.println(name + " just joined the chat!");
                }
            }

            while (true) {
                String message = reader.readLine();
                if (message.startsWith("@quit")) {
                    synchronized (clients) {
                        for (User client : clients) {
                            client.writer.println(name + " left to chat!");
                        }
                    }
                    break;
                }
                else if (message.startsWith("@list")) {
                    synchronized (clients) {
                    BufferedReader readerfile = new BufferedReader(new FileReader("phonebook.txt"));
                    String line;
                    phone=new ArrayList<>();
                    while ((line = readerfile.readLine()) != null) {
                        phone.add(line);
                    }
                    readerfile.close();

                        this.writer.println("phonebook: ");
                        for (String p : phone) {
                            this.writer.println(p);
                        }
                    }
                }
                else if (message.startsWith("@add")) {
                    synchronized (clients) {
                        String[] parts = message.split(" ");
                        if (parts[0].equals("@add") && parts.length > 2) {
                            BufferedReader readerfile = new BufferedReader(new FileReader("phonebook.txt"));
                            String line;
                            phone = new ArrayList<>();
                            while ((line = readerfile.readLine()) != null) {
                                phone.add(line);
                            }
                            readerfile.close();
                            String nick = parts[1];
                            String uniqName=parts[1];
                            int num = 0;
                            for (String p : phone) {
                                String[] parts1 = p.split(" ");

                                if (parts1[0].equals(nick)) {
                                    num++;
                                    nick=uniqName+"_"+num;
                                    parts[1]=nick;
                                }
                            }
                            phone.add(parts[1] + " " + parts[2]);
                            FileWriter writerfile = new FileWriter("phonebook.txt");
                            for (String p : phone) {
                                writerfile.write(p + System.getProperty("line.separator"));
                            }
                            writerfile.close();
                            this.writer.println("Contact added.");
                        }
                    }
                }
               else if (message.startsWith("@senduser"))  {
                    String[] parts = message.split(" ");
                    if(parts[0].equals("@senduser") && parts.length>1){
                        synchronized (clients) {
                            String tmp ;
                            for (User client : clients) {
                                tmp = client.getUserName();
                                if (tmp.equals(parts[1])) {
                                    client.writer.println(name + ": " + message);
                                }
                            }
                        }
                    }
                }
                 else {
                    synchronized (clients) {
                        for (User client : clients) {
                            client.writer.println(name + ": " + message);
                        }
                    }
                }
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            close();
        }
    }

    private String getUserName() {
        return this.name;
    }

    void close() {
        try {
            reader.close();
            writer.close();
            socket.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
