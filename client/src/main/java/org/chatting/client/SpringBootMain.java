package org.chatting.client;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootMain {

    public static void main(String[] args) {
        Application.launch(JavaFXApplication.class, args);
    }
}
