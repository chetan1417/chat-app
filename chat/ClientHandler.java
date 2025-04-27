package chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private ServerGUI gui;
    private PrintWriter writer;
    private static List<PrintWriter> clients = new ArrayList<>();

    public ClientHandler(Socket socket, ServerGUI gui) {
        this.socket = socket;
        this.gui = gui;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            clients.add(writer);

            String name = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                gui.appendMessage(line, true);
                broadcast(line);
            }

        } catch (IOException e) {
            gui.appendMessage("Client disconnected.", true);
        } finally {
            clients.remove(writer);
        }
    }

    private void broadcast(String message) {
        for (PrintWriter client : clients) {
            client.println(message);
        }
    }

    // New method added for ServerGUI to broadcast system messages
    public static void broadcastFromServer(String message) {
        for (PrintWriter client : clients) {
            client.println(message);
        }
    }
}
