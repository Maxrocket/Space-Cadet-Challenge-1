package chatapplication.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import chatapplication.Handler;

public class ClientConnection {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String serverIP = "127.0.0.1";
    private Socket connection;
    public String username;
    public ArrayList<String> commandQueue;

    public Handler handler;

    public ClientConnection(Handler handler, String username, String serverIP) {
        this.handler = handler;
        commandQueue = new ArrayList();
        this.serverIP = serverIP;
        this.username = username;
    }

    public void init() {
        try {
            connectToServer();
            setupStreams();
            whileChatting();
        } catch (IOException ioException) {

        } finally {
            closeConnection();
        }
    }

    private void connectToServer() throws IOException {
        //Creates a connection to the server.
        System.out.println("attempting to connect");
        connection = new Socket(InetAddress.getByName(serverIP), 16180);
        System.out.println("Now connected to " + connection.getInetAddress().getHostName());
    }

    private void setupStreams() throws IOException {
        //Creates the streams between the computers.
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
    }

    private void whileChatting() throws IOException {
        String message = "";
        sendMessage("newUser " + username);
        do {
            try {
                message = (String) input.readObject();
                System.out.println("Client " + username + " RECIEVED - " + message);
                addQueue(message);
            } catch (ClassNotFoundException ex) {

            }
        } while (!message.equals("SERVER - END"));

    }

    private void closeConnection() {
        //Handles closing all the streams and the connection.
        System.out.println("Client " + username + " - Closing the connection.");
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (NullPointerException npx) {

        }
        handler.getMouseManager().setUIManager(null);
    }

    public void sendMessage(String message) {
        //Handles sending a message to the server through the connection.
        try {
            output.writeObject(message);
            output.flush();
            System.out.println("Client " + username + " SENT - " + message);
        } catch (IOException ioException) {
            System.out.println("Something went wrong!");
        }
    }

    public void addQueue(String command) {
        //Adds a new command to the queue.
        commandQueue.add(command);
    }

    public void bumpQueue() {
        //Removes the first index of the queue.
        commandQueue.remove(0);
    }
}
