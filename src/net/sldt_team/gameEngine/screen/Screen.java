package net.sldt_team.gameEngine.screen;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.components.ScreenComponent;
import net.sldt_team.gameEngine.components.QuitButton;
import net.sldt_team.gameEngine.ext.Translator;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.renderengine.Texture;
import net.sldt_team.gameEngine.screen.event.ComponentsEventProvider;
import net.sldt_team.gameEngine.screen.event.MessagesEventProvider;
import net.sldt_team.gameEngine.screen.event.RendersEventProvider;
import net.sldt_team.gameEngine.screen.message.Message;
import net.sldt_team.gameEngine.screen.message.MessageType;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public abstract class Screen {

    /**
     * An instance of GameApplication
     */
    protected GameApplication theGame;

    private Texture backgroundImage;
    private List<ScreenComponent> screenComponents;
    private final ArrayList<Runnable> messageQueue = new ArrayList<Runnable>();

    /**
     * Whenever or not the screen buttons are enabled
     */
    public boolean areControlsEnabled = true;

    /**
     * Whenever or not the cursor should be displayed
     */
    public boolean showCursor = true;

    private MessageDisplay displayedMessage = null;

    public Screen() {
        screenComponents = new ArrayList<ScreenComponent>();
    }

    /**
     * Adds a component to the screen
     */
    public void addComponentToScreen(final ScreenComponent component) {
        if (this instanceof ComponentsEventProvider) {
            ComponentsEventProvider provider = (ComponentsEventProvider) this;
            if (!provider.canComponentAdd(component)) {
                return;
            }
        }

        Runnable r = new Runnable() {
            public void run() {
                component.onComponentAdd();
                screenComponents.add(component);
                if (Screen.this instanceof ComponentsEventProvider) {
                    ComponentsEventProvider provider = (ComponentsEventProvider) Screen.this;
                    provider.onComponentAdded(component);
                }
            }
        };
        messageQueue.add(r);
    }

    /**
     * Returns components count
     */
    public int getComponentsCount() {
        return screenComponents.size();
    }

    /**
     * Removes a component from the screen
     */
    public void removeComponentFromScreen(final ScreenComponent component) {
        if (this instanceof ComponentsEventProvider) {
            ComponentsEventProvider provider = (ComponentsEventProvider) this;
            if (!provider.canComponentRemove(component)) {
                return;
            }
        }

        Runnable r = new Runnable() {
            public void run() {
                component.onComponentRemove();
                screenComponents.remove(component);
                if (Screen.this instanceof ComponentsEventProvider) {
                    ComponentsEventProvider provider = (ComponentsEventProvider) Screen.this;
                    provider.onComponentRemoved();
                }
            }
        };
        messageQueue.add(r);
    }

    /**
     * @exclude
     */
    public void doInit() {
        backgroundImage = theGame.renderEngine.loadTexture("backgrounds/mainBG");
        initScreen();
    }

    /**
     * Displays a message at the middle of the screen
     */
    public void displayMessage(Message message) {
        if (this instanceof MessagesEventProvider) {
            MessagesEventProvider provider = (MessagesEventProvider) this;
            if (!provider.canMessageDialogDisplay(message)) {
                return;
            }
        }

        displayedMessage = new MessageDisplay(message, this, theGame.renderEngine);

        if (this instanceof MessagesEventProvider) {
            MessagesEventProvider provider = (MessagesEventProvider) this;
            provider.onMessageDialogDisplayed(message);
        }
    }

    /**
     * Removes the current displayed message
     */
    public void clearDesplayedMessage() {
        if (this instanceof MessagesEventProvider) {
            MessagesEventProvider provider = (MessagesEventProvider) this;
            if (!provider.canMessageDialogClear(displayedMessage.theMessage)) {
                return;
            }
        }

        displayedMessage = null;

        if (this instanceof MessagesEventProvider) {
            MessagesEventProvider provider = (MessagesEventProvider) this;
            provider.onMessageDialogCleared();
        }
    }

    /**
     * Adds a quit button
     */
    public void addQuitButton() {
        final Runnable action = new Runnable() {
            public void run() {
                theGame.closeGame();
            }
        };
        QuitButton quit = new QuitButton(Translator.instance.translate("screen.quit"), GameApplication.getScreenWidth() - 100, GameApplication.getScreenHeight() - 90, 60, 60, theGame.renderEngine);
        Runnable al123 = new Runnable() {
            public void run() {
                displayMessage(new Message(MessageType.CONFIRMATION, theGame.getGameName(), "Are you sure you realy want to exit this game ?", action));
            }
        };
        quit.setButtonAction(al123);
        addComponentToScreen(quit);
    }

    /**
     * @exclude
     */
    public void setGame(GameApplication game) {
        theGame = game;
    }

    /**
     * Refrechs the screen
     */
    public void refreshScreen() {
        Runnable r = new Runnable() {
            public void run() {
                for (ScreenComponent component : screenComponents) {
                    component.onComponentRemove();
                }
                screenComponents.clear();
            }
        };
        messageQueue.add(r);

        initScreen();
    }

    /**
     * @exclude
     */
    public void onExit() {
        Runnable r = new Runnable() {
            public void run() {
                for (ScreenComponent component : screenComponents) {
                    component.onComponentRemove();
                }
                screenComponents.clear();
                screenComponents = null;
            }
        };
        messageQueue.add(r);

        onExitingScreen();
    }

    /**
     * @exclude
     */
    public void onTick() {
        if (displayedMessage != null) {
            displayedMessage.updateMessage();
            return;
        }

        if (areControlsEnabled) {
            for (ScreenComponent g : screenComponents) {
                g.updateComponent();
            }
        }

        for (Runnable r : messageQueue) {
            r.run();
        }
        messageQueue.clear();

        updateScreen();
    }

    /**
     * @exclude
     */
    public void drawWindow(RenderEngine renderEngine, FontRenderer fontRenderer) {
        if (this instanceof RendersEventProvider) {
            RendersEventProvider provider = (RendersEventProvider) this;
            provider.preRenderScreen(renderEngine, fontRenderer);
        } else {
            renderEngine.bindTexture(backgroundImage);
            renderEngine.renderQuad(10, 10, GameApplication.getScreenWidth() - 20, GameApplication.getScreenHeight() - 20);
        }

        renderScreen(renderEngine, fontRenderer);

        for (ScreenComponent g : screenComponents) {
            if (g != null)
                g.renderComponent(renderEngine, fontRenderer);
        }

        if (theGame.gameSettings.showFPS) {
            fontRenderer.setRenderingSize(4);
            fontRenderer.setRenderingColor(ColorHelper.RED);
            fontRenderer.renderString("FPS : " + theGame.getGameFPS(), 0, 50);
        }

        if (!showCursor) {
            Mouse.setGrabbed(true);
        } else {
            Mouse.setGrabbed(false);
        }

        if (displayedMessage != null) {
            displayedMessage.renderMessage(renderEngine, fontRenderer);
        }

        if (this instanceof RendersEventProvider) {
            RendersEventProvider provider = (RendersEventProvider) this;
            provider.postRenderScreen(renderEngine, fontRenderer);
        }
    }

    /**
     * Called when a tick is dispatched to the screen
     */
    protected abstract void updateScreen();

    /**
     * Called when screen is rendered
     */
    protected abstract void renderScreen(RenderEngine renderEngine, FontRenderer fontRenderer);

    /**
     * Called when screen is initialized (used to add all default components)
     */
    protected abstract void initScreen();

    /**
     * Called when user exits this screen
     */
    protected abstract void onExitingScreen();

    /**
     * Used by components as an event system (example : when button get clicked)
     * WARNING : Engine does not automaticaly call this method, a game component must import this method
     */
    public abstract void actionPerformed(int compID, ScreenComponent component);
}
