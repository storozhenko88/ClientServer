
import model.Client;
import service.ServerService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) {

        ServerService service = new ServerService();

        try (ServerSocket serverSocket = new ServerSocket(8085)) {

            while (true) {

                Socket accept = serverSocket.accept();
                new Thread(() -> {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                        PrintWriter out = new PrintWriter(accept.getOutputStream());
                        String customerRecord = "";
                        String word;
                        boolean isExit = false;

                        out.println("Hello");
                        out.println("INFORMATION:");
                        out.println("if you want to finish, type 'exit'");
                        out.flush();
                        Client client = new Client("user", LocalTime.now(), accept, service.addPath(out, reader));
                        service.addClient(out, client);
                        service.informationClient(accept);

                        while (((word = reader.readLine()) != null) && !isExit) {
                            if (word.equals("exit")) {
                                isExit = service.exit(accept, customerRecord);
                                out.println("you finished work with server!!!");
                                out.flush();
                                reader.close();
                                out.close();
                            } else customerRecord = customerRecord + '\n' + word;
                        }
                    } catch (IOException e) {
                        System.out.println("Client disconnected!");
                    }
                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

