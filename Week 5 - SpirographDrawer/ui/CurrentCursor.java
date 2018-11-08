package spirographs.ui;

import spirographs.ui.UIListButton;

public class CurrentCursor {
    
    UIListButton currentCursor;
    boolean justChanged = false;

    public CurrentCursor(UIListButton currentCursor) {
        this.currentCursor = currentCursor;
    }
    
    public UIListButton getCurrentCursor() {
        return currentCursor;
    }

    public void setCurrentCursor(UIListButton currentCursor) {
        this.currentCursor = currentCursor;
    }

    public boolean isJustChanged() {
        return justChanged;
    }

    public void setJustChanged(boolean justChanged) {
        this.justChanged = justChanged;
    }
    
    
    
}
