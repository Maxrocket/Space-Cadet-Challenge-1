package chatapplication.states;

import java.awt.Graphics;
import chatapplication.Handler;
import chatapplication.networking.ClientConnection;
import chatapplication.networking.ServerConnection;
import chatapplication.networking.ServerManager;
import chatapplication.ui.ClickListener;
import chatapplication.ui.UIButton;
import chatapplication.ui.UIManager;
import chatapplication.ui.UITextBox;
import java.awt.Font;

public class MenuState extends State {

    private UIManager uiManager;
    private UITextBox usernameBox, ipAddressBox;

    public MenuState(final Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);

        usernameBox = new UITextBox(50 + 350, 50 + 250, 700, 60, new Font("monospaced", Font.PLAIN, 30), "abcdefghijklmnopqrstuvwxyABCDEFGHIJKLMNOPQRSTUVWXYZ", "", "Username:", handler);
        ipAddressBox = new UITextBox(50 + 350, 140 + 250, 700, 60, new Font("monospaced", Font.PLAIN, 30), "1234567890.", "", "IP Address:", handler);
        uiManager.addUIObject(usernameBox);
        uiManager.addUIObject(ipAddressBox);

        uiManager.addUIObject(new UIButton(50 + 350, 220 + 250, 340, 100, "HOST", new ClickListener() {
            public void onClick() {
                setUpServer(usernameBox.currentText, "127.0.0.1");
            }
        }));
        uiManager.addUIObject(new UIButton(410 + 350, 220 + 250, 340, 100, "JOIN", new ClickListener() {
            public void onClick() {
                setUpClient(usernameBox.currentText, ipAddressBox.currentText);
            }
        }));

        handler.getMouseManager().setUIManager(uiManager);
    }

    public void tick() {
        uiManager.tick();
    }

    public void render(Graphics g) {
        uiManager.render(g);
    }

    public void init() {
        handler.getMouseManager().setUIManager(uiManager);
    }

    public void setUpServer(final String name, final String ipAddress) {
        Runnable connections = new Runnable() {
            public void run() {
                ServerManager server = new ServerManager(handler);
                server.init();
                while (true) {
                    server.tick();
                }
            }
        };
        new Thread(connections).start();
        setUpClient(name, ipAddress);
    }

    public void setUpClient(final String name, final String ipAddress) {
        Runnable connections = new Runnable() {
            public void run() {
                ClientConnection client = new ClientConnection(handler, name, ipAddress);
                handler.setClient(client);
                client.init();
            }
        };
        new Thread(connections).start();
        handler.setCurrentState(handler.getGame().messageState);
        handler.getGame().messageState.init();
    }
}
