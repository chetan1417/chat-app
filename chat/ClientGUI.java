package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private PrintWriter writer;
    private BufferedReader reader;
    private String username;

    public ClientGUI() {
        login();
    }

    private void login() {
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        Object[] message = {
                "Username:", userField,
                "Password:", passField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Client Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            username = userField.getText();
            String password = new String(passField.getPassword());

            if (UserManager.validateClient(username, password)) {
                setupGUI();
                connectToServer();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
                login();
            }
        } else {
            System.exit(0);
        }
    }

    private void setupGUI() {
        setTitle("Client: " + username);
        setSize(500, 500);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(chatArea);
        add(scroll, BorderLayout.CENTER);

        messageField = new JTextField();
        JButton sendButton = new JButton("Send");

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(messageField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("server", 5000);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.println(username);

            // Load chat history
            File historyFile = new File("chat/chat_history.txt");
            if (historyFile.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(historyFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith(username + ":")) {
                            appendMessage(line, true);
                        } else {
                            appendMessage(line, false);
                        }
                    }
                }
            }

            new Thread(() -> {
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        if (!line.startsWith(username + ":")) {
                            appendMessage(line, false);
                        }
                    }
                } catch (IOException e) {
                    appendMessage("Disconnected from server.", false);
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Connection error: " + e.getMessage());
            System.exit(0);
        }
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String formatted = username + ": " + message + " [" + time + "]";
            writer.println(formatted);
            appendMessage(formatted, true);
            messageField.setText("");
        }
    }

    private void appendMessage(String message, boolean isSender) {
        if (isSender) {
            chatArea.append(String.format("%70s\n", message));
        } else {
            chatArea.append(message + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientGUI::new);
    }
}
