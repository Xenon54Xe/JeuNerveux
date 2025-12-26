package net.rubicon.main;

import net.rubicon.tile.LoadMapManager;
import net.rubicon.ui.UIManager;
import net.rubicon.entity.EntityManager;
import net.rubicon.event.*;
import net.rubicon.event.Event;
import net.rubicon.handler.KeyHandler;
import net.rubicon.handler.MouseHandler;
import net.rubicon.handler.MouseMotionHandler;
import net.rubicon.tile.MapMakerManager;
import net.rubicon.tile.TileManager;

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
    public final Event eventUIClick = new Event();
    public final Event eventEntityDead = new Event();
    public final Event eventChangeMap = new Event();

    // HANDLERS
    public final KeyHandler keyH = new KeyHandler();
    public final MouseHandler mouseH = new MouseHandler();
    public final MouseMotionHandler mouseMH = new MouseMotionHandler();

    // MAP
    public final TileManager tileM;
    private final MapMakerManager mapMakerManager;

    // MAP LOADER
    LoadMapManager loadMapM;

    // UI
    public final UIManager uiM;

    // ENTITIES
    public EntityManager entityM;

    public ITrackable tracked;

    // COLLISION CHECKER
    public CollisionChecker cChecker;

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

        // UI
        uiM = new UIManager(this);

        // MAP LOADER
        loadMapM = new LoadMapManager(this);

        // MAP INIT
        tileM = new TileManager(this);
        tileM.setMapName("map03.txt");
        tileM.loadMap();

//        String mapName = "map03.txt";
//        tileM.setMapName(mapName);
//        tileM.loadMap();

        // MAPMAKER
        mapMakerManager = new MapMakerManager(this);

        // COLLISION
        cChecker = new CollisionChecker(this);

        // ENTITIES
        entityM = new EntityManager(this);

        // END
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
                update(dt);
                render(bs);
                lastTime = System.nanoTime();

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

    private void update(double dt) {

        // Everything that need update
        entityM.update(dt);
        uiM.update();
        mapMakerManager.update();

        // Allow to have a one frame click
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
