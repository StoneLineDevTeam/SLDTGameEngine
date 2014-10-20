package net.sldt_team.gameEngine;

import net.sldt_team.gameEngine.exception.code.ErrorCode008;
import net.sldt_team.gameEngine.ext.EnumOS2;
import net.sldt_team.gameEngine.ext.EnumOSMappingHelper;
import net.sldt_team.gameEngine.ext.gameSettings.GameSettings;
import net.sldt_team.gameEngine.ext.Session;
import net.sldt_team.gameEngine.exception.GameException;
import net.sldt_team.gameEngine.exception.code.ErrorCode006;
import net.sldt_team.gameEngine.logging.LoggerHandler;
import net.sldt_team.gameEngine.logging.ConsoleHandlerFormator;
import net.sldt_team.gameEngine.logging.OutputStreamToLogger;
import net.sldt_team.gameEngine.particle.ParticleManager;
import net.sldt_team.gameEngine.renderengine.*;
import net.sldt_team.gameEngine.renderengine.assetSystem.AssetType;
import net.sldt_team.gameEngine.screen.Screen;
import net.sldt_team.gameEngine.sound.SoundManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.*;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GameApplication implements IGame, Runnable {

    /** Display settings */
    private static boolean isWindowed = true;
    private static int sizeX = 1600;
    private static int sizeY = 900;

    //The SLDTD first page displayed at game init
    private String background;

    //Game timer
    private Timer gameTimer;

    /** FPS calculation */
    private int fps;
    private long lastFPS;
    public int finalFPS;

    /** The rendering system */
    public RenderEngine renderEngine;
    public FontRenderer fontRenderer;
    public ParticleManager particleManager;

    //Player Session
    public Session playerSession;

    //The Sound manager
    public SoundManager soundManager;

    //Game Settings
    public GameSettings gameSettings;

    //The current screen
    private Screen currentFrame;

    //Is game initialized ?
    private boolean initialized;

    //Is exiting current screen ?
    private boolean isWindowExiting;

    //Ticks calcultion used to display the SLDT's GameEngine page
    private int initTime;

    //Game logs
    public List<String> logs = new ArrayList<String>();

    //Game logger
    public static Logger log;

    //The name of app directory
    private static String appDirName;

    //Game exception handler
    private ExceptionHandler exceptionHandler;

    //The game name & version
    private String gameName;
    private String gameVersion;

    //The font name
    private String fontName = "normal";

    //SLDT powered by background scaling system
    private float rotate = 0.0F;
    private float scale = 0.0F;
    private int sldtTicks;
    private int sldtScaleTicks;

    //The timer power
    private float timer;

    /**
     * @param loggerName The name of the game logger
     * @param session The player session if this game uses multiplayer or if you need user/password/sessionID storage
     * @param timerPower The power of the game timer (freqence of call to update function)
     * @param dirName The game directory name (set it to #ROOT_FOLDER# for the app root folder)
     * @param exhandler How the game should handles exception thrown in the main thread
     * @param name The name of the game to display on application's title bar
     * @param version The game version (use getGameVersion() to get it)
     */
    public GameApplication(String loggerName, Session session, float timerPower, String dirName, ExceptionHandler exhandler, String name, String version){
        log = Logger.getLogger(loggerName);
        appDirName = dirName;
        exceptionHandler = exhandler;
        gameName = name;
        gameVersion = version;
        gameSettings = new GameSettings();
        playerSession = session;
        gameTimer = new Timer(1000F);
        timer = timerPower;

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new ConsoleHandlerFormator());
        log.setUseParentHandlers(false);
        log.addHandler(handler);
        log.addHandler(new LoggerHandler(this));
        log.info("GameEngine " + EngineConstants.ENGINE_VERSION);
        log.info("RenderEngine " + EngineConstants.RENDERENGINE_VERSION);
        log.info("Binding " + EngineConstants.BINDING_VERSION);
        System.setOut(new PrintStream(new OutputStreamToLogger(log, Level.INFO), true));
        System.setErr(new PrintStream(new OutputStreamToLogger(log, Level.SEVERE), true));
    }

    /**
     * Sets the default font that FontRenderer needs to use
     */
    public void setFontName(String s){
        fontName = s;
        fontRenderer.reloadFontRenderer(fontName);
    }

    /**
     * Get the system time as long (used to calculate FPS)
     */
    public static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            finalFPS = fps;
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    private void init() {
        getGameDir();

        lastFPS = getTime();
        initialized = false;
        if (getAssetsFileType() == null){
            throw new GameException(new ErrorCode008());
        }
        renderEngine = new RenderEngine(exceptionHandler, new AssetManager(getGameDir() + File.separator + "resources" + File.separator + gameName.toLowerCase(), getAssetsFileType()), log);
        fontRenderer = new FontRenderer(renderEngine, fontName);
        background = "backgrounds/sldtBG.png";
        particleManager = new ParticleManager();
        soundManager = new SoundManager(log);
        gameSettings.loadSettings();
    }

    /**
     * Returns the game directory
     */
    public static File getGameDir() {
        if (appDirName.equals("#ROOT_FOLDER#")){
            return new File(".");
        }
        return getAppDir(appDirName);
    }

    private static File getAppDir(String par0Str) {
        String s = System.getProperty("user.home", ".");
        File file;

        switch (EnumOSMappingHelper.enumOSMappingArray[getOS().ordinal()]) {
            case 1:
            case 2:
                file = new File(s, (new StringBuilder()).append('.')
                        .append(par0Str).append('/').toString());
                break;

            case 3:
                String s1 = System.getenv("APPDATA");

                if (s1 != null) {
                    file = new File(s1, (new StringBuilder()).append(".")
                            .append(par0Str).append('/').toString());
                } else {
                    file = new File(s, (new StringBuilder()).append('.')
                            .append(par0Str).append('/').toString());
                }

                break;

            case 4:
                file = new File(s, (new StringBuilder())
                        .append("Library/Application Support/").append(par0Str)
                        .toString());
                break;

            default:
                file = new File(s, (new StringBuilder()).append(par0Str)
                        .append('/').toString());
                break;
        }

        if (!file.exists() && !file.mkdirs()) {
            throw new GameException(new ErrorCode006());
        } else {
            return file;
        }
    }

    private static EnumOS2 getOS() {
        String s = System.getProperty("os.name").toLowerCase();

        if (s.contains("win")) {
            return EnumOS2.windows;
        }

        if (s.contains("mac")) {
            return EnumOS2.macos;
        }

        if (s.contains("solaris")) {
            return EnumOS2.solaris;
        }

        if (s.contains("sunos")) {
            return EnumOS2.solaris;
        }

        if (s.contains("linux")) {
            return EnumOS2.linux;
        }

        if (s.contains("unix")) {
            return EnumOS2.linux;
        } else {
            return EnumOS2.unknown;
        }
    }

    private void update() {
        if (!initialized){
            initTime += 1;
            sldtTicks++;
            sldtScaleTicks++;
            if (sldtTicks >= 30){
                rotate += 0.1F;
                sldtTicks = 0;
            }
            if (sldtScaleTicks >= 60){
                scale += 0.001F;
                sldtScaleTicks = 0;
            }
            if (initTime >= 5000){
                initialized = true;
                gameTimer = new Timer(timer);
                initGame();
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
                System.exit(0);
            }
        }


        if (!isWindowExiting){
            if (currentFrame != null){
                currentFrame.onTick();
            }
        }

        particleManager.doUpdate();

        updateGameOverlay();
    }

    private void render() {
        if (!initialized){
            fontRenderer.setRenderingSize(5);
            fontRenderer.setRenderingColor(ColorRenderer.GREEN);
            fontRenderer.renderString("SLDT's GameEngine " + EngineConstants.ENGINE_VERSION, getScreenWidth() - fontRenderer.getStringWidth("SLDT's GameEngine " + EngineConstants.ENGINE_VERSION), getScreenHeight() - 64);
            Texture i = renderEngine.loadTexture(background);
            renderEngine.bindTexture(i);
            renderEngine.setRotationLevel(-rotate);
            renderEngine.setScaleLevel(1 - scale);
            renderEngine.renderQuad(10, 10, getScreenWidth() - 20, getScreenHeight() - 20);
        }

        if (!isWindowExiting){
            if (currentFrame != null){
                currentFrame.drawWindow(renderEngine, fontRenderer);
            }
        }

        if (isWindowExiting){
            renderEngine.bindColor(ColorRenderer.BLACK);
            renderEngine.renderQuad(0, 0, getScreenWidth(), getScreenHeight());
            fontRenderer.setRenderingColor(ColorRenderer.WHITE);
            fontRenderer.renderString("Loading...", 20, 20);
        }

        particleManager.doRender(renderEngine, fontRenderer);

        renderGameOverlay();
    }

    /**
     * Displays a new screen (args : screen - the new screen)
     */
    public void displayScreen(Screen screen) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        isWindowExiting = true;
        if (currentFrame != null) {
            currentFrame.onExit();
            currentFrame = null;
        }
        screen.setGame(this);
        currentFrame = screen;
        currentFrame.initWindow();
        isWindowExiting = false;
    }

    /**
     * Returns the user's screen width
     */
    public static int getScreenWidth() {
        if (isWindowed){
            return sizeX;
        }
        Toolkit tool = Toolkit.getDefaultToolkit();
        return tool.getScreenSize().width;
    }

    /**
     * Returns the user's screen height
     */
    public static int getScreenHeight() {
        if (isWindowed){
            return sizeY;
        }
        Toolkit tool = Toolkit.getDefaultToolkit();
        return tool.getScreenSize().height;
    }

    /**
     * Sets up the main game thread (args : game - the game witch the thread is for, name - the name of the game to display in thread name,
     * settings are the default window settings like resolution and fullscreen boolean)
     */
    public static void setupThread(GameApplication game, String name, AppSettings settings){
        isWindowed = (Boolean)settings.getWindowSettings().get("SLDT_WINDOW_FULLSCREEN");
        sizeX = (Integer)settings.getWindowSettings().get("SLDT_WINDOW_WIDTH");
        sizeY = (Integer)settings.getWindowSettings().get("SLDT_WINDOW_HEIGHT");
        Thread thread = new Thread(game, name +  " Main - Thread");
        thread.setPriority(10);
        thread.start();
    }

    private void initOpenGL(){
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, getScreenWidth(), getScreenHeight(), 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    private void onGameCrash(Exception e){
        if (! (e instanceof GameException)) {
            exceptionHandler.handleException(new GameException(e));
        } else {
            exceptionHandler.handleException((GameException) e);
        }
    }

    /**
     * Called when the game is shutting down
     */
    public void closeGame() {
        log.info("Stopping game...");
        gameSettings.saveSettings();
        if (currentFrame != null) {
            currentFrame.onExit();
        }
        exitGame();
        log.info("Closing OpenAL...");
        soundManager.onClosingGame();
        AL.destroy();
        log.info("OpenAL closed !");
        log.info("Closing OpenGL frame...");
        Display.destroy();
        log.info("Good bye !!!");
        System.exit(0);
    }

    private DisplayMode findDisplayMode(int width, int height, int bpp) throws LWJGLException {
        DisplayMode[] modes = Display.getAvailableDisplayModes();
        for ( DisplayMode mode : modes ) {
            if ( mode.getWidth() == width && mode.getHeight() == height && mode.getBitsPerPixel() >= bpp && mode.getFrequency() <= 60 ) {
                return mode;
            }
        }
        return Display.getDesktopDisplayMode();
    }

    protected void refreshOpenGLDisplay() throws LWJGLException, IOException {
        Display.destroy();
        if (isWindowed) {
            Display.setDisplayMode(findDisplayMode(sizeX, sizeY, Display.getDisplayMode().getBitsPerPixel()));
        } else {
            Display.setDisplayModeAndFullscreen(findDisplayMode(sizeX, sizeY, Display.getDisplayMode().getBitsPerPixel()));
        }
        ByteBuffer[] shit = new ByteBuffer[2];
        if (getIconPackage() == null) {
            shit[0] = renderEngine.mountTexture(GameApplication.class.getResourceAsStream("icon16.png"));
            shit[1] = renderEngine.mountTexture(GameApplication.class.getResourceAsStream("icon32.png"));
        } else {
            shit[0] = renderEngine.mountTexture(ClassLoader.getSystemResourceAsStream("./" + getIconPackage() + "/gameIcon16.png"));
            shit[1] = renderEngine.mountTexture(ClassLoader.getSystemResourceAsStream("./" + getIconPackage() + "/gameIcon32.png"));
        }
        Display.setIcon(shit);
        Display.create();
        initOpenGL();
        if (getAssetsFileType() == null){
            throw new GameException(new ErrorCode008());
        }
        renderEngine = new RenderEngine(exceptionHandler, new AssetManager(getGameDir() + File.separator + "resources" + File.separator + gameName.toLowerCase(), getAssetsFileType()), log);
        fontRenderer = new FontRenderer(renderEngine, fontName);
        /*if (currentFrame != null) {
            currentFrame.refreshScreen();
        }*/
    }

    /**
     * Updates the display mode (resolution and fullscreen)
     */
    public void updateGameDisplay(AppSettings settings){
        try {
            isWindowed = (Boolean)settings.getWindowSettings().get("SLDT_WINDOW_FULLSCREEN");
            sizeX = (Integer)settings.getWindowSettings().get("SLDT_WINDOW_WIDTH");
            sizeY = (Integer)settings.getWindowSettings().get("SLDT_WINDOW_HEIGHT");
            refreshOpenGLDisplay();
        } catch (LWJGLException e){
            onGameCrash(e);
        } catch (IOException e) {
            onGameCrash(e);
        }
    }

    public void run() {
        try {
            if (isWindowed) {
                Display.setDisplayMode(findDisplayMode(sizeX, sizeY, Display.getDisplayMode().getBitsPerPixel()));
            } else {
                Display.setDisplayModeAndFullscreen(findDisplayMode(sizeX, sizeY, Display.getDisplayMode().getBitsPerPixel()));
            }
            Display.setTitle(gameName);
            Display.create();
            log.info("Starting LWJGL - OpenAL...");
            AL.create();
            log.info("LWJGL - OpenAL successfully started !");
            Mouse.create();
            Keyboard.create();
            initOpenGL();
            init();
            ByteBuffer[] shit = new ByteBuffer[2];
            if (getIconPackage() == null) {
                shit[0] = renderEngine.mountTexture(GameApplication.class.getResourceAsStream("icon16.png"));
                shit[1] = renderEngine.mountTexture(GameApplication.class.getResourceAsStream("icon32.png"));
            } else {
                shit[0] = renderEngine.mountTexture(ClassLoader.getSystemResourceAsStream("./" + getIconPackage() + "/gameIcon16.png"));
                shit[1] = renderEngine.mountTexture(ClassLoader.getSystemResourceAsStream("./" + getIconPackage() + "/gameIcon32.png"));
            }
            Display.setIcon(shit);
            //Mouse.setNativeCursor(new org.lwjgl.input.Cursor(16, 16, 0, 0, 1, getHandMousePointer(), null));
            while (!Display.isCloseRequested()){
                Display.update();
                gameTimer.updateTimer();
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                for (int j = 0; j < gameTimer.elapsedTicks; j++) {
                    update();
                }
                render();

                updateFPS();
            }
            closeGame();
        } catch (Exception e){
            onGameCrash(e);
        }
    }

    /**
     * Returns the current displayed screen
     */
    public Screen getCurrentFrame(){
        return currentFrame;
    }

    /**
     * Returns the game name
     */
    public String getGameName(){
        if (gameName == null){
            return "Game";
        }
        return gameName;
    }

    /**
     * Returns the game version
     */
    public String getGameVersion(){
        if (gameVersion == null){
            return "InfDev";
        }
        return gameVersion;
    }

    /**
     * Renders the game (used to render things that are not rendered by this class)
     */
    public abstract void renderGameOverlay();

    /**
     * Updates the game (used to update things that are not updated by this class)
     */
    public abstract void updateGameOverlay();

    /**
     * Called when the game intializis
     */
    public abstract void initGame();

    /**
     * Called when the game shuts down
     */
    public abstract void exitGame();

    /**
     * Called to get the icon package of the game (package were gameIcon16.png and gameIcon32.png exists)
     * Optional, it can return null
     */
    public abstract String getIconPackage();

    /**
     * Get the assets file type (WAIRNING : not optional, may crash the game if null)
     */
    public abstract AssetType getAssetsFileType();
}
