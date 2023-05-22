package Assignment2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class Main extends GameEngine {
    public static void main(String[] args) {
        createGame(new Main());
    }
    public void init() {
        InitSystem();
        initMenu();
        initEnvironment();
        initPlayer();
        initEnemy();
    }
    public void update(double dt) {
        updateMenu(dt);

        // While in-game, if the level is complete stop processing
        if (this.env.GetIsLevelComplete()) { return; }
        updateEnvironment();
        updatePlayer();
        updateEnemy();
    }
    public void paintComponent() {
        drawMenu();

        drawEnvironment();

        drawPlayer();
        drawEnemy();

        drawEnvironmentLayerTop();
    }


    //-------------------------------------------------------
    // System methods
    //-------------------------------------------------------
    // "nothing"; no game state change was clicked
    // "single_player"; single player game state change was clicked
    // "time_attack"; time attack game state change was clicked
    String gameStateString;

    // gameState = 0; Main menu
    // gameState = 1; Loading page
    // gameState = 2; Settings page
    // gameState = 3; Help page
    // gameState = 4; Environment(Single Player)
    // gameState = 5; Environment(Time Attack)
    // gameState = 6; Checkout page
    int gameState;
    boolean isSinglePlayer;
    int width;
    int height;

    public void InitSystem() {
        this.gameState = 0;
        this.gameStateString = "nothing";
        this.isSinglePlayer = true;
        this.width = 500;
        this.height = 500;
        setWindowSize(this.width, this.height);
    }


    //-------------------------------------------------------
    // Mouse Handler
    //-------------------------------------------------------
    public double mouseX;
    public double mouseY;
    boolean mouse1Pressed = false;


    // MousePressed Event Handler
    public void mousePressed(MouseEvent e) {
        if (gameState == 0) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                // TODO: Should streamline this so the mouse handler interrupt can return quicker.
                this.gameStateString = this.sm.menuMouseClicked(e);
            }
        }
    }

    // MouseReleased Event Handler
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) { this.mouse1Pressed = false; }
    }

    // MouseMoved Event Handler
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }


    //-------------------------------------------------------
    // Key Presses
    //-------------------------------------------------------
//    public boolean rightKey;
//    public boolean leftKey;
//    public boolean upKey;
//    public boolean downKey;
    public boolean escKey;
//    public boolean shiftKey;
//    public boolean enterKey;
    public boolean spaceKey;

    // Called whenever a key is pressed
    public void keyPressed(KeyEvent e) {
//        //The user pressed left arrow
//        if(e.getKeyCode() == KeyEvent.VK_LEFT) { this.leftKey = true; }
//        // The user pressed right arrow
//        if(e.getKeyCode() == KeyEvent.VK_RIGHT) { this.rightKey = true; }
//        // The user pressed up arrow
//        if(e.getKeyCode() == KeyEvent.VK_UP) { this.upKey = true; }
//        // The user pressed up arrow
//        if(e.getKeyCode() == KeyEvent.VK_DOWN) { this.downKey = true; }
        // The user pressed ESC
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { this.escKey = true; }
//        // The user pressed shiftKey
//        if(e.getKeyCode() == KeyEvent.VK_SHIFT) { this.shiftKey = true; }
//        // The user pressed enterKey
//        if(e.getKeyCode() == KeyEvent.VK_ENTER) { this.enterKey = true; }
        // The user pressed spaceKey
        if(e.getKeyCode() == KeyEvent.VK_SPACE) { this.spaceKey = true; }
    }

    // Called whenever a key is released
    public void keyReleased(KeyEvent e) {
//        // The user released left arrow
//        if(e.getKeyCode() == KeyEvent.VK_LEFT) { this.leftKey = false; }
//        // The user released right arrow
//        if(e.getKeyCode() == KeyEvent.VK_RIGHT) { this.rightKey = false; }
//        // The user released up arrow
//        if(e.getKeyCode() == KeyEvent.VK_UP) { this.upKey = false; }
//        // The user released up arrow
//        if(e.getKeyCode() == KeyEvent.VK_DOWN) { this.downKey = false; }
        // The user released ESC
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { this.escKey = false; }
//        // The user released shiftKey
//        if(e.getKeyCode() == KeyEvent.VK_SHIFT) { this.shiftKey = false; }
//        // The user released enterKey
//        if(e.getKeyCode() == KeyEvent.VK_ENTER) { this.enterKey = false; }
        // The user released spaceKey
        if(e.getKeyCode() == KeyEvent.VK_SPACE) { this.spaceKey = false; }
    }

    // Process the keys pressed for each game element.
