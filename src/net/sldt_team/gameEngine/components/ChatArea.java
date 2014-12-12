package net.sldt_team.gameEngine.components;

import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class ChatArea implements ScreenComponent {

    private int areaX;
    private int areaY;
    private int areaWidth;
    private int areaHeight;

    private int missagesNumber;

    private int maxMessages = 24;

    private int scrollNumber;

    private List<String> areaContent;
    private List<String> renderedAreaContent;

    /**
     * Initializes a new Chat area
     * @param x X
     * @param y Y
     * @param width Width
     * @param height Height
     */
    public ChatArea(int x, int y, int width, int height) {
        areaX = x;
        areaY = y;
        areaWidth = width;
        areaHeight = height;
        areaContent = new ArrayList<String>();
        renderedAreaContent = new ArrayList<String>();
        maxMessages = height / 30;
    }

    public void onComponentRemove() {
    }

    public void onComponentAdd() {
    }

    public int getX() {
        return areaX;
    }

    public int getY() {
        return areaY;
    }

    public void renderComponent(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderEngine.bindColor(ColorHelper.WHITE);
        renderEngine.renderQuad(areaX, areaY, areaWidth, areaHeight);
        fontRenderer.setRenderingColor(ColorHelper.BLACK);
        int j = 0;
        for (int i = 0; i < renderedAreaContent.size(); i++) {
            String current = renderedAreaContent.get(i);
            fontRenderer.renderString(current, areaX + 15, areaY + j);
            j += 30;
        }
    }

    public void updateComponent() {
        if (missagesNumber <= maxMessages) {
            return;
        }
        int i = Mouse.getDWheel();
        if (i < 0) {
            System.out.println("scrollUp();");
            scrollUp();
        }
        if (i > 0) {
            System.out.println("scrollDown();");
            scrollDown();
        }
    }

    private void scrollUp() {
        if (scrollNumber == (missagesNumber - maxMessages)) {
            return;
        }
        scrollNumber++;
        updateChat();
    }

    private void scrollDown() {
        if (scrollNumber == 0) {
            return;
        }
        scrollNumber--;
        updateChat();
    }

    private void updateChat() {
        renderedAreaContent.clear();
        int done = (maxMessages - 1);
        int parser = scrollNumber;
        while ((done--) > -1) {
            renderedAreaContent.add(areaContent.get(parser));
            parser++;
        }
    }

    /**
     * Adds a line to this area
     */
    public void addLine(String line) {
        areaContent.add(line);
        missagesNumber++;
        if (missagesNumber <= maxMessages) {
            renderedAreaContent.add(line);
        } else if (missagesNumber > maxMessages) {
            scrollNumber = missagesNumber - maxMessages;
            updateChat();
        }
    }
}
