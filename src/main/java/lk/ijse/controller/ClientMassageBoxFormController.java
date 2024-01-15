package lk.ijse.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class ClientMassageBoxFormController implements Initializable {

    @FXML
    private JFXTextArea txtAreaClient;

    @FXML
    private TextField txtClient;

    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;

    public void client() throws IOException {
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 3001);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                String message = "";
                String reply = "";

                while (!message.equals("exit")) {

                    message = dataInputStream.readUTF();
                    txtAreaClient.appendText("\nServer : " + message);
                }
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {}

    public void clientSendMessageOnAction(javafx.event.ActionEvent actionEvent) throws IOException {
        txtAreaClient.appendText("\nme : " + txtClient.getText());
       // txtAreaClient.setStyle("-fx-text-alignment: right;");
        dataOutputStream.writeUTF(txtClient.getText());
        dataOutputStream.flush();
        txtClient.setText("");
    }
}
