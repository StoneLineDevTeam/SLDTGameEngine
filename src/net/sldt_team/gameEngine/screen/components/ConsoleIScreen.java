package net.sldt_team.gameEngine.screen.components;

import net.sldt_team.gameEngine.gui.GuiButton;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.renderengine.Texture;
import net.sldt_team.gameEngine.util.StringUtilities;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class ConsoleIScreen implements IScreenComponent {

    private int areaX;
    private int areaY;
    private int areaWidth;
    private int areaHeight;

    private int messagesNumber;

    private int maxLines = 24;

    private int scrollNumber;

    private List<String> areaContent;
    private List<String> renderedAreaContent;

    private GuiButton scrollUp;
    private GuiButton scrollDown;

    private int maxCharsPerLine = 0;

    /**
     * Initializes a new console screen
     * @param x X
     * @param y Y
     * @param width Width
     * @param height Height
     * @param renderEngine The rendering engine
     */
    public ConsoleIScreen(int x, int y, int width, int height, RenderEngine renderEngine) {
        areaX = x;
        areaY = y;
        areaWidth = width;
        areaHeight = height;
        areaContent = new ArrayList<String>();
        renderedAreaContent = new ArrayList<String>();
        maxLines = (height / 25);
        maxCharsPerLine = width / 9;

        /** Gui buttons to scroll up and down */
        Texture i = renderEngine.loadTexture("components/consoleScreen_scrollUp");
        Texture j = renderEngine.loadTexture("components/consoleScreen_scrollDown");
        scrollUp = new GuiButton("", 128, 128, i, (x + width) + 5, (y + 5), 32, 32);
        scrollDown = new GuiButton("", 128, 128, j, (x + width) + 5, (y + height) - (32 + 5), 32, 32);
        Runnable up = new Runnable() {
            public void run() {
                scrollDown();
            }
        };
        scrollUp.setAction(up);
        Runnable down = new Runnable() {
            public void run() {
                scrollUp();
            }
        };
        scrollDown.setAction(down);
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
        fontRenderer.setRenderingSize(4);
        int j = 0;
        for (int i = 0; i < renderedAreaContent.size(); i++) {
            String current = renderedAreaContent.get(i);
            fontRenderer.renderString(current, areaX + 15, areaY + j);
            j += 25;
        }

        renderEngine.bindColor(ColorHelper.GREEN);
        float number = scrollNumber * (areaHeight - maxLines) / messagesNumber;
        //int graphicalPos = scrollNumber * (areaHeight) / missagesNumber;
        if ((areaY + number) > ((areaY + areaHeight) - 64)) {
            number = (areaHeight - 64);
        }

        renderEngine.renderQuad((areaX + areaWidth) + 5, (areaY + number), 32, 64);

        scrollUp.render(renderEngine, fontRenderer);
        scrollDown.render(renderEngine, fontRenderer);

        checkWheelScroll();
    }

    public void updateComponent() {
        if (messagesNumber <= maxLines) {
            return;
        }

        scrollUp.update();
        scrollDown.update();
    }

    private void checkWheelScroll() {
        if (messagesNumber <= maxLines) {
            return;
        }
        int i = Mouse.getDWheel();
        if (i < 0) {
            //System.out.println("scrollUp();");
            scrollUp();
        }
        if (i > 0) {
            //System.out.println("scrollDown();");
            scrollDown();
        }
    }

    private void scrollUp() {
        if (scrollNumber == (messagesNumber - maxLines)) {
            return;
        }
        scrollNumber++;
        updateConsole();
    }

    private void scrollDown() {
        if (scrollNumber == 0) {
            return;
        }
        scrollNumber--;
        updateConsole();
    }

    private void updateConsole() {
        renderedAreaContent.clear();
        int done = (maxLines - 1);
        int parser = scrollNumber;
        while ((done--) > -1) {
            renderedAreaContent.add(areaContent.get(parser));
            parser++;
        }
    }

    /**
     * Adds a line to this console
     */
    public void addLine(String line) {
        if (line.length() > maxCharsPerLine) {
            List<String> list = StringUtilities.stringWarp(line, maxCharsPerLine);
            areaContent.addAll(list);
            messagesNumber += list.size();
        } else {
            areaContent.add(line);
            messagesNumber++;
        }
        if (messagesNumber <= maxLines) {
            renderedAreaContent.add(line);
        } else if (messagesNumber > maxLines) {
            scrollNumber = messagesNumber - maxLines;
            updateConsole();
        }
    }
}
