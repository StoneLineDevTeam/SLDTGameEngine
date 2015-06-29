package net.sldt_team.gameEngine;

import net.sldt_team.gameEngine.exception.code.ErrorCode008;
import net.sldt_team.gameEngine.misc.EnumOS2;
import net.sldt_team.gameEngine.misc.EnumOSMappingHelper;
import net.sldt_team.gameEngine.misc.Translator;
import net.sldt_team.gameEngine.misc.gameSettings.GameSettings;
import net.sldt_team.gameEngine.misc.Session;
import net.sldt_team.gameEngine.exception.GameException;
import net.sldt_team.gameEngine.exception.code.ErrorCode006;
import net.sldt_team.gameEngine.logging.LoggerHandler;
import net.sldt_team.gameEngine.logging.ConsoleHandlerFormator;
import net.sldt_team.gameEngine.logging.OutputStreamToLogger;
import net.sldt_team.gameEngine.network.client.IClientEventHandler;
import net.sldt_team.gameEngine.network.client.NetworkManagerClient;
import net.sldt_team.gameEngine.network.server.NetworkManagerServer;
import net.sldt_team.gameEngine.network.server.IServerEventHandler;
import net.sldt_team.gameEngine.particle.ParticleEngine;
import net.sldt_team.gameEngine.renderengine.*;
import net.sldt_team.gameEngine.renderengine.assetSystem.AssetType;
import net.sldt_team.gameEngine.renderengine.decoders.GTFTextureDecoder;
import net.sldt_team.gameEngine.renderengine.decoders.PNGTextureDecoder;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.helper.TextureFormatHelper;
import net.sldt_team.gameEngine.screen.Screen;
import net.sldt_team.gameEngine.sound.SoundEngine;
import net.sldt_team.gameEngine.sound.decoders.WaveSoundDecoder;
import net.sldt_team.gameEngine.sound.helper.SoundFormatHelper;
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
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GameApplication implements Runnable {

    /**
     * The rendering engine
     */
    public RenderEngine renderEngine;

    /**
     * The font-rendering engine
     */
    public FontRenderer fontRenderer;

    /**
     * The particle engine
     */
    public ParticleEngine particleEngine;

    /**
     * The player session
     */
    public Session playerSession;

    /**
     * The sound manager
     */
    public SoundEngine soundEngine;

    /**
     * Instance of the game settings
     */
    public GameSettings gameSettings;

    /**
     * Game logs
     */
    public List<String> logs = new ArrayList<String>();

    /**
     * Game Engine Logger
     */
    public static Logger engineLogger;

    /**
     * Game Logger
     */
    public static Logger gameLogger;

    /**
     * This list is used by the SyncedPacket to pass handle data to the main thread
     * NOTE : This list will not work when your game isn't multi-player.
     */
    public List<Runnable> syncedRunList;

    /**
     * The server manager
     * NOTE : This will be null when your game isn't multi-player.
     */
    public NetworkManagerServer serverManager;

    /**
     * The client manager
     * NOTE : This will be null when your game isn't multi-player.
     */
    public NetworkManagerClient clientManager;

    /**
     * Is the console activated (true when a program arg is equal to -console
     */
    public boolean isConsoleActivated;

    //Display settings
    private static boolean isWindowed = true;
    private static boolean isWindowed1 = true;
    private static int sizeX = 1600;
    private static int sizeY = 900;
    //The SLDTD first page displayed at game init
    private String background;
    //Game timer
    private Timer gameTimer;
    //Fps calculator
    private int fps;
    private long lastFPS;
    private int finalFPS;
    //The current screen
    private Screen currentFrame;
    //Is game initialized ?
    private boolean initialized;
    //Is exiting current screen ?
    private boolean isWindowExiting;
    //Ticks calcultion used to display the SLDT's GameEngine page
    private int initTime;
    //The name of app directory
    private static String appDirName;
    //Game exception handler
    private IExceptionHandler exceptionHandler;
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
    //Is this GameApplication using the default mouse cursor
    private boolean gameUsesDefaultCursor;
    //Time between frames calculator
    private long prevTime;
    private long curTime;
    //Is this game a network game ?
    private boolean isNetworkingGame;

    /**
     * Initializes a new Game App
     *
     * @param loggerName The name of the game logger
     * @param session    The player session if this game uses multiplayer or if you need user/password/sessionID storage
     * @param timerPower The power of the game timer (freqence of call to update function)
     * @param dirName    The game directory name (set it to #ROOT_FOLDER# for the app root folder)
     * @param exhandler  How the game should handles exception thrown in the main thread
     * @param name       The name of the game to display on application's title bar
     * @param version    The game version (use getGameVersion() to get it)
     * @param logging    Should the game display all logging and messages ?
     */
    public GameApplication(String loggerName, Session session, float timerPower, String dirName, IExceptionHandler exhandler, String name, String version, boolean logging) {
        isConsoleActivated = logging;

        gameLogger = Logger.getLogger(loggerName);
        engineLogger = Logger.getLogger("SLDT's GameEngine");

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

        LoggerHandler logger = new LoggerHandler(this);

        gameLogger.setUseParentHandlers(false);
        if (isConsoleActivated) {
            gameLogger.addHandler(handler);
            gameLogger.addHandler(logger);
        }

        engineLogger.setUseParentHandlers(false);
        if (isConsoleActivated) {
            engineLogger.addHandler(handler);
            engineLogger.addHandler(logger);
        }

        engineLogger.info("GameEngine " + EngineConstants.ENGINE_VERSION);
        engineLogger.info("RenderEngine " + EngineConstants.RENDERENGINE_VERSION);
        engineLogger.info("SoundEngine " + EngineConstants.SOUNDENGINE_VERSION);
        engineLogger.info("Binding " + EngineConstants.BINDING_VERSION);

        System.setOut(new PrintStream(new OutputStreamToLogger(gameLogger, Level.INFO), true));
        System.setErr(new PrintStream(new OutputStreamToLogger(gameLogger, Level.SEVERE), true));

        gameUsesDefaultCursor = true;
    }

    /**
     * Call this if you plan to remove the default cursor (You'll need to make the cursor render yourself).
     */
    protected void setHasCustomCursor(){
        if (!gameUsesDefaultCursor){
            engineLogger.severe("Unable to define custom cursor : Game already have a custom cursor !");
            return;
        }
        gameUsesDefaultCursor = false;
    }

    /**
     * Sets the game lang file name to whatever you want
     */
    protected void setDefaultLang(String lng){
        if (Translator.instance != null){
            engineLogger.severe("Unable to set the game language : Translator already initialized !");
            return;
        }
        new Translator(lng);
    }

    /**
     * Sets this game to be a multi-player game, so enable synced list and create network systems instances
     * NOTE : If you don't want a server, just set serverEventHandler to null.
     * NOTE_2 : If you don't want a client, just set clientEventHandler to null.
     */
    protected void setGameMultiplayer(IServerEventHandler serverEventHandler, IClientEventHandler clientEventHandler){
        if (isNetworkingGame){
            engineLogger.severe("Unable to define multiplayer game : Game is already multiplayer !");
            return;
        }
        isNetworkingGame = true;
        syncedRunList = new ArrayList<Runnable>();
        if (serverEventHandler != null) {
            serverManager = new NetworkManagerServer(this, serverEventHandler);
        }
        if (clientEventHandler != null) {
            clientManager = new NetworkManagerClient(this, clientEventHandler);
        }
    }

    /**
     * Get the current number of FPS
     */
    public int getGameFPS() {
        return finalFPS;
    }

    /**
     * Returns the time between frames (usualy for some animations)
     */
    public long getFramesTime() {
        return curTime - prevTime;
    }

    /**
     * Get the system time as long (used to calculate FPS)
     */
    public static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
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
        currentFrame.doInit();
        isWindowExiting = false;
    }

    /**
     * Returns the current displayed screen
     */
    public Screen getCurrentFrame() {
        return currentFrame;
    }

    /**
     * Returns the game name
     */
    public String getGameName() {
        if (gameName == null) {
            return "Game";
        }
        return gameName;
    }

    /**
     * Returns the game version
     */
    public String getGameVersion() {
        if (gameVersion == null) {
            return "InfDev";
        }
        return gameVersion;
    }

    /**
     * Switchs full screen state
     */
    public void switchFullScreen(){
        isWindowed1 = !isWindowed1;
        try {
            Display.setFullscreen(isWindowed1);
        } catch (LWJGLException e) {
            engineLogger.warning("Unable to switch full screen !");
        }
    }

    /**
     * Updates the display mode (resolution and fullscreen)
     */
    public void updateGameDisplay(AppSettings settings) {
        try {
            Map<String, Object> map = settings.getWindowSettings();
            isWindowed = (Boolean) map.get("SLDT_WINDOW_FULLSCREEN");
            isWindowed1 = (Boolean) map.get("SLDT_WINDOW_FULLSCREEN");
            sizeX = (Integer) map.get("SLDT_WINDOW_WIDTH");
            sizeY = (Integer) map.get("SLDT_WINDOW_HEIGHT");
            refreshOpenGLDisplay();
        } catch (LWJGLException e) {
            onGameCrash(e);
        } catch (IOException e) {
            onGameCrash(e);
        }
    }

    /**
     * Returns the user's screen width
     */
    public static int getScreenWidth() {
        if (isWindowed) {
            return sizeX;
        }
        Toolkit tool = Toolkit.getDefaultToolkit();
        return tool.getScreenSize().width;
    }

    /**
     * Returns the user's screen height
     */
    public static int getScreenHeight() {
        if (isWindowed) {
            return sizeY;
        }
        Toolkit tool = Toolkit.getDefaultToolkit();
        return tool.getScreenSize().height;
    }

    /**
     * Returns the game directory
     */
    public static File getGameDir() {
        if (appDirName.equals("#ROOT_FOLDER#")) {
            return new File(".");
        }
        return getAppDir(appDirName);
    }

    /**
     * Sets up the main game thread (args : game - the game witch the thread is for, name - the name of the game to display in thread name,
     * settings are the default window settings like resolution and fullscreen boolean)
     */
    public static void setupThread(GameApplication game, String name, AppSettings settings) {
        Map<String, Object> map = settings.getWindowSettings();
        isWindowed = (Boolean) map.get("SLDT_WINDOW_FULLSCREEN");
        isWindowed1 = (Boolean) map.get("SLDT_WINDOW_FULLSCREEN");
        sizeX = (Integer) map.get("SLDT_WINDOW_WIDTH");
        sizeY = (Integer) map.get("SLDT_WINDOW_HEIGHT");
        Thread thread = new Thread(game, name + " Main - Thread");
        thread.setPriority(10);
        thread.start();
    }

    /**
     * Called when the game is shutting down
     */
    public void closeGame() {
        engineLogger.info("Stopping game...");
        if (currentFrame != null) {
            currentFrame.onExit();
        }
        gameSettings.saveSettings();
        exitGame();
        engineLogger.info("Closing OpenAL...");
        soundEngine.onClosingGame();
        AL.destroy();
        engineLogger.info("OpenAL closed !");
        engineLogger.info("Closing OpenGL frame...");
        Display.destroy();
        engineLogger.info("Good bye !!!");
        System.exit(0);
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
        TextureFormatHelper.instance.registerNewTextureFormat("PNG", new PNGTextureDecoder());
        TextureFormatHelper.instance.registerNewTextureFormat("GTF", new GTFTextureDecoder());
        SoundFormatHelper.instance.registerNewSoundFormat("WAV", new WaveSoundDecoder());
        if (this instanceof IRegistryModifier){
            IRegistryModifier interfacer = (IRegistryModifier) this;
            interfacer.registerCustomTextureDecoders(TextureFormatHelper.instance);
            interfacer.registerCustomSoundDecoders(SoundFormatHelper.instance);
        }

        getGameDir();

        lastFPS = getTime();
        initialized = false;
        if (getAssetsFileType() == null) {
            throw new GameException(new ErrorCode008());
        }
        renderEngine = new RenderEngine(exceptionHandler, new AssetsManager(getGameDir() + File.separator + "resources" + File.separator + gameName.toLowerCase(), getAssetsFileType()), engineLogger);
        fontRenderer = new FontRenderer(renderEngine, fontName);
        soundEngine = new SoundEngine();
        background = "backgrounds/sldtBG";
        particleEngine = new ParticleEngine();
        gameSettings.loadSettings();
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
        fontRenderer.updateFontAnimation();

        if (!initialized) {
            if (this instanceof IPreloadModifier){
                IPreloadModifier modifier = (IPreloadModifier) this;
                modifier.updatePreLoadScreen();
            } else {
                sldtTicks++;
                sldtScaleTicks++;
                if (sldtTicks >= 30) {
                    rotate += 0.1F;
                    sldtTicks = 0;
                }
                if (sldtScaleTicks >= 60) {
                    scale += 0.001F;
                    sldtScaleTicks = 0;
                }
            }
            initTime++;
            if (initTime >= 5000) {
                initialized = true;
                gameTimer = new Timer(timer);
                initGame();
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                System.exit(0);
            }
        }


        if (!isWindowExiting) {
            if (currentFrame != null) {
                currentFrame.onTick();
            }
        }

        particleEngine.doUpdate();

        updateGameOverlay();

        if (isNetworkingGame && syncedRunList != null){
            for (Runnable r : syncedRunList){
                r.run();
            }
            syncedRunList.clear();
        }
    }
    private void render() {
        if (!initialized) {
            if (this instanceof IPreloadModifier){
                IPreloadModifier modifier = (IPreloadModifier) this;
                modifier.renderPreLoadScreen();
            } else {
                fontRenderer.setRenderingSize(5);
                fontRenderer.setRenderingColor(ColorHelper.GREEN);
                fontRenderer.renderString("SLDT's GameEngine " + EngineConstants.ENGINE_VERSION, getScreenWidth() - fontRenderer.getStringWidth("SLDT's GameEngine " + EngineConstants.ENGINE_VERSION), getScreenHeight() - 64);
                Material i = renderEngine.getMaterial(background);
                renderEngine.bindMaterial(i);

                renderEngine.addTranslationMatrix(10, 10);
                renderEngine.enableMiddleRotationScale();
                renderEngine.setRotationLevel(-rotate);
                renderEngine.setScaleLevel(1 - scale);
                renderEngine.renderQuad(10, 10, getScreenWidth() - 20, getScreenHeight() - 20);
            }
        }

        if (!gameUsesDefaultCursor) {
            Mouse.setGrabbed(true);
        }

        if (!isWindowExiting) {
            if (currentFrame != null) {
                currentFrame.drawWindow(renderEngine, fontRenderer);
                if (!gameUsesDefaultCursor) {
                    Mouse.setGrabbed(true);
                } else if (!currentFrame.showCursor){
                    Mouse.setGrabbed(true);
                }
            }
        }

        if (isWindowExiting) {
            renderEngine.bindColor(ColorHelper.BLACK);
            renderEngine.renderQuad(0, 0, getScreenWidth(), getScreenHeight());
            fontRenderer.setRenderingColor(ColorHelper.WHITE);
            fontRenderer.renderString("Loading...", 20, 20);
        }

        particleEngine.doRender(renderEngine, fontRenderer);

        renderGameOverlay();
    }
    private void initOpenGL() {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, getScreenWidth(), getScreenHeight(), 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
    private void onGameCrash(Exception e) {
        if (!(e instanceof GameException)) {
            exceptionHandler.handleException(new GameException(e));
        } else {
            exceptionHandler.handleException((GameException) e);
        }
    }
    private DisplayMode findDisplayMode(int width, int height, int bpp) throws LWJGLException {
        DisplayMode[] modes = Display.getAvailableDisplayModes();
        for (DisplayMode mode : modes) {
            if (mode.getWidth() == width && mode.getHeight() == height && mode.getBitsPerPixel() >= bpp && mode.getFrequency() <= 60) {
                return mode;
            }
        }
        return Display.getDesktopDisplayMode();
    }
    private void refreshOpenGLDisplay() throws LWJGLException, IOException {
        Display.destroy();
        if (isWindowed) {
            Display.setDisplayMode(findDisplayMode(sizeX, sizeY, Display.getDisplayMode().getBitsPerPixel()));
        } else {
            Display.setDisplayModeAndFullscreen(findDisplayMode(sizeX, sizeY, Display.getDisplayMode().getBitsPerPixel()));
        }
        ByteBuffer[] shit = new ByteBuffer[2];
        if (getIconPackage() == null) {
            shit[0] = renderEngine.mountTexture("PNG", GameApplication.class.getResourceAsStream("icon16.png"));
            shit[1] = renderEngine.mountTexture("PNG", GameApplication.class.getResourceAsStream("icon32.png"));
        } else {
            shit[0] = renderEngine.mountTexture("PNG", ClassLoader.getSystemResourceAsStream("./" + getIconPackage() + "/gameIcon16.png"));
            shit[1] = renderEngine.mountTexture("PNG", ClassLoader.getSystemResourceAsStream("./" + getIconPackage() + "/gameIcon32.png"));
        }
        Display.setIcon(shit);
        Display.create();
        initOpenGL();
        if (getAssetsFileType() == null) {
            throw new GameException(new ErrorCode008());
        }
        renderEngine.reloadRenderingEngine();
        fontRenderer = new FontRenderer(renderEngine, fontName);

        if (currentFrame != null) {
            currentFrame.onExit();
            currentFrame.setGame(this);
            currentFrame.doInit();
        }
    }

    /**
     * @exclude
     */
    public void run() {
        try {
            if (isWindowed) {
                Display.setDisplayMode(findDisplayMode(sizeX, sizeY, Display.getDisplayMode().getBitsPerPixel()));
            } else {
                Display.setDisplayModeAndFullscreen(findDisplayMode(sizeX, sizeY, Display.getDisplayMode().getBitsPerPixel()));
            }
            Display.setTitle(gameName);
            Display.create();
            engineLogger.info("Starting LWJGL - OpenAL...");
            AL.create();
            engineLogger.info("LWJGL - OpenAL successfully started !");
            Mouse.create();
            Keyboard.create();
            initOpenGL();
            init();
            ByteBuffer[] shit = new ByteBuffer[2];
            if (getIconPackage() == null) {
                shit[0] = renderEngine.mountTexture("PNG", GameApplication.class.getResourceAsStream("icon16.png"));
                shit[1] = renderEngine.mountTexture("PNG", GameApplication.class.getResourceAsStream("icon32.png"));
            } else {
                shit[0] = renderEngine.mountTexture("PNG", ClassLoader.getSystemResourceAsStream("./" + getIconPackage() + "/gameIcon16.png"));
                shit[1] = renderEngine.mountTexture("PNG", ClassLoader.getSystemResourceAsStream("./" + getIconPackage() + "/gameIcon32.png"));
            }
            Display.setIcon(shit);
            while (!Display.isCloseRequested()) {
                curTime = getTime();
                Display.update();
                gameTimer.updateTimer();
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                for (int j = 0; j < gameTimer.elapsedTicks; j++) {
                    update();
                }
                render();

                updateFPS();
                prevTime = curTime;
            }
            closeGame();
        } catch (Exception e) {
            onGameCrash(e);
        }
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