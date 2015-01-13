package net.sldt_team.gameEngine.screen;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.gui.GuiButton;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.renderengine.Texture;
import net.sldt_team.gameEngine.screen.message.Message;
import net.sldt_team.gameEngine.util.GuiUtilities;

/**
 * @exclude
 */
public class MessageDisplay {

    protected Message theMessage;

    protected MessageDisplay(Message message, Screen screen, RenderEngine renderEngine) {
        theMessage = message;

        theMessage.init(renderEngine, screen);
    }

    protected void renderMessage(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderEngine.bindColor(new ColorHelper(0, 0, 0, 128));
        renderEngine.renderQuad(0, 0, GameApplication.getScreenWidth(), GameApplication.getScreenHeight());

        Texture i = renderEngine.loadTexture("message/dialog");
        renderEngine.bindTexture(i);
        renderEngine.renderQuad(GuiUtilities.getCenteredObjectX(512), GuiUtilities.getCenteredObjectY(256), 512, 256);

        fontRenderer.setRenderingSize(5);
        fontRenderer.setRenderingColor(ColorHelper.WHITE);
        fontRenderer.renderString(theMessage.getMessageTitle(), GuiUtilities.getCenteredObjectX(512) + 5, GuiUtilities.getCenteredObjectY(256));

        for (GuiButton button : theMessage.getButtons()) {
            button.render(renderEngine, fontRenderer);
        }

        int spacement = 0;
        for (String s : theMessage.getContent()) {
            fontRenderer.setRenderingSize(4);
            fontRenderer.setRenderingColor(ColorHelper.BLACK);
            fontRenderer.renderString(s, GuiUtilities.getCenteredObjectX(512) + 100, GuiUtilities.getCenteredObjectY(256) + (50 + spacement));
            spacement += 24;
        }

        Texture j = renderEngine.loadTexture(theMessage.getTexturePath());
        renderEngine.bindTexture(j);
        renderEngine.renderQuad(GuiUtilities.getCenteredObjectX(512) + 15, GuiUtilities.getCenteredObjectY(256) + 50, 64, 64);
    }

    protected void updateMessage() {
        for (GuiButton button : theMessage.getButtons()) {
            button.update();
        }
    }
}
