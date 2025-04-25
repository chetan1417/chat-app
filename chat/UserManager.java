package chat;

import java.io.*;
import java.util.*;

public class UserManager {
    private static final String SERVER_FILE = "chat/server_info.txt";
    private static final String CLIENT_FILE = "chat/Credentials.txt";

    public boolean serverExists() {
        return new File(SERVER_FILE).exists();
    }

    public void registerServer(String id, String password, String phone) {
        try (PrintWriter out = new PrintWriter(new FileWriter(SERVER_FILE))) {
            out.println(id);
            out.println(password);
            out.println(phone);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validateServer(String id, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(SERVER_FILE))) {
            String storedId = reader.readLine();
            String storedPass = reader.readLine();
            return storedId.equals(id) && storedPass.equals(password);
        } catch (IOException e) {
            return false;
        }
    }

    public void registerClient(String username, String password) {
        try (PrintWriter out = new PrintWriter(new FileWriter(CLIENT_FILE, true))) {
            out.println(username + "," + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean validateClient(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CLIENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                if (split[0].equals(username) && split[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
