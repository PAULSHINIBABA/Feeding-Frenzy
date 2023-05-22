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

        updateEnvironment();
        updatePlayer();
        updateEnemy();
    }
    public void paintComponent() {
        drawMenu();

        drawPlayer();
        drawEnemy();
        drawEnvironment();
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

    public void InitSystem() {
        this.gameState = 0;
        this.gameStateString = "nothing";
        this.isSinglePlayer = false;
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
//    public boolean escKey;
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
//        // The user pressed ESC
//        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { this.escKey = true; }
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
//        // The user released ESC
//        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { this.escKey = false; }
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
        switch(this.gameState) {
            case 0: // Main menu
                if (Objects.equals(gameStateString, "single_player")) {
                    System.out.println("Single Player mode was clicked");
                    // TODO: Load the next game state -> LoadingPage -> SinglePlayerEnvironment
                    this.gameState = 1; // Go to LoadingPage
                    this.isSinglePlayer = true;
//                this.gameStateString = "nothing"; // Reset the gameStateString

                } else if (Objects.equals(gameStateString, "time_attack")) {
                    System.out.println("Time Attack mode was clicked");
                    // TODO: Load the next game state -> LoadingPage -> TimeAttackEnvironment
                    this.gameState = 1; // Go to LoadingPage
                    this.isSinglePlayer = false;
//                this.gameStateString = "nothing"; // Reset the gameStateString
                }
                break;
            case 1: // Loading page
                this.lp.startLoading();
                this.lp.updatePage(dt);
                if (spaceKey) {
                    // Is single player
                    if (this.isSinglePlayer) { this.gameState = 4; }
                    // Is time attack
                    else { this.gameState = 5; }
                }
                break;
//            case 2:
//                break;
//            case 3:
//                break;
            case 4: // Single player
                break;
            case 5: // Time attack
                break;
//            case 6:
//                break;
            default:
                break;
        }
    }

    public void drawMenu() {
        switch(this.gameState) {
            case 0:
                this.sm.drawAll();
                break;
            case 1:
                this.lp.drawAll();
                break;
//            case 2:
//                break;
//            case 3:
//                break;
            case 4:
                // Is single player
                // TODO: load the environment (toggle single player)
                // TODO: load the player
                break;
            case 5:
                // Is time attack
                // TODO: load the environment (toggle time attack)
                // TODO: load the player

                break;
//            case 6:
//                break;
            default:
                break;
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
    public void initEnvironment() {
    }
    public void updateEnvironment() {
    }
    public void drawEnvironment() {
    }
}
