package Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import model.Client;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.*;

public class ServerService {

    private Map<Socket, Client> clients = new HashMap<>();
    private int counter = 1;
    private final ObjectMapper mapper = new JsonMapper();


    public String addPath(PrintWriter out, BufferedReader reader) {
        String path;
        try {
            out.println("by default, the customer record storage location is in the folder - client-" + counter);
            out.print("if you want to change folder name press 1: ");
            out.flush();
            path = "src/main/resources/client-" + counter + ".json";

            String choice = reader.readLine();
            if (choice.equals("1")) {
                out.print("enter name folder: ");
                out.flush();
                String folderName = reader.readLine();
                path = "src/main/resources/" + folderName + ".json";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    public Client addClient(PrintWriter out,  Client client) {
        client.setUserName("user - " + counter);
        out.println(client);
        out.flush();
        clients.put(client.getSocketUser(), client);
        counter++;
        return client;
    }

    public boolean informationClient(Socket socket) {
        boolean performingTheTask = false;
        if (clients.size() != 0) {
            for (Map.Entry<Socket, Client> client : clients.entrySet()) {
                if (!client.getValue().getSocketUser().equals(socket)) {
                    try {
                        PrintWriter outAnotherClient = new PrintWriter(client.getValue().getSocketUser().getOutputStream());
                        outAnotherClient.println("==================================");
                        outAnotherClient.println("INFORMATION: new client connected");
                        outAnotherClient.println("==================================");
                        outAnotherClient.flush();
                        performingTheTask = true;
                    } catch (IOException e) {
                        System.err.println("an error occurred while sending a message to the client");
                        e.printStackTrace();
                    }
                }
            }
        }
        return performingTheTask;
    }

    public boolean exit(Socket socket, String customerRecord) {
        try {
            File fileClient = new File(clients.get(socket).getInfoStoragePath());
            mapper.writeValue(fileClient, customerRecord);
            clients.get(socket).getSocketUser().close();
            clients.remove(socket);
            return true;
        } catch (IOException e) {
            System.err.println("cannot save customer record");
            e.printStackTrace();
            return false;
        }
    }

}

