package net.sldt_team.gameEngine.screen;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.gui.GuiButton;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.renderengine.Material;
import net.sldt_team.gameEngine.screen.message.Message;
import net.sldt_team.gameEngine.util.GuiUtilities;
import net.sldt_team.gameEngine.util.Vector2D;

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

        Material i = renderEngine.getMaterial("message/dialog");
        renderEngine.bindMaterial(i);

        Vector2D centeredObject = GuiUtilities.getCenteredObject(512, 256);

        renderEngine.renderQuad((float)centeredObject.getX(), (float)centeredObject.getY(), 512, 256);

        fontRenderer.setRenderingSize(5);
        fontRenderer.setRenderingColor(ColorHelper.WHITE);
        fontRenderer.renderString(theMessage.getMessageTitle(), (float)centeredObject.getX() + 5, (float)centeredObject.getY());

        for (GuiButton button : theMessage.getButtons()) {
            button.render(renderEngine, fontRenderer);
        }

        int spacement = 0;
        for (String s : theMessage.getContent()) {
            fontRenderer.setRenderingSize(4);
            fontRenderer.setRenderingColor(ColorHelper.BLACK);
            fontRenderer.renderString(s, (float)centeredObject.getX() + 100, (float)centeredObject.getY() + (50 + spacement));
            spacement += 24;
        }

        Material j = renderEngine.getMaterial(theMessage.getTexturePath());
        renderEngine.bindMaterial(j);
        renderEngine.renderQuad((float)centeredObject.getX() + 15, (float)centeredObject.getY() + 50, 64, 64);
    }

    protected void updateMessage() {
        for (GuiButton button : theMessage.getButtons()) {
            button.update();
        }
    }
}
