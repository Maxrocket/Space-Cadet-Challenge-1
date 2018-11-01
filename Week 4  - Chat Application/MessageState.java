package chatapplication.states;

import java.awt.Graphics;
import chatapplication.Handler;
import chatapplication.ui.ClickListener;
import chatapplication.ui.UIButton;
import chatapplication.ui.UIConsole;
import chatapplication.ui.UIListBox;
import chatapplication.ui.UIManager;
import chatapplication.ui.UITextBox;
import java.awt.Font;

public class MessageState extends State {

    private UIManager uiManager;
    private UITextBox inputBox;
    private UIConsole uiConsole;
    private UIListBox uiListBox;

    public MessageState(final Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);

        inputBox = new UITextBox(50, handler.getHeight() - 110, handler.getWidth() - 370, 60, new Font("monospaced", Font.PLAIN, 30), "abcdefghijklmnopqrstuvwxyABCDEFGHIJKLMNOPQRSTUVWXYZ 1234567890!\"£$%^&*()_+-=[]{};:'@#~,<.>/?", "", "", handler);
        uiManager.addUIObject(inputBox);

        uiManager.addUIObject(new UIButton(handler.getWidth() - 300, handler.getHeight() - 110, 250, 60, "SENT", new ClickListener() {
            public void onClick() {
                handler.getClient().sendMessage("message [" + handler.getClient().username + "] " + inputBox.currentText);
                inputBox.clear();
            }
        }));
        
        uiConsole = new UIConsole(50, 50, handler.getWidth() - 370, 720);
        uiManager.addUIObject(uiConsole);
        uiListBox = new UIListBox(handler.getWidth() - 300, 50, 250, 720);
        uiManager.addUIObject(uiListBox);
    }

    public void tick() {
        uiManager.tick();
        if (handler.getClient() != null && !handler.getClient().commandQueue.isEmpty()) {
            for (String command : handler.getClient().commandQueue) {
                String[] arg = command.split(" ");
                switch (arg[0]) {
                    case "message":
                        uiConsole.add(command.substring(8));
                        break;
                    case "userList":
                        uiListBox.clear();
                        for (int i = 1; i < arg.length; i++) {
                            uiListBox.add(arg[i]);
                        }
                        break;
                }
            }
            handler.getClient().commandQueue.clear();
        }
    }

    public void render(Graphics g) {
        uiManager.render(g);
    }

    public void init() {
        handler.getMouseManager().setUIManager(uiManager);
    }
    
}
