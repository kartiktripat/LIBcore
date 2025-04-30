package server;

import server.services.AuthenticationService;
import server.services.SessionManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 12345;
    private static AuthenticationService authService;
    private static SessionManager sessionManager;

    public static void main(String[] args) {
        authService = new AuthenticationService();
        sessionManager = new SessionManager();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[SERVER] Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] Connected to client: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, authService, sessionManager);
                clientHandler.start();
            }

        } catch (IOException e) {
            System.err.println("[SERVER] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
