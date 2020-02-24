package lab12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client{

    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;

    public Client() {
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket("localhost", 1234);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter( socket.getOutputStream(),true);

            System.out.print("Welcome to the chat! Input your name: ");
            writer.println(scanner.nextLine());

            inThread in = new inThread(reader);
            in.start();

            String message;
            while (true) {
                message = scanner.nextLine();
                if( message.startsWith("@quit")){
                    writer.println("@quit");
                    break; }
                writer.println(message);
            }
            in.SenderStop();
            reader.close();
            writer.close();
            socket.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}