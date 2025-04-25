package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton, createClientButton;
    private ServerSocket serverSocket;
    private UserManager userManager;
    private PrintWriter writer;

    public ServerGUI() {
        userManager = new UserManager();

        if (!userManager.serverExists()) {
            registerServer();
        } else {
            loginServer();
        }
    }

    private void registerServer() {
        JTextField idField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JTextField phoneField = new JTextField();
        Object[] message = {
                "Server ID:", idField,
                "Password:", passField,
                "Phone Number:", phoneField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Register Server", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            userManager.registerServer(idField.getText(), new String(passField.getPassword()), phoneField.getText());
            loginServer();
        } else {
            System.exit(0);
        }
    }

    private void loginServer() {
        JTextField idField = new JTextField();
        JPasswordField passField = new JPasswordField();
        Object[] message = {
                "Server ID:", idField,
                "Password:", passField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Server Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (userManager.validateServer(idField.getText(), new String(passField.getPassword()))) {
                setupGUI(idField.getText());
                startServerSocket();
                connectAsClient(idField.getText()); // Server connects to its own socket
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
                loginServer();
            }
        } else {
            System.exit(0);
        }
    }

    private void setupGUI(String username) {
        setTitle("Server Chat - " + username);
        setSize(500, 500);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(chatArea);
        add(scroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Send");
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        createClientButton = new JButton("Create Client Credentials");
        createClientButton.addActionListener(e -> openClientRegistration());

        add(createClientButton, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage(username));
        messageField.addActionListener(e -> sendMessage(username));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void openClientRegistration() {
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        Object[] message = {
                "New Client Username:", userField,
                "Password:", passField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Create Client", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            userManager.registerClient(userField.getText(), new String(passField.getPassword()));
            JOptionPane.showMessageDialog(this, "Client created successfully!");
        }
    }

    private void startServerSocket() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(5000);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new ClientHandler(clientSocket, this).start();
                }
            } catch (IOException e) {
                appendMessage("Error starting server socket.", false);
            }
        }).start();
    }

    private void connectAsClient(String username) {
        try {
            Socket socket = new Socket("localhost", 5000);
            writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.println(username);

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
                        // Skip echoed messages sent by the server itself
                        if (!line.contains("[FROM_SERVER]")) {
                            appendMessage(line, false);
                        }
                    }
                } catch (IOException e) {
                    appendMessage("Disconnected from server.", false);
                }
            }).start();
        } catch (IOException e) {
            appendMessage("Error connecting to own server socket.", false);
        }
    }

    public void appendMessage(String message, boolean isSender) {
        try (PrintWriter out = new PrintWriter(new FileWriter("chat/chat_history.txt", true))) {
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isSender) {
            chatArea.append(String.format("%70s\n", message));
        } else {
            chatArea.append(message + "\n");
        }
    }

    private void sendMessage(String username) {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String formatted = username + ": " + message + " [" + time + "] [FROM_SERVER]";
            writer.println(formatted);
            appendMessage(formatted, true);
            messageField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ServerGUI::new);
    }

}
