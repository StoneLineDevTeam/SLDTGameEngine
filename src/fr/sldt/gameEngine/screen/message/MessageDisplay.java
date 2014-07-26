package fr.sldt.gameEngine.screen.message;

import fr.sldt.gameEngine.GameApplication;
import fr.sldt.gameEngine.gui.GuiButton;
import fr.sldt.gameEngine.renderengine.ColorRenderer;
import fr.sldt.gameEngine.renderengine.FontRenderer;
import fr.sldt.gameEngine.renderengine.RenderEngine;
import fr.sldt.gameEngine.screen.Screen;
import fr.sldt.gameEngine.util.MathUtilities;

public class MessageDisplay {

    public Message theMessage;

    public MessageDisplay(Message message, Screen screen, RenderEngine renderEngine){
        theMessage = message;

        theMessage.init(renderEngine, screen);
    }

    public void renderMessage(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderEngine.bindColor(new ColorRenderer(0, 0, 0, 128));
        renderEngine.renderQuad(0, 0, GameApplication.getScreenWidth(), GameApplication.getScreenHeight());

        int i = renderEngine.loadTexture("message/dialog.png");
        renderEngine.bindTexture(i);
        renderEngine.renderQuad(MathUtilities.getCenteredObjectX(512), MathUtilities.getCenteredObjectY(256), 512, 256);

        fontRenderer.setRenderingSize(5);
        fontRenderer.setRenderingColor(ColorRenderer.WHITE);
        fontRenderer.renderString(theMessage.getMessageTitle(), MathUtilities.getCenteredObjectX(512) + 5, MathUtilities.getCenteredObjectY(256));

        for (GuiButton button : theMessage.getButtons()){
            button.render(renderEngine, fontRenderer);
        }

        int spacement = 0;
        for (String s : theMessage.getContent()){
            fontRenderer.setRenderingSize(4);
            fontRenderer.setRenderingColor(ColorRenderer.BLACK);
            fontRenderer.renderString(s, MathUtilities.getCenteredObjectX(512) + 100, MathUtilities.getCenteredObjectY(256) + (50 + spacement));
            spacement += 24;
        }

        int j = renderEngine.loadTexture(theMessage.getTexturePath());
        renderEngine.bindTexture(j);
        renderEngine.renderQuad(MathUtilities.getCenteredObjectX(512) + 15, MathUtilities.getCenteredObjectY(256) + 50, 64, 64);
    }

    public void updateMessage() {
        for (GuiButton button : theMessage.getButtons()){
            button.update();
        }
    }
}
