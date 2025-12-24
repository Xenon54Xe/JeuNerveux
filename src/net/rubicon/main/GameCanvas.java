package net.rubicon.main;

import net.rubicon.UI.UIManager;
import net.rubicon.entity.EntityManager;
import net.rubicon.event.TestListener;
import net.rubicon.event.UIClickEvent;
import net.rubicon.event.UIClickEventComponent;
import net.rubicon.handler.KeyHandler;
import net.rubicon.handler.MouseHandler;
import net.rubicon.handler.MouseMotionHandler;
import net.rubicon.tile.IMapManager;
import net.rubicon.tile.MapMaker;
import net.rubicon.tile.TileManager;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameCanvas extends Canvas implements Runnable, IMapManager {

    // SCREEN SETTINGS
    private final int originalTileSize = 16;
    private final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS
    public final int maxWorldCol;
    public final int maxWorldRow;
    public final int worldHeight;
    public final int worldWidth;

    // FPS
    private final int FPS = 60;
    private final double drawInterval = 1.0 / FPS;
    public double dt;
    // TEST FPS
    private int frameCount = 0;
    private double timeBetweenFPSFrames = 0;

    // EVENT
    public final UIClickEvent uiClickEvent = new UIClickEvent();
    // HANDLERS
    public final KeyHandler keyH = new KeyHandler();
    public final MouseHandler mouseH = new MouseHandler();
    public final MouseMotionHandler mouseMH = new MouseMotionHandler();

    // TILES
    public final int layerCount;
    // MAP
    public final String mapName;
    public final String mapPath;
    private final int[] mapDimensions;
    public final TileManager tileM;

    // UI
    public final UIManager uiM;

    // ENTITIES
    public EntityManager entityM = new EntityManager(this);

    // COLLISION CHECKER
    public CollisionChecker cChecker = new CollisionChecker(this);

    // UTILS
    private final MapMaker mapMaker;

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
        TestListener<UIClickEventComponent> testListener = new TestListener<>();
        uiClickEvent.addListener(testListener);

        // MAP INIT
        mapName = "map03";
        mapPath = "/res/maps/" + mapName + ".txt";
        mapDimensions = IMapManager.super.getMapDimensions(mapPath);
        maxWorldCol = mapDimensions[0];
        maxWorldRow = mapDimensions[1];
        layerCount = mapDimensions[2];
        tileM = new TileManager(this, mapName);
        worldWidth = maxWorldCol * tileSize;
        worldHeight = maxWorldRow * tileSize;

        // MAPMAKER
        uiM = new UIManager(this);
        mapMaker = new MapMaker(this);


        // END
        setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
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
        mapMaker.update();

        // Allow to have a one frame click
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
