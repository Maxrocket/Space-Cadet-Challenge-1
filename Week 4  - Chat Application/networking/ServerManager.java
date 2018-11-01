package chatapplication.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import chatapplication.Handler;

public class ServerManager {

    public Handler handler;
    public ArrayList<ServerConnection> users;
    public ArrayList<ServerConnection> usersToRemove;
    private ServerSocket server;

    public ServerManager(Handler handler) {
        this.handler = handler;
        users = new ArrayList();
        usersToRemove = new ArrayList();
    }

    public void init() {
        try {
            server = new ServerSocket(16180, 100);
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        startNewConnection();
    }
    
    public void tick() {
        for (ServerConnection userToRemove : usersToRemove) {
            users.remove(userToRemove);
        }
        for (ServerConnection user : users) {
            if (!user.commandQueue.isEmpty()) {
                String[] arg = user.commandQueue.get(0).split(" ");
                switch (arg[0]) {
                    case "newUser":
                        user.username = arg[1];
                        String userNames = "userList";
                        for (ServerConnection user1 : users) {
                            userNames += " " + user1.username;
                        }
                        broadCast(userNames);
                        break;
                    case "message":
                        broadCast(user.commandQueue.get(0));
                        break;
                }
                user.commandQueue.remove(0);
            }
        }
    }

    public void startNewConnection() {
        System.out.println("START NEW CONNECTION!");
        try {
            users.add(new ServerConnection(handler, server, this));
        } catch (IOException ex) {
            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Runnable connections = new Runnable() {
            public void run() {
                users.get(users.size() - 1).init();
            }
        };
        new Thread(connections).start();
    }

    public void addConnectionToRemove(ServerConnection serverConnection) {
        usersToRemove.add(serverConnection);
    }

    public void broadCast(String message) {
        for (ServerConnection user : users) {
            user.sendMessage(message);
        }
    }
}
