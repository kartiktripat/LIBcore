package client.staff;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class StaffClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private StaffGUI gui;

    public StaffClient(String serverIP, int port) {
        try {
            socket = new Socket(serverIP, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            gui = new StaffGUI(this);
            listenForResponses();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Connection Error: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void listenForResponses() {
        new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    gui.displayServerMessage(response);
                }
            } catch (IOException e) {
                gui.displayServerMessage("Connection closed.");
            }
        }).start();
    }

    public static void main(String[] args) {
        new StaffClient("localhost", 12345);
    }
}
