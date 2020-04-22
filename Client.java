import java.io.*;
import java.net.*;
import java.nio.channels.SocketChannel;
import java.util.HashSet;

public class Client {
    public static void main(String[] args) throws IOException {

        SocketChannel outcoming = SocketChannel.open();
        try {
            outcoming.connect(new InetSocketAddress(InetAddress.getLocalHost() , 2605));
            System.out.println("Server reached");
            outcoming.socket().setSoTimeout(10000);
            try (ObjectOutputStream SendtoServer = new ObjectOutputStream(outcoming.socket().getOutputStream());
                 ObjectInputStream GetfromServer = new ObjectInputStream(outcoming.socket().getInputStream())
                 ) {
                ClientCommandReader clientreader = new ClientCommandReader(GetfromServer, SendtoServer);
                System.out.println((String) GetfromServer.readObject());
                System.out.println(">Start command reading");
                clientreader.start_reading(new HashSet<String>(), "");
                System.out.println(">Closing socket and terminating programm. Boooom");
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }  catch (IOException e) {
            System.out.println(">Could not connect. Server caught a corona.");
            System.out.println(e.getMessage());
        }
    }
}