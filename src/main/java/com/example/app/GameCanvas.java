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
import com.example.app.ui.UIMainMenuManager;
import com.example.app.ui.UIManager;

import java.awt.*;
import java.awt.image.BufferStrategy;


public class GameCanvas extends Canvas implements Runnable {

    // SCREEN SETTINGS
    private final int originalTileSize = 16;
    private final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // FPS
    private final int FPS = 60;
    private final double drawInterval = 1.0 / FPS;
    public double dt;
    // TEST FPS
    private int frameCount = 0;
    private double timeBetweenFPSFrames = 0;

    // EVENT
    public final IEvent eventUIClick = new Event();
    public final IEvent eventEntityDead = new Event();
    public final IEvent eventChangeMap = new Event();
    public final IEvent eventCreateMap = new Event();
    public final IEvent eventGroupDead = new Event();

    // HANDLERS
    public final KeyHandler keyH = new KeyHandler();
    public final MouseHandler mouseH = new MouseHandler();
    public final MouseMotionHandler mouseMH = new MouseMotionHandler();

    // MAP
    public final TileManager tileM;
    public final MapDrawerManager mapDrawerM;

    // MAP LOADER
    public final LoadMapManager loadMapM;
    
    // MAP CREATER
    public final MapCreateManager mapCreateM;

    // SceneryManager
    public final SceneryManager sceneryM;

    // UI
    public final UIManager uiM;
    private final UIMainMenuManager uiMainMenuM;

    // ENTITIES
    public final EntityManager entityM;
    public ITrackable tracked;

    // COLLISION CHECKER
    public final CollisionChecker cChecker;

    // THREAD
    private Thread gameThread;

    public GameCanvas() {
        // WINDOW SETTINGS
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.BLACK);
        setIgnoreRepaint(true); // IMPORTANT: no auto-Swing repaint

        // Handler init
        addKeyListener(keyH);
        addMouseListener(mouseH);
        addMouseMotionListener(mouseMH);

        // Event init
        TestListener testListener = new TestListener();
        eventChangeMap.addListener(testListener);
        eventUIClick.addListener(testListener);
        eventEntityDead.addListener(testListener);
        eventCreateMap.addListener(testListener);
        eventGroupDead.addListener(testListener);

        // UI
        uiM = new UIManager(this);
        uiMainMenuM = new UIMainMenuManager(this);

        // MAP LOADER
        loadMapM = new LoadMapManager(this);
        
        // MAP CREATE
        mapCreateM = new MapCreateManager(this);

        // MAP INIT
        tileM = new TileManager(this);

        // MAPMAKER
        mapDrawerM = new MapDrawerManager(this);

        // COLLISION
        cChecker = new CollisionChecker(this);

        // ENTITIES
        entityM = new EntityManager(this);

        // SceneryManager
        sceneryM = new SceneryManager(this);
        sceneryM.changeScenery(SceneryManager.TITLE_SCENERY);

        // END
        uiMainMenuM.setShow(true);
        setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setTracked(ITrackable tracked) {
        this.tracked = tracked;
    }

    @Override
    public void run() {

        // Buffer strategy (how to show images)
        createBufferStrategy(2);  // triple buffering
        BufferStrategy bs = getBufferStrategy();

        // Frame logic
        long lastTime = System.nanoTime();
        while (gameThread != null) {

            dt = (System.nanoTime() - lastTime) / 1_000_000_000.0;

            if (dt >= drawInterval) {
                update();
                render(bs);
                lastTime = System.nanoTime();

                // Show FPS
                frameCount++;
                timeBetweenFPSFrames += dt;
                if (frameCount == 60){
                    System.out.println("FPS: " + (int)(Math.round(60d / timeBetweenFPSFrames)));
                    frameCount = 0;
                    timeBetweenFPSFrames = 0;
                }
            }
        }
    }

    private void update() {
        // Everything that need update
        entityM.update();
        uiM.update();
        mapDrawerM.update();
        uiMainMenuM.update();
        sceneryM.update();

        // Allow to have a one frame click
        // UPDATE AFTER EVERY THING !!!!
        // TO ALLOW ONE FRAME CLICK ...
        keyH.update();
        mouseH.update();
    }

    private void render(BufferStrategy bs) {

        // Everything that need to be rendered
        Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();

        // CLEAR SCREEN
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        // TILES
        tileM.draw(g2);
        // ENTITIES
        entityM.draw(g2);
        // UI
        uiM.draw(g2);

        // END
        g2.dispose();
        bs.show(); // FLIP BUFFERS (exact paint timing)
        Toolkit.getDefaultToolkit().sync(); // Optional for Linux
    }
}
