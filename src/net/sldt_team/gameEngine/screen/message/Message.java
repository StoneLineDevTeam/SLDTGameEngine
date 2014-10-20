package net.sldt_team.gameEngine.screen.message;

import com.sun.istack.internal.NotNull;
import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.gui.GuiButton;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.screen.Screen;
import net.sldt_team.gameEngine.util.MathUtilities;

import java.util.ArrayList;
import java.util.List;

public class Message {

    private String messageTitle;
    private String[] messageContent = new String[4];
    private String texturePath;
    private List<GuiButton> buttons;
    private boolean isConfirmation;

    private Runnable yesAction;


    public Message(@NotNull MessageType type, String title, @NotNull String content, Runnable yes){
        String path = null;
        yesAction = yes;
        buttons = new ArrayList<GuiButton>();
        switch(type){
            case INFO:
                path = "message/infoLogo.png";
                break;
            case ERROR:
                path = "message/errorLogo.png";
                break;
            case CRITICAL:
                path = "message/criticalLogo.png";
                break;
            case WARNING:
                path = "message/warningLogo.png";
                break;
            case CONFIRMATION:
                path = "message/confirmLogo.png";
                isConfirmation = true;
                break;
        }
        texturePath = path;

        String[] splits = content.split("\n");
        if (splits.length > 3){
            GameApplication.log.warning("MessageDisplay -> Can not set content : LINE_NUMBER_EXITING_MEMORY");
            return;
        }
        int i = 0;
        for (String s : splits){
            messageContent[i] = s;
            i++;
        }
        messageTitle = title;
    }

    public void init(RenderEngine renderEngine, final Screen screen){
        if (isConfirmation){
            GuiButton yes = new GuiButton("Yes", 128, 32, renderEngine.loadTexture("message/button.png"), (int)MathUtilities.getCenteredObjectX(512) + 5, (int)MathUtilities.getCenteredObjectY(256) + 200, 150, 50);
            GuiButton no = new GuiButton("No", 128, 32, renderEngine.loadTexture("message/button.png"), (int)MathUtilities.getCenteredObjectX(512) + 160, (int)MathUtilities.getCenteredObjectY(256) + 200, 150, 50);
            no.setAction(new Runnable() {
                public void run() {
                    screen.clearDesplayedMessage();
                }
            });
            if (yesAction != null){
                yes.setAction(yesAction);
            } else {
                yes.setAction(new Runnable() {
                    public void run() {
                        screen.clearDesplayedMessage();
                    }
                });
            }
            buttons.add(yes);
            buttons.add(no);
            return;
        }
        GuiButton ok = new GuiButton("Ok", 128, 32, renderEngine.loadTexture("message/button.png"), (int)MathUtilities.getCenteredObjectX(512) + 5, (int)MathUtilities.getCenteredObjectY(256) + 200, 150, 50);
        if (yesAction != null){
            ok.setAction(yesAction);
        } else {
            ok.setAction(new Runnable() {
                public void run() {
                    screen.clearDesplayedMessage();
                }
            });
        }
        buttons.add(ok);
    }

    public String getMessageTitle(){
        if (messageTitle == null){
            return "SLDT's GameEngine";
        }
        return messageTitle;
    }

    public void setContentForLine(int id, String line){
        if (id > 3){
            GameApplication.log.warning("MessageDisplay -> Can not set line " + id + " : LINE_ID_EXITING_MEMORY");
            return;
        }
        if (id < 0){
            GameApplication.log.warning("MessageDisplay -> Can not set line " + id + " : LINE_ID_INVALID");
            return;
        }
        messageContent[id] = line;
    }

    public List<GuiButton> getButtons(){
        return buttons;
    }

    public List<String> getContent(){
        List<String> list = new ArrayList<String>(4);
        for (int i = 0 ; i < 4 ; i++){
            String s = messageContent[i];
            if (s == null){
                list.add("");
            } else {
                list.add(s);
            }
        }
        return list;
    }

    public String getTexturePath(){
        return texturePath;
    }
}
