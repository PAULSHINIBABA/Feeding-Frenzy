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
        initItems();
        initEnemy();
    }
    public void update(double dt) {
        updateMenu(dt);

        // While in-game, if the level is complete stop processing
        if (this.env.GetIsLevelComplete()) { return; }
        updateEnvironment();
        updatePlayer(dt);
        updateItems(dt);
        updateEnemy();
    }
    public void paintComponent() {
        drawMenu();

        drawEnvironment();

        drawEnemy(); // Draw enemy first
        drawItems(); // Then draw items
        drawPlayer(); // Then draw the player

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
    int score;

    public void InitSystem() {
        this.gameState = 0;
        this.gameStateString = "nothing";
        this.isSinglePlayer = true;
        this.width = 500;
        this.height = 500;
        this.score = 0;
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
    public boolean rightKey;
    public boolean leftKey;
    public boolean upKey;
    public boolean downKey;
    public boolean escKey;
//    public boolean shiftKey;
//    public boolean enterKey;
    public boolean spaceKey;

    // Called whenever a key is pressed
    public void keyPressed(KeyEvent e) {
//        //The user pressed left arrow
        if(e.getKeyCode() == KeyEvent.VK_LEFT) { this.leftKey = true; }
        // The user pressed right arrow
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) { this.rightKey = true; }
        // The user pressed up arrow
        if(e.getKeyCode() == KeyEvent.VK_UP) { this.upKey = true; }
        // The user pressed up arrow
        if(e.getKeyCode() == KeyEvent.VK_DOWN) { this.downKey = true; }
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
        if(e.getKeyCode() == KeyEvent.VK_LEFT) { this.leftKey = false; }
        // The user released right arrow
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) { this.rightKey = false; }
        // The user released up arrow
        if(e.getKeyCode() == KeyEvent.VK_UP) { this.upKey = false; }
        // The user released up arrow
        if(e.getKeyCode() == KeyEvent.VK_DOWN) { this.downKey = false; }
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
    myfish myfish;
//    double score;
    boolean gamestart; // TODO: Replace to merge with the rest
    Image myfish_r,myfish_l, pearlimage,boomimage,starfishimage,backgoundimage;
    public void initPlayer() {
        this.myfish = new myfish();
        this.gamestart = false;

        //load image
        this.backgoundimage = loadImage("Assignment2/assets/image/background.png");

        // TODO: Compress the _r _l into one image and use a reflect method
        this.myfish_r = loadImage("Assignment2/assets/image/myfish_r.png");
        this.myfish_l = loadImage("Assignment2/assets/image/myfish_l.png");
    }
    public void updatePlayer(double dt) {
        if (gameState == 4) {
            // TODO: Should update this to go to a game over screen instead
            if (!this.gamestart) { this.gamestart = true; }

            // playArea fields
            double playAreaX = this.env.GetPlayAreaX();
            double playAreaY = this.env.GetPlayAreaY();
            double playAreaWidth = this.env.GetPlayAreaWidth();
            double playAreaHeight = this.env.GetPlayAreaHeight();

            // Fish moves within the playArea bounds
            myfish.updatemyfish(dt,
                    this.upKey,
                    this.downKey,
                    this.leftKey,
                    this.rightKey,
                    playAreaX,
                    playAreaY,
                    playAreaWidth,
                    playAreaHeight);

            // Check for collision with the enemy
            // TODO: Will probably need to refactor this with the Enemy code
//            if (myfish.getmyfishRec().intersects(getenemiefishRec())) {
//                // Increase fish size
//                myfish.myfish_w += 3;
//                myfish.myfish_h += 12;
//            }
        }
    }
    public void drawPlayer() {
        if (this.gamestart) { this.drawMyself(); }
        // TODO: Handle the game over state (need menu?)
    }

    // Create a rectangle of the enemy?
    // TODO: Will need to refactor this with the Enemy code
//    public Rectangle getenemiefishRec(){
//        //enemies width and enemies high
//        int emfish_w = 15;
//        int emfish_h = 15;
////        return new Rectangle((int) emposition_x, (int) emposition_y, emfish_w, emfish_h);
//        return new Rectangle((int) 0, (int) 0, emfish_w, emfish_h);
//    }

    public void drawMyself() {
        // Decide which image to draw based on direction
        // TODO: Should refactor this. (use a reflect image method with one image only)
        Image currentFishImage = this.leftKey ? myfish_l : myfish_r;

        // Draw the fish at its current position
        drawImage(currentFishImage, (int)myfish.mposition_x,  (int)myfish.mposition_y, myfish.myfish_w, myfish.myfish_h);

        // Draw the collider box
        changeColor(0,255,0);
        // top
        drawLine(myfish.mposition_x,
                myfish.mposition_y,
                (myfish.mposition_x + myfish.myfish_w),
                myfish.mposition_y);
        // bottom
        drawLine(myfish.mposition_x,
                (myfish.mposition_y + myfish.myfish_h),
                (myfish.mposition_x + myfish.myfish_w),
                (myfish.mposition_y + myfish.myfish_h));
        // left
        drawLine(myfish.mposition_x,
                myfish.mposition_y,
                myfish.mposition_x,
                (myfish.mposition_y + myfish.myfish_h));
        // right
        drawLine((myfish.mposition_x + myfish.myfish_w),
                myfish.mposition_y,
                (myfish.mposition_x + myfish.myfish_w),
                (myfish.mposition_y + myfish.myfish_h));
    }


    //-------------------------------------------------------
    // Item methods
    //-------------------------------------------------------
    pearl pearl;
    boom boom;
    starfish starfish;
    public void initItems() {
        this.pearl = new pearl();
        this.boom = new boom();
        this.starfish = new starfish();

        this.pearlimage = loadImage("Assignment2/assets/image/Pearl.png");
        this.boomimage = loadImage("Assignment2/assets/image/boom.png");
        this.starfishimage = loadImage("Assignment2/assets/image/starfish.png");
    }
    public void updateItems(double dt) {
        // TODO: Update the rand input values to use the playArea bounds
        double randStarX = rand(500);
        double randStarY = rand(500);
        double randBombX = rand(500);
        double randBombY = rand(500);
        double randSpeed = rand(100);
        if(gamestart) {
            //update pearl
            pearl.updatepearl(dt,
                    myfish.getmyfishRec(),
                    myfish.myfishspeed_x,
                    myfish.myfishspeed_y,
                    randStarX,
                    randStarY);

            //update starfish
            // TODO: update this to use the playArea bounds
            if (starfish.updatestarfish(dt,
                    myfish.getmyfishRec(),
                    score, randStarX,
                    randStarY,
                    randSpeed,
                    width(),
                    height())) {

                // Update the environment score
                this.env.addScore(20);
            }

            // TODO: update this to use the playArea bounds
            if (boom.updateboom(dt,
                    myfish.getmyfishRec(),
                    randBombX,
                    randBombY,
                    randSpeed,
                    width(),
                    height())) {

                // Set the gameStart to be false, ie. game over
                // TODO: load the game over menu state
                gamestart = false;
            }
        }

    }
    public void drawItems() {
        this.drawpearl();
        this.drawstarfish();
        this.drawboom();
    }

    public void drawboom(){
        if (boom.isvisible()){
            drawImage(boomimage,boom.boompos_x,boom.boompos_y, boom.boom_w, boom.boom_h);
        }
    }
    public void drawstarfish(){
        if (starfish.isvisible()){
            drawImage(starfishimage,starfish.starfishpos_x,starfish.starfishpos_y,starfish.starfish_w,starfish.starfish_h);
        }
    }
    public void drawpearl(){
        if (pearl.isvisible()){
            changeColor(Color.white);
            drawImage(pearlimage,(int)pearl.getpositionx(),(int)pearl.getpositiony(), pearl.getwidth(), pearl.getheight());
        }
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
