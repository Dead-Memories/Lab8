package ClientSide;

import java.io.*;
import java.net.*;
import java.nio.channels.SocketChannel;

public class Client {
    static boolean connect_button = false;
    static boolean login_button;
    static boolean logout_flag;
    public static void main(String[] args) throws IOException, InterruptedException {
        while (true) {
            logout_flag = true;
            login_button = false;
            SocketChannel outcoming = SocketChannel.open();
            try {
                outcoming.connect(new InetSocketAddress("127.0.0.1", 2605));
                System.out.println("ServerSide.Server reached");
                outcoming.socket().setSoTimeout(10000);
                try (ObjectOutputStream SendtoServer = new ObjectOutputStream(outcoming.socket().getOutputStream());
                     ObjectInputStream GetfromServer = new ObjectInputStream(outcoming.socket().getInputStream())
                ) {
                    System.out.println((String) GetfromServer.readObject());
                    LoginForm.main(SendtoServer, GetfromServer);

                    while (!login_button)
                        Thread.sleep(200);

                    MainForm.main(SendtoServer, GetfromServer);

                    while(logout_flag)
                        Thread.sleep(500);

                    System.out.println(">Closing socket and terminating programm. Boooom");
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } catch (IOException e) {
                System.out.println(">Could not connect. ServerSide.Server caught a corona.");
                System.out.println(e.getMessage());
            }
        }
    }
}