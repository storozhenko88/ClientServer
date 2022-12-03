package service;

import model.Client;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerServiceTest {

    private Map<Socket, Client> clients = new HashMap<>();
    private ServerService service = new ServerService();
    private PrintWriter outWriter;
    private BufferedReader reader;
    private Socket accept;
    private ByteArrayOutputStream out;
    private Client client;

    @Before
    public void init() {
        try {
            accept = mock(Socket.class);
            out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream("2".getBytes());
            when(accept.getOutputStream()).thenReturn(out);
            when(accept.getInputStream()).thenReturn(in);
            outWriter = new PrintWriter(accept.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
            client = new Client("user-1", LocalTime.now(), accept, "src/main/resources/client-1.json");
            clients.put(accept, client);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void addClientTest()  {
        Assert.assertEquals(service.addClient(outWriter, client), clients.get(accept));
    }

    @Test
    public void addPathTest(){
        String path = "src/main/resources/client-1.json";
        Assert.assertEquals(path, service.addPath(outWriter, reader));
    }

    @Test
    public void informationClientTest() throws IOException {
        Socket accept2 = mock(Socket.class);
        Client client2 = new Client("user-2", LocalTime.now(), accept2, "src/main/resources/client-2.json");
        service.addClient(outWriter, client);
        service.addClient(outWriter, client2);
        when(accept2.getOutputStream()).thenReturn(out);
        Assert.assertTrue(service.informationClient(client.getSocketUser()));
    }
}

