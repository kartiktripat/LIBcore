package client.member;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class MemberClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private MemberGUI gui;

    public MemberClient(String serverIP, int port) {
        try {
            socket = new Socket(serverIP, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            gui = new MemberGUI(this);
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
        new MemberClient("localhost", 12345);
    }
}