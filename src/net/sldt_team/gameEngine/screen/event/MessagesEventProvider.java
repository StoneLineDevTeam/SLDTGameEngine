package net.sldt_team.gameEngine.screen.event;

import net.sldt_team.gameEngine.screen.message.Message;

public interface MessagesEventProvider {

    /**
     * Message dialog displayed
     */
    public void onMessageDialogDisplayed(Message message);

    /**
     * Message dialog cleared (removed)
     */
    public void onMessageDialogCleared();

    /**
     * Can message be displayed
     */
    public boolean canMessageDialogDisplay(Message message);

    /**
     * Can message be cleared (removed)
     */
    public boolean canMessageDialogClear(Message message);
}
