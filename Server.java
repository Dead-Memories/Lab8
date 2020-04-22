import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Server {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        final Logger logger = LogManager.getLogger();

        String env_name = "LAB_PROG_ENV_NAME";
        if (System.getenv(env_name) == null)
            System.out.println("Переменная окружения не задана. Для считывания файла добавьте переменную среды " + env_name);
        else {

            String file_name = System.getenv(env_name);

            System.out.println("\nСчитано: " + env_name + "   " + file_name);

            File file = new File(file_name);

            TreeSet<Organization> organizations = readFromFile(file);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String collection_creation_date = now.format(formatter);

            logger.info("Got collection with "+ organizations.size() + " elements");

            logger.info("We are running server");

            try {
                try (ServerSocket ss = new ServerSocket(2605)) {
                    System.out.println("ServerSocket awaiting connections...");
                    try (Socket socket = ss.accept()) {
                        System.out.println("Connection from " + socket + "!");
                        logger.info("Connection from " + socket + "!");
                        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                            objectOutputStream.writeObject("Connection establish.");
                            ServerCommandReader reader = new ServerCommandReader(objectOutputStream, objectInputStream, logger);

                            logger.info("Start messages exchange");

                            reader.start_listening(organizations, collection_creation_date, file);
//                            while (true) {
//
////                            Command cmessage = (Command) objectInputStream.readObject();
////                            System.out.println("Received [" + cmessage.description + "] from: " + socket);
////                            objectOutputStream.writeObject(new Response(cmessage.description));
//                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                logger.info("Connection were terminated");
                SaveToFile.saveToFile(organizations, file);
                logger.info("Collection was saved in " + file.getPath());
            }
        }
    }
    public static TreeSet<Organization> readFromFile(File file)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String text = new String();

            while((line=reader.readLine())!=null)  text+=line+'\n';

            TreeSet<Organization> organizations = new TreeSet<Organization>();

            JSONArray orgs = new JSONArray(text);


            for (int i=0; i<orgs.length(); i++)
            {
                JSONObject a = orgs.getJSONObject(i);

                String name = a.getString("name");

                long id = a.getLong("id");

                JSONObject c = a.getJSONObject("coordinates");
                Coordinates coords = new Coordinates(c.getLong("x"), c.getInt("y"));

                String creationDate = a.getString("creationDate");

                float annualTurnover = a.getFloat("annualTurnover");

                Integer employeesCount = a.getInt("employeesCount");

                OrganizationType type;
                if (a.isNull("type"))  type = null;
                else type = OrganizationType.valueOf(a.getString("type"));

                Address officialAddress;
                if (a.isNull("officialAddress")) officialAddress = null;
                else officialAddress = new Address(a.getString("officialAddress"));

                Organization organization = new Organization(name, coords, creationDate, annualTurnover, employeesCount, type, officialAddress);

                organizations.add(organization);
            }

            return organizations;
        }
        catch (Exception e)
        {
            System.out.println("Беда с файлом. Создана пустая коллекция");
            System.out.println(e.getMessage());
            return new TreeSet<Organization>();
        }
    }
}
