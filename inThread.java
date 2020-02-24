package lab12;

import java.io.BufferedReader;
import java.io.IOException;


public class
inThread extends Thread {

    private boolean flag;
    BufferedReader reader;

    public inThread(BufferedReader reader) {
        this.reader = reader;
        flag = true;
    }

    public void SenderStop() {
        flag = false;
    }

    public void run() {
        try {
            while (flag) {
                String str = reader.readLine();
                System.out.println(str);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
