package net.sldt_team.gameEngine.controls;

import net.sldt_team.gameEngine.gui.GuiButton;
import net.sldt_team.gameEngine.renderengine.ColorRenderer;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class ConsoleScreen implements GameComponent {

    private int areaX;
    private int areaY;
    private int areaWidth;
    private int areaHeight;

    private int missagesNumber;

    private int maxLines = 24;

    private int scrollNumber;

    private List<String> areaContent;
    private List<String> renderedAreaContent;

    private GuiButton scrollUp;
    private GuiButton scrollDown;

    public ConsoleScreen(int x, int y, int width, int height, RenderEngine renderEngine) {
        areaX = x;
        areaY = y;
        areaWidth = width;
        areaHeight = height;
        areaContent = new ArrayList<String>();
        renderedAreaContent = new ArrayList<String>();
        maxLines = (height / 25);

        /** Gui buttons to scroll up and down */
        int i = renderEngine.loadTexture("components/consoleScreen_scrollUp.png");
        int j = renderEngine.loadTexture("components/consoleScreen_scrollDown.png");
        scrollUp = new GuiButton("", i, i, (x + width) + 5, (y + 5), 32, 32);
        scrollDown = new GuiButton("", j, j, (x + width) + 5, (y + height) - (32 + 5), 32, 32);
        ComponentAction up = new ComponentAction() {
            public void actionPerformed() {
                scrollDown();
            }
        };
        scrollUp.setAction(up);
        ComponentAction down = new ComponentAction() {
            public void actionPerformed() {
                scrollUp();
            }
        };
        scrollDown.setAction(down);
    }

    /**
     * Called when the component is removed from a screen
     */
    public void onComponentRemove() {
    }

    /**
     * Called when this component is added to a screen
     */
    public void onComponentAdd() {
    }

    public int getX() {
        return areaX;
    }

    public int getY() {
        return areaY;
    }

    /**
     * Render Current Component
     */
    public void renderComponent(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderEngine.bindColor(ColorRenderer.WHITE);
        renderEngine.renderQuad(areaX, areaY, areaWidth, areaHeight);
        fontRenderer.setRenderingColor(ColorRenderer.BLACK);
        fontRenderer.setRenderingSize(4);
        int j = 0;
        for (int i = 0; i < renderedAreaContent.size(); i++) {
            String current = renderedAreaContent.get(i);
            fontRenderer.renderString(current, areaX + 15, areaY + j);
            j += 25;
        }

        renderEngine.bindColor(ColorRenderer.GREEN);
        float number = scrollNumber * (areaHeight - maxLines) / missagesNumber;
        //int graphicalPos = scrollNumber * (areaHeight) / missagesNumber;
        if ((areaY + number) > ((areaY + areaHeight) - 64)){
            number = (areaHeight - 64);
        }

        renderEngine.renderQuad((areaX + areaWidth) + 5, (areaY + number), 32, 64);

        scrollUp.render(renderEngine, fontRenderer);
        scrollDown.render(renderEngine, fontRenderer);
    }

    /**
     * Update the current Component
     */
    public void updateComponent() {
        if (missagesNumber <= maxLines) {
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

        scrollUp.update();
        scrollDown.update();
    }

    private void scrollUp() {
        if (scrollNumber == (missagesNumber - maxLines)) {
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

    public void addLine(String line) {
        areaContent.add(line);
        missagesNumber++;
        if (missagesNumber <= maxLines) {
            renderedAreaContent.add(line);
        } else if (missagesNumber > maxLines) {
            scrollNumber = missagesNumber - maxLines;
            updateConsole();
        }
    }
}
