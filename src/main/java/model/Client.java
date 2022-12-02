package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.Socket;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
public class Client {
    private String userName;
    private LocalTime date;
    private Socket socketUser;
    private String infoStoragePath;




    @Override
    public String toString() {
        return "Connected: user name = " + userName + '\'' +
                ", time connect = " + date;
    }
}
