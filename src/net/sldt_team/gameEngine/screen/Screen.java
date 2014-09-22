package net.sldt_team.gameEngine.screen;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.controls.ComponentAction;
import net.sldt_team.gameEngine.controls.GameComponent;
import net.sldt_team.gameEngine.controls.QuitButton;
import net.sldt_team.gameEngine.ext.Translator;
import net.sldt_team.gameEngine.renderengine.ColorRenderer;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.screen.event.MessagesEventProvider;
import net.sldt_team.gameEngine.screen.event.RendersEventProvider;
import net.sldt_team.gameEngine.screen.message.Message;
import net.sldt_team.gameEngine.screen.message.MessageDisplay;
import net.sldt_team.gameEngine.screen.message.MessageType;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public abstract class Screen implements IScreen {

    /** An instance of GameApplication */
    protected GameApplication theGame;

    private int backgroundImage;
    private List<GameComponent> windowComponents;
    private final ArrayList<Runnable> messageQueue = new ArrayList<Runnable>();

    /** Whenever or not the screen buttons are enabled */
    public boolean areControlsEnabled = true;

    /** Whenever or not the cursor should be displayed */
    public boolean showCursor = true;

    private MessageDisplay displayedMessage = null;

    public Screen() {
        windowComponents = new ArrayList<GameComponent>();
    }

    /**
     * Adds a component to the screen
     */
    public void addComponentToWindow(final GameComponent component) {
        Runnable r = new Runnable() {
            public void run() {
                component.onComponentAdd();
                windowComponents.add(component);
            }
        };
        messageQueue.add(r);
    }

    /**
     * Removes a component from the screen
     */
    public void removeComponentFromWindow(final GameComponent component) {
        Runnable r = new Runnable() {
            public void run() {
                component.onComponentRemove();
                windowComponents.remove(component);
            }
        };
        messageQueue.add(r);
    }

    public void initWindow() {
        backgroundImage = theGame.renderEngine.loadTexture("backgrounds/mainBG.png");
        initScreen();
    }

    /**
     * Displays a message at the middle of the screen
     */
    public void displayMessage(Message message){
        if (this instanceof MessagesEventProvider){
            MessagesEventProvider provider = (MessagesEventProvider)this;
            if (!provider.canMessageDialogDisplay(message)) {
                return;
            }
        }

        displayedMessage = new MessageDisplay(message, this, theGame.renderEngine);

        if (this instanceof MessagesEventProvider) {
            MessagesEventProvider provider = (MessagesEventProvider)this;
            provider.onMessageDialogDisplayed(message);
        }
    }

    /**
     * Removes the current displayed message
     */
    public void clearDesplayedMessage(){
        if (this instanceof MessagesEventProvider){
            MessagesEventProvider provider = (MessagesEventProvider)this;
            if (!provider.canMessageDialogClear(displayedMessage.theMessage)) {
                return;
            }
        }

        displayedMessage = null;

        if (this instanceof MessagesEventProvider) {
            MessagesEventProvider provider = (MessagesEventProvider)this;
            provider.onMessageDialogCleared();
        }
    }

    /**
     * Adds a quit button
     */
    public void addQuitButton() {
        final ComponentAction action = new ComponentAction() {
            public void actionPerformed() {
                theGame.closeGame();
            }
        };
        QuitButton quit = new QuitButton(Translator.instance.translate("screen.quit"), GameApplication.getScreenWidth() - 100, GameApplication.getScreenHeight() - 90, 60, 60, theGame.renderEngine);
        ComponentAction al123 = new ComponentAction() {
            public void actionPerformed() {
                displayMessage(new Message(MessageType.CONFIRMATION, theGame.getGameName(), "Are you sure you realy want to exit this game ?", action));
            }
        };
        quit.setButtonAction(al123);
        windowComponents.add(quit);
        addComponentToWindow(quit);
    }

    public void setGame(GameApplication game) {
        theGame = game;
    }

    /**
     * Refrechs the screen
     */
    public void refreshScreen(){
        Runnable r = new Runnable() {
            public void run() {
                for (GameComponent component : windowComponents) {
                    component.onComponentRemove();
                }
                windowComponents.clear();
            }
        };
        messageQueue.add(r);

        initScreen();
    }

    public void onExit() {
        Runnable r = new Runnable() {
            public void run() {
                for (GameComponent component : windowComponents) {
                    component.onComponentRemove();
                }
                windowComponents.clear();
                windowComponents = null;
            }
        };
        messageQueue.add(r);

        onExitingScreen();
    }

    public void onTick() {
        if (displayedMessage != null){
            displayedMessage.updateMessage();
            return;
        }

        if (areControlsEnabled) {
            for (GameComponent g : windowComponents) {
                g.updateComponent();
            }
        }

        for (Runnable r : messageQueue) {
             r.run();
        }
        messageQueue.clear();

        updateScreen();
    }

    public void drawWindow(RenderEngine renderEngine, FontRenderer fontRenderer) {
        if (this instanceof RendersEventProvider){
            RendersEventProvider provider = (RendersEventProvider)this;
            provider.preRenderScreen(renderEngine, fontRenderer);
        } else {
            renderEngine.bindTexture(backgroundImage);
            renderEngine.renderQuad(10, 10, GameApplication.getScreenWidth() - 20, GameApplication.getScreenHeight() - 20);
        }

        renderScreen(renderEngine, fontRenderer);

        for (GameComponent g : windowComponents) {
            if (g != null)
                g.renderComponent(renderEngine, fontRenderer);
        }

        if (theGame.gameSettings.showFPS) {
            fontRenderer.setRenderingSize(4);
            fontRenderer.setRenderingColor(ColorRenderer.RED);
            fontRenderer.renderString("FPS : " + theGame.finalFPS, 0, 50);
        }

        if (!showCursor) {
            Mouse.setGrabbed(true);
        } else {
            Mouse.setGrabbed(false);
        }

        if (displayedMessage != null){
            displayedMessage.renderMessage(renderEngine, fontRenderer);
        }

        if (this instanceof RendersEventProvider){
            RendersEventProvider provider = (RendersEventProvider)this;
            provider.postRenderScreen(renderEngine, fontRenderer);
        }
    }

    /**
     * Called when a tick is dispatched to the screen
     */
    public abstract void updateScreen();

    /**
     * Called when screen is rendered
     */
    public abstract void renderScreen(RenderEngine renderEngine, FontRenderer fontRenderer);

    /**
     * Called when screen is initialized (used to add all default components)
     */
    public abstract void initScreen();

    /**
     * Called when user exits this screen
     */
    public abstract void onExitingScreen();
}
