// java
package com.example.app;

import com.example.app.entity.EntityManager;
import com.example.app.event.Event;
import com.example.app.event.IEvent;
import com.example.app.event.TestListener;
import com.example.app.handler.KeyHandler;
import com.example.app.handler.MouseHandler;
import com.example.app.handler.MouseMotionHandler;
import com.example.app.tile.LoadMapManager;
import com.example.app.tile.MapCreateManager;
import com.example.app.tile.MapDrawerManager;
import com.example.app.tile.TileManager;
import com.example.app.ui.UIManager;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameCanvas extends Canvas implements Runnable {

    // \`SCREEN CONFIG\`
    private static final int ORIGINAL_TILE_SIZE = 16;
    private static final int SCALE = 3;
    public final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    public final int MAX_SCREEN_COL = 16;
    public final int MAX_SCREEN_ROW = 12;
    public final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    public final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;

    // \`TIMING / FPS\`
    private static final int FPS = 60;
    private final double drawInterval = 1.0 / FPS;
    public double dt;
    private int frameCount = 0;
    private long lastCheck = 0;

    // \`GAME STATE\`
    public static final int PLAY_STATE = 1;
    public static final int PAUSE_STATE = 2;
    public int gameState;

    // \`EVENTS\` (public to match original usage)
    public final IEvent eventUIClick = new Event();
    public final IEvent eventEntityDead = new Event();
    public final IEvent eventChangeMap = new Event();
    public final IEvent eventCreateMap = new Event();
    public final IEvent eventGroupDead = new Event();

    // \`INPUT HANDLERS\`
    public final KeyHandler keyH = new KeyHandler();
    public final MouseHandler mouseH = new MouseHandler();
    public final MouseMotionHandler mouseMH = new MouseMotionHandler();

    // \`MANAGERS\`
    public final TileManager tileM;
    public final MapDrawerManager mapDrawerM;
    public final LoadMapManager loadMapM;
    public final MapCreateManager mapCreateM;
    public final SceneryManager sceneryM;
    public final UIManager uiM;
    public final EntityManager entityM;
    public Trackable tracked;

    // \`COLLISION & SOUND\`
    public final CollisionChecker cChecker;
    private final Sound music = new Sound();
    private final Sound se = new Sound();

    // \`THREAD\`
    private Thread gameThread;

    // \`UTILS\`
    public final boolean editorMode = true;

    public GameCanvas() {
        // window / canvas settings
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setIgnoreRepaint(true); // IMPORTANT: no auto-Swing repaint

        // input listeners
        addKeyListener(keyH);
        addMouseListener(mouseH);
        addMouseMotionListener(mouseMH);

        // events
        TestListener testListener = new TestListener();
        eventChangeMap.addListener(testListener);
        eventUIClick.addListener(testListener);
        eventEntityDead.addListener(testListener);
        eventCreateMap.addListener(testListener);
        eventGroupDead.addListener(testListener);

        // collision
        cChecker = new CollisionChecker(this);

        // managers (preserve original initialization order)
        uiM = new UIManager(this);

        tileM = new TileManager(this);
        loadMapM = new LoadMapManager(this);
        mapCreateM = new MapCreateManager(this);
        mapDrawerM = new MapDrawerManager(this);

        entityM = new EntityManager(this);
        sceneryM = new SceneryManager(this);
        sceneryM.changeScenery(SceneryManager.TITLE_SCENERY);

        setFocusable(true);
    }

    // \`LIFECYCLE\`
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        playMusic(Sound.INTRO_SCENE);
        gameState = PLAY_STATE;
    }

    @Override
    public void run() {
        // Buffer strategy setup
        createBufferStrategy(2); // double buffering (comment preserved)
        BufferStrategy bs = getBufferStrategy();

        long lastTime = System.nanoTime();

        while (gameThread != null) {
            dt = (System.nanoTime() - lastTime) / 1_000_000_000.0;

            if (dt >= drawInterval) {
                update();
                render(bs);
                lastTime = System.nanoTime();

                // FPS reporting
                frameCount++;
                if (frameCount == FPS) {
                    double timeElapsed = (System.nanoTime() - lastCheck) / 10.0e8;
                    lastCheck = System.nanoTime();
                    System.out.println("FPS: " + (int) (Math.round(FPS / timeElapsed)));
                    frameCount = 0;
                }
            }
        }
    }

    // \`GAME LOOP HELPERS\`
    private void update() {
        if (gameState == PLAY_STATE) {
            entityM.update();
            mapDrawerM.update();
        }

        uiM.update();
        sceneryM.update();

        // Allow single-frame input handling: update after all logic
        keyH.update();
        mouseH.update();
    }

    private void render(BufferStrategy bs) {
        Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();
        try {
            // clear
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            // drawing order
            tileM.draw(g2);
            entityM.draw(g2);
            uiM.draw(g2);
        } finally {
            g2.dispose();
        }

        bs.show(); // flip buffers
        Toolkit.getDefaultToolkit().sync(); // optional on Linux
    }

    // \`AUDIO CONTROLS\`
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }

    // \`SETTERS / UTIL\`
    public void setTracked(Trackable tracked) {
        this.tracked = tracked;
    }
}