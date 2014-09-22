package net.sldt_team.gameEngine.screen.event;

import net.sldt_team.gameEngine.screen.message.Message;

public interface MessagesEventProvider {

    /**
     * Events to hook into message dialogs display and clear
     */
    public void onMessageDialogDisplayed(Message message);
    public void onMessageDialogCleared();

    /**
     * Events to know if message dialogs can display or clear
     */
    public boolean canMessageDialogDisplay(Message message);
    public boolean canMessageDialogClear(Message message);
}
