package lk.ijse.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerMassageBoxController implements Initializable {

    @FXML
    private JFXTextArea txtAreaServer;
    @FXML
    private TextField txtTypingBar;

    DataOutputStream dataOutputStream;

    DataInputStream dataInputStream;

    public void server() throws IOException {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(3001);

                Socket socket = serverSocket.accept();
                txtAreaServer.appendText("Chat Started!");

                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                String message = "";
                String reply = "";
                while (!message.equals("exit")) {
                    message = dataInputStream.readUTF();
                    txtAreaServer.appendText("\nClient : " + message);
                }
                dataOutputStream.close();
                dataInputStream.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @FXML
    void serverSendOnAction(ActionEvent event) throws IOException {
        txtAreaServer.appendText("\nme : " + txtTypingBar.getText());
      //  txtAreaServer.setStyle("-fx-text-alignment: right;");
        dataOutputStream.writeUTF(txtTypingBar.getText());
        dataOutputStream.flush();
        txtTypingBar.setText("");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            server();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
