package chatapplication.networking;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import chatapplication.Handler;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConnection {

    public Handler handler;

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    private ServerManager serverManager;

    public String username;
    public ArrayList<String> commandQueue;

    public ServerConnection(Handler handler, ServerSocket server, ServerManager serverManager) throws IOException {
        this.handler = handler;
        this.server = server;
        this.serverManager = serverManager;
        username = "";
        commandQueue = new ArrayList();
    }

    public void init() {
        try {
            waitForConnection();
            setupStreams();
            connection();
        } catch (EOFException eofException) {
            System.out.println("\n Server ended the connection! ");
        } catch (IOException ex) {
            System.out.println("Looks like they disconnected :(");
        } finally {
            closeConnection();
        }
    }

    private void waitForConnection() throws IOException {
        //Waits for something to connect to the connection.
        System.out.println("Waiting for someone to connect...");
        connection = server.accept();
        System.out.println("Now connected to " + connection.getInetAddress().getHostName());
    }

    private void setupStreams() throws IOException {
        //Sets up the streams with the new connection
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        System.out.println("Streams are now setup");
    }

    private void connection() throws IOException {
        //First sends the size of the game to the new connection. Then enters a while loop 
        //that is only exited when the connection ends. The while loop first waits to recieve
        //a message then looks at the keyword of the command at the start of the message.
        //It will then either handle the command in this object or if it doesn't recognise the
        //keyword it adds it to the queue for another object to handle it.
        String message = "";
        do {
            try {
                message = (String) input.readObject();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Server Connection " + username + " RECIEVED - " + message);
            addQueue(message);
        } while (!message.equals("CLIENT - END"));
    }

    public void sendMessage(String message) {
        //Sends a message to the connected client.
        try {
            System.out.println("Server Connection " + username + " SENT - " + message);
            output.writeObject(message);
            output.flush();
        } catch (IOException ioException) {
            System.out.println(ioException);
        }
    }

    public void closeConnection() {
        //Handles closing all the streams and the connection.
        System.out.println("Server - Closing the connection.");
        serverManager.addConnectionToRemove(this);
        try {
            output.close(); //Closes the output path to the client
            input.close(); //Closes the input path to the server, from the client.
            connection.close(); //Closes the connection between you and the client
        } catch (IOException ioException) {
            ioException.printStackTrace();
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