//    public void processKeys() {
//        if (this.leftKey) {
//        }
//        if (this.rightKey) {
//        }
//        if (this.upKey) {
//        }
//        if (this.downKey) {
//        }
//        if (this.escKey) {
//        }
//        if (this.shiftKey) {
//        }
//    }



    //-------------------------------------------------------
    // Menu methods
    //-------------------------------------------------------
    public StartMenu sm;
//    public int menuState;
//    public LoadingPage lp;
    public LoadingPage lp;

    public void initMenu() {
        this.sm = new StartMenu(this);
//        this.sm.init();

        this.lp = new LoadingPage(this);
    }

    public void updateMenu(double dt) {
        if (gameState == 0) {
            if (Objects.equals(gameStateString, "single_player")) {
                this.gameState = 1; // Go to LoadingPage
                this.env.SetIsTimeAttack(false); // Set the environment to single player mode

            } else if (Objects.equals(gameStateString, "time_attack")) {
                this.gameState = 1; // Go to LoadingPage
                this.env.SetIsTimeAttack(true); // Set the environment to time attack mode
            }
        } else if (gameState == 1) {
            this.lp.startLoading();
            this.lp.updatePage(dt);
            if (spaceKey && (this.lp.getProgress() >= 1.0)) {
                // Set to the game play area instance
                this.gameState = 4;
                this.env.SetBaseTime(getTime());
                this.env.SetIsPaused(false);
            }
        }
    }

    public void drawMenu() {
        if (this.gameState == 0) {
            this.sm.drawAll();
        } else if (gameState == 1) {
            this.lp.drawAll();
        }
    }


    //-------------------------------------------------------
    // Player methods
    //-------------------------------------------------------
    public void initPlayer() {
    }
    public void updatePlayer() {
    }
    public void drawPlayer() {
    }


    //-------------------------------------------------------
    // Enemy methods
    //-------------------------------------------------------
    public void initEnemy() {
    }
    public void updateEnemy() {
    }
    public void drawEnemy() {
    }


    //-------------------------------------------------------
    // Environment methods
    //-------------------------------------------------------
    Environment env;
    public void initEnvironment() {
        this.env = new Environment(this, !this.isSinglePlayer);
        this.env.SetEnvironmentPlayWidth(this.width);
        this.env.SetEnvironmentPlayHeight(this.height);
    }
    public void updateEnvironment() {
        if (gameState == 4) {
            this.env.EnvironmentLevelCompleteCheck();

            boolean wasPaused = false;

            if (this.escKey) {
                if (this.env.GetIsPaused()) {
                    this.env.SetIsPaused(false);
                    wasPaused = true;
                } else {
                    this.env.SetIsPaused(true);
                }
            }

            UpdateTimer(wasPaused);
        }
    }

    public void UpdateTimer(boolean wasPaused) {
        // return if paused
        if (this.env.GetIsPaused()) { return; }

        // fields for paused timer
        double newTime = getTime();

        // Process the timer for paused case
        if (wasPaused) {
            double beforePause;
            double timeDelay;

            beforePause = this.env.GetTimer();
            double afterPause = Math.round(((newTime - this.env.GetBaseTime()) / 1000.0) * 100.0) / 100.0;
            timeDelay = (afterPause - beforePause);

            // Set the pausable timer
            this.env.addCountDownTimerOffset(timeDelay);
        }

        // Set the global timer
        this.env.SetTimer(newTime);

        // Time attack counter processing
        if (this.env.GetIsTimeAttack()) { this.env.SetCountDownCurrentTimer(this.env.GetCountDownTimerWOffset()); }
    }

    public void drawEnvironment() {
        if (gameState == 4) {
            this.env.drawEnvironment();
        }
    }

    public void drawEnvironmentLayerTop() {
        if (gameState == 4) {
            this.env.drawHUD();
            boolean timeAttack = this.env.GetIsTimeAttack();
            this.env.drawTimer(timeAttack);
            this.env.drawScore();
            this.env.drawGrowth();
        }
    }
}
