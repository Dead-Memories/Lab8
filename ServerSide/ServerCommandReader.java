package ServerSide;

import Organizations.*;
import Communication.*;
//import org.apache.logging.log4j.Logger;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

public class ServerCommandReader {

    ObjectOutputStream toClient;
    ObjectInputStream fromClient;
    ServerCommandHub hub;
//    Logger logger;
    Connection connection;
    long userid;


public ServerCommandReader(ObjectOutputStream toClient, ObjectInputStream fromClient, Connection connection, long userid) throws SQLException {
    this.toClient = toClient;
    this.fromClient = fromClient;
    this.hub = new ServerCommandHub(toClient, fromClient, connection, userid);
//    this.logger = logger;
    this.connection = connection;
    this.userid = userid;
}

public void start_listening(Set<Organization> organizations) throws IOException, ClassNotFoundException, SQLException {

        String command;
        String[] commandParts;
        while (true) {

                    command = ((Command) fromClient.readObject()).description;

//                    logger.info("Got command: " + command);

                    commandParts = command.split(" ", 3);

                    switch (commandParts[0]) {
                        case "getUser":
                            hub.getUser();
                            break;
                        case "getUpdate":
                            hub.getUpdate();
                            break;
                        case "help":
                            hub.help(commandParts);
                            break;
                        case "info":
                            hub.info(organizations);
                            break;
                        case "getData":
                            hub.getData(organizations);
                            break;
                        case "show":
                            hub.show(organizations);
                            break;
                        case "add":
                            hub.add(organizations);
                            break;
                        case "getRowById":
                            hub.getRowById(organizations, Long.parseLong(commandParts[1]));
                            break;
                        case "update_by_id":
                            try {
                                hub.update_by_id(organizations, Long.parseLong(commandParts[1]));
                            } catch (Exception e) {
                                toClient.writeObject(new Response(">В коллекции нет элемента с указанным id. Повторите ввод"));
                            }
                            break;
                        case "remove_by_id":
                            hub.remove_by_id(Long.parseLong(commandParts[1]), organizations);
                            break;
                        case "clear":
                            hub.clear(organizations);
                            break;
                        case "add_if_min":
                            hub.add_if_min(organizations);
                            break;
                        case "remove_greater":
                            try {
                                hub.remove_greater(organizations, commandParts[1]);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case "remove_lower":
                            hub.remove_lower(organizations, commandParts[1]);
                            break;
                        case "count_greater_than_type":
                            try {
                                hub.count_greater_than_type(organizations, commandParts[1]);
                            } catch (Exception e) {
                                System.out.println("Попробуйте еще раз");
                            }
                            break;
                        case "print_descending":
                            hub.print_descending(organizations);
                            break;
                        case "print_field_ascending_official_address":
                            hub.print_field_ascending_official_address(organizations);
                            break;
                    }
                }
            }
}



