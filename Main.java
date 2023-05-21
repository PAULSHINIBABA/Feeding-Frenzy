// Imports
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

// The main entry point for the game
public class Main extends GameEngine{

    // TODO: Can remove
    public static void main(String args[]) {
        createGame(new Main());
    }

    public void init() {
        setWindowSize(this.width, this.height); // TODO: Can remove
        this.InitImages();
        this.InitEnvironment();
        this.InitEnemies();

        // Fake init player
        FakeInitPlayer(); // TODO: Replace with Lucass
    }

    public void update(double dt) {

        // TODO: Required below
        // Stop processing the game if the level is complete
        if (this.env.GetIsLevelComplete()) { return; }

        this.UpdateEnvironment();

        // Update out of game state
        if (this.env.GetIsPaused()) {
            System.out.println("Is paused");
            return;
        }

        // Update in-game state
        this.SpawnEnemy();
        
        // Update player 
        this.FakeUpdatePlayer(); // TODO: Replace with Lucass
        
        // Update enemies
        this.UpdateEnemies(dt);
    }

    public void paintComponent() {
        changeBackgroundColor(black);
        clearBackground(this.width, this.height);

        // TODO: Required below
        // Lower layer is drawn first
        // Draw the entities
        this.DrawEnvironment();
        this.DrawEnemies(this.enemies);
        this.DrawEnemies(this.sharkEnemies);
        // draw player
        FakeDrawPlayer(); // TODO: Replace with Lucass

        // Draw any menus as an overlay.
        this.DrawEnvironmentLayerTop();

    }

    //-------------------------------------------------------
    // TODO: Remove the mouse section BELOW
    // Mouse Handler
    //-------------------------------------------------------
    double mouseX;
    double mouseY;
    double previousMouseX;
    double previousMouseY;
    double fakePlayerLength = 64;
    double fakePlayerHeight = 24;
    double fakePlayerHeadRadius = 12;
    boolean fakePlayerIsAlive = true;
    int fakePlayerSize = 0;
    double mouseHeadingX;
    boolean mouse1Pressed = false;

    // MousePressed Event Handler
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            this.mouse1Pressed = true;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            this.mouse1Pressed = false;
        }
    }

    // MouseMoved Event Handler
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();

        if (this.previousMouseX < this.mouseX) {
            this.mouseHeadingX = 1.0;
        } else {
            this.mouseHeadingX = -1.0;
        }

        this.previousMouseX = this.mouseX;
        this.previousMouseY = this.mouseY;
    }

    public void FakeInitPlayer() {
        this.mouseX = (double)width / 2;
        this.mouseY = (double)height / 2;
    }

    public void FakeUpdatePlayer() {
        if (fakePlayerIsAlive) {
            if (this.mouse1Pressed) {
                ColliderCheck();
            }
        }
        // TODO: The player should have some lives?
        if (escKey) { fakePlayerIsAlive = true; }
        if (upKey) {
            fakePlayerSize += 1;
            if (fakePlayerSize > 2) {
                fakePlayerSize = 2;
            }
            System.out.println("Player Size: " + fakePlayerSize);
        }
        if (downKey) {
            fakePlayerSize -= 1;
            if (fakePlayerSize < 0) {
                fakePlayerSize = 0;
            }
            System.out.println("Player Size: " + fakePlayerSize);
        }
        // update player size based on thresholds
        if (this.env.GetCurrentGoal() > this.env.GetGrowthThresholdMedium()) {
            this.fakePlayerSize = 1;
        }
        if (this.env.GetCurrentGoal() > this.env.GetGrowthThresholdLarge()) {
            this.fakePlayerSize = 2;
        }
    }

    public void FakeDrawPlayer() {
        if (fakePlayerIsAlive) {
            double scale;
            if (fakePlayerSize == 1) {
                scale = 1.5;
            } else if (fakePlayerSize == 2) {
                scale = 2.0;
            } else {
                scale = 1.0;
            }

            double fakePlayerXOffset = this.mouseX - ((fakePlayerLength * scale) / 2);
            double fakePlayerYOffset = this.mouseY - ((fakePlayerHeight * scale) / 2);

            // draw fake body collider
            changeColor(0, 0, 0);
            drawLine(fakePlayerXOffset, fakePlayerYOffset, (fakePlayerXOffset + (fakePlayerLength * scale)), fakePlayerYOffset);
            drawLine(fakePlayerXOffset, (fakePlayerYOffset + (fakePlayerHeight * scale)), (fakePlayerXOffset + (fakePlayerLength * scale)), (fakePlayerYOffset + (fakePlayerHeight * scale)));
            drawLine(fakePlayerXOffset, fakePlayerYOffset, fakePlayerXOffset, (fakePlayerYOffset + (fakePlayerHeight * scale)));
            drawLine((fakePlayerXOffset + (fakePlayerLength * scale)), fakePlayerYOffset, (fakePlayerXOffset + (fakePlayerLength * scale)), (fakePlayerYOffset + (fakePlayerHeight * scale)));

            // draw fake head collider
            changeColor(0255, 0, 0);
            if (this.mouseHeadingX == -1.0) {
                drawCircle(fakePlayerXOffset, (fakePlayerYOffset + ((fakePlayerHeight * scale) / 2)), (fakePlayerHeadRadius * scale));
            } else {
                drawCircle((fakePlayerXOffset + (fakePlayerLength * scale)), (fakePlayerYOffset + ((fakePlayerHeight * scale) / 2)), (fakePlayerHeadRadius * scale));
            }
        }
    }
    // TODO: Remove the mouse section ABOVE

    //-------------------------------------------------------
    // TODO: Remove the key section BELOW
    // Key Presses
    //-------------------------------------------------------
    public boolean rightKey;
    public boolean leftKey;
    public boolean upKey;
    public boolean downKey;
    public boolean escKey;
    public boolean shiftKey;
    public boolean enterKey;
    public boolean spaceKey;

    // Called whenever a key is pressed
    public void keyPressed(KeyEvent e) {
        //The user pressed left arrow
        if(e.getKeyCode() == KeyEvent.VK_LEFT) { this.leftKey = true; }
        // The user pressed right arrow
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) { this.rightKey = true; }
        // The user pressed up arrow
        if(e.getKeyCode() == KeyEvent.VK_UP) { this.upKey = true; }
        // The user pressed up arrow
        if(e.getKeyCode() == KeyEvent.VK_DOWN) { this.downKey = true; }
        // The user pressed ESC
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { this.escKey = true; }
        // The user pressed shiftKey
        if(e.getKeyCode() == KeyEvent.VK_SHIFT) { this.shiftKey = true; }
        // The user pressed enterKey
        if(e.getKeyCode() == KeyEvent.VK_ENTER) { this.enterKey = true; }
        // The user pressed spaceKey
        if(e.getKeyCode() == KeyEvent.VK_SPACE) { this.spaceKey = true; }
    }

    // Called whenever a key is released
    public void keyReleased(KeyEvent e) {
        // The user released left arrow
        if(e.getKeyCode() == KeyEvent.VK_LEFT) { this.leftKey = false; }
        // The user released right arrow
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) { this.rightKey = false; }
        // The user released up arrow
        if(e.getKeyCode() == KeyEvent.VK_UP) { this.upKey = false; }
        // The user released up arrow
        if(e.getKeyCode() == KeyEvent.VK_DOWN) { this.downKey = false; }
        // The user released ESC
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { this.escKey = false; }
        // The user released shiftKey
        if(e.getKeyCode() == KeyEvent.VK_SHIFT) { this.shiftKey = false; }
        // The user released enterKey
        if(e.getKeyCode() == KeyEvent.VK_ENTER) { this.enterKey = false; }
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
    // TODO: Remove the key section ABOVE


    //-------------------------------------------------------
    // TODO: Required Section
    // TODO: Update fields if required, to work with team code
    // Other system related methods
    //-------------------------------------------------------
    Random rand;
    Image enemyFish1;
    Image enemyFish2;
    Image enemyFish3;
    Image shark;

    public void InitImages() {
        // TODO: The image should be updated with the actual images to used
        this.environmentImage = loadImage("assets/environment/test_background.png");
        this.enemyFish1 = loadImage("assets/enemies/test_fish1.png");
        this.enemyFish2 = loadImage("assets/enemies/test_fish2.png");
        this.enemyFish3 = loadImage("assets/enemies/test_fish3.png");
        this.shark = loadImage("assets/enemies/test_shark.png");
    }


    //-------------------------------------------------------
    // TODO: Required Section
    // TODO: Update fields if required, to work with team code
    // Environment methods
    //-------------------------------------------------------
    Environment env;
    boolean playAsTimeAttack = true;
    double timeAttackTimeLimit = 60.0;
    int width = 1200; // TODO: Can be replaced
    int height = 900; // TODO: Can be replaced
    int hudHeight = 80;
    int hudWidth = 10;
    Image environmentImage;
    int hudOffsetX = 10;
    int hudOffsetY = 20;
    int hudInfoSpacing = 2;
    int hudFontSize = 12;
    int hudBarThickness = 12;

    public void InitEnvironment() {
        // TODO: May need to update fields?
        this.env = new Environment(this.environmentImage, 64, 64, this.width, this.height, this.hudWidth, this.hudHeight, this.playAsTimeAttack, this.timeAttackTimeLimit);
        this.env.SetBaseTime(getTime());
        int target = 15;
        this.env.SetGrowthThresholdLarge(((target / 3) * 2));
        this.env.SetGrowthThresholdMedium((target / 3));
        this.env.SetTargetGoal(target);
    }

    public void UpdateEnvironment() {
        this.env.EnvironmentLevelCompleteCheck();

        boolean wasPaused = false;
        if (this.spaceKey) {
            if (this.env.GetIsPaused()) {
//                System.out.println("Un-Pausing");
                this.env.SetIsPaused(false);
                wasPaused = true;
            } else {
//                System.out.println("Pausing");
                this.env.SetIsPaused(true);
            }
        }

        UpdateTimer(wasPaused);
    }

    public void UpdateTimer(boolean wasPaused) {
        // return if paused
        if (this.env.GetIsPaused()) { return; }

        // fields for paused timer
        double newTime = getTime();
        double beforePause;
        double timeDelay;

        // Process the timer for paused case
        if (wasPaused) {
            beforePause = this.env.GetTimer();
            double afterPause = Math.round(((newTime - this.env.GetBaseTime()) / 1000.0) * 100.0) / 100.0;
            timeDelay = (afterPause - beforePause);

            // Set the pausable timer
            this.env.AddCountDownTimerOffset(timeDelay);
        }

        // Set the global timer
        this.env.SetTimer(newTime);

        // Time attack counter processing
        if (this.env.GetIsTimeAttack()) { this.env.SetCountDownCurrentTimer(this.env.GetCountDownTimerWOffset()); }
    }
    public void DrawEnvironment() {
        this.DrawLevel();
    }
    public void DrawEnvironmentLayerTop() {
        this.DrawHUD();
        boolean timeAttack = this.env.GetIsTimeAttack();
        this.DrawTimer(timeAttack);
        this.DrawScore();
        this.DrawGrowth();

    }

    public void DrawLevel() {
        // Get the environment image and display it first.
        Image envImg = this.env.GetEnvironment();
        int playWidth = this.env.GetPlayAreaWidth();
        int playHeight = this.env.GetPlayAreaHeight();
//        int envWidth = this.env.GetEnvironmentWidth();
//        int envHeight = this.env.GetEnvironmentHeight();
        double envXOffset = this.env.GetEnvironmentXOffset();
        double envYOffset = this.env.GetEnvironmentYOffset();

        drawImage(envImg, envXOffset, envYOffset, playWidth, playHeight);

//        // Use the play area, hud info to draw the hud?
//        int playX = env.GetPlayAreaX();
//        int playY = env.GetPlayAreaY();
//        int playWidth = env.GetPlayAreaWidth();
//        int playHeight = env.GetPlayAreaHeight();
////        int hudWidth = env.GetHUDWidth();
////        int hudHeight = env.GetHUDHeight();
//
//        drawImage(env.GetEnvironment(), playX, playY, playWidth, playHeight);
    }

    public void DrawScore() {
        int playerScore = env.GetScore();
        int clr = 0;
        changeColor(clr,clr,clr);
        drawText(this.hudOffsetX, (this.hudOffsetY + this.hudFontSize + this.hudInfoSpacing), ("Score: " + Double.toString(playerScore)), "Sans Serif", this.hudFontSize);
    }

    public void DrawGrowth() {
        double growthTarget = this.env.GetTargetGoal();
        double growthM = this.env.GetGrowthThresholdMedium();
        double growthL = this.env.GetGrowthThresholdLarge();

        double growthBarLength = 300;
        double growthBarXOffset = (this.width / 2) - growthBarLength;
        double growthBarYOffset = this.hudOffsetY * 2;

        double gSS = (((2 * growthBarLength) / growthTarget) * (growthM));
        double gSM = (((2 * growthBarLength) / growthTarget) * (growthL - growthM));
        double gSL = (((2 * growthBarLength) / growthTarget) * (growthTarget - growthL));

        // draw the small size
        int dark = 150;
        int lighter = 175;
        int light = 200;
        int basic = 120;
        changeColor(basic,dark,basic);
        drawSolidRectangle(growthBarLength, growthBarYOffset, gSS, this.hudBarThickness);

        // draw the medium size
        double nextBar = growthL - growthM;
        changeColor(basic,lighter,basic);
        drawSolidRectangle(growthBarLength + gSS, growthBarYOffset, gSM, this.hudBarThickness);

        // draw the large size
        double finalBar = growthTarget - growthL;
        changeColor(basic,light,basic);
        drawSolidRectangle(growthBarLength + gSS + gSM, growthBarYOffset, gSL, this.hudBarThickness);

        // Draw the current growth
        double growthState = (((2 * growthBarLength) / growthTarget) * (this.env.GetCurrentGoal()));
        if (growthState >= (2 * growthBarLength)) { growthState = (2 * growthBarLength); }
        changeColor(0,255,0);
        drawSolidRectangle(growthBarLength, growthBarYOffset, growthState, this.hudBarThickness);

        // draw the growth bar borders
        this.DrawPanelBorder(growthBarXOffset, growthBarYOffset, growthBarLength * 3, this.hudBarThickness);
    }

    public void DrawPanelBorder(double x, double y, double l, double h) {
        int clr = 0;
        changeColor(clr,clr,clr);
        int thickness = 2;
//        drawLine(x,y,l,y,thickness);
//        drawLine(x,h,l,h,thickness);
//        drawLine(x,y,x,h,thickness);
//        drawLine(l,y,l,h,thickness);

        drawLine(x,y,l,y,thickness);
        drawLine(x,y+h,l,y+h,thickness);
        drawLine(x,y,x,y+h,thickness);
        drawLine(l,y,l,y+h,thickness);
    }

    // Merged into the growth so don't need another HUD panel to show this.
//    public void DrawGoals() {
//        // TODO: Draw the goals for the level.
//    }

    public void DrawTimer(boolean isTimeAttack) {
//        double displayTime = this.env.GetTimer();
        double displayTime = this.env.GetCountDownTimerWOffset();
        int clr = 0;
        changeColor(clr,clr,clr);
        drawText(this.hudOffsetX, this.hudOffsetY, ("Time: " + Double.toString(displayTime)), "Sans Serif", this.hudFontSize);

        if (isTimeAttack) {
            DrawTimeAttackBar();
        }
    }

    public void DrawTimeAttackBar() {
//        double growthTarget = this.env.GetTargetGoal();
//        double growthM = this.env.GetGrowthThresholdMedium();
//        double growthL = this.env.GetGrowthThresholdLarge();
        double timeAttackLimit = this.env.GetCountDownTimer();
        double timeAttackRemaining = this.env.GetCountDownCurrentTimer();

        double timeAttackBarLength = 300;
        double timeAttackBarXOffset = (double)(this.width / 2) - timeAttackBarLength;
        double timeAttackBarYOffset = this.hudOffsetY * 3;

//        double gSS = (((2 * growthBarLength) / growthTarget) * (growthM));
//        double gSM = (((2 * growthBarLength) / growthTarget) * (growthL - growthM));
//        double gSL = (((2 * growthBarLength) / growthTarget) * (growthTarget - growthL));

        // draw the small size
        int dark = 120;
        int lighter = 175;
        int light = 240;
        int basic = 50;
        changeColor(light,basic,basic);
        drawSolidRectangle(timeAttackBarLength, timeAttackBarYOffset, timeAttackBarLength * 2, this.hudBarThickness);
//
//        // draw the medium size
//        double nextBar = growthL - growthM;
//        changeColor(basic,lighter,basic);
//        drawSolidRectangle(growthBarLength + gSS, growthBarYOffset, gSM, this.hudBarThickness);
//
//        // draw the large size
//        double finalBar = growthTarget - growthL;
//        changeColor(basic,light,basic);
//        drawSolidRectangle(growthBarLength + gSS + gSM, growthBarYOffset, gSL, this.hudBarThickness);

        // Draw the current remaining time
        double remainingTime = (((2 * timeAttackBarLength) / timeAttackLimit) * (timeAttackRemaining));
        if (remainingTime >= (2 * timeAttackBarLength)) { remainingTime = (2 * timeAttackBarLength); }

        changeColor(dark,basic,basic);
        drawSolidRectangle(timeAttackBarLength, timeAttackBarYOffset, remainingTime, this.hudBarThickness);

        // draw the time attack borders
        this.DrawPanelBorder(timeAttackBarXOffset, timeAttackBarYOffset, timeAttackBarLength * 3, this.hudBarThickness);
    }

    public void DrawHUD() {
        int hudWidth = this.env.GetHUDWidth();
        int hudHeight = this.env.GetHUDHeight();

        changeColor(180,180,255);
        // HUD left
        drawSolidRectangle(0,0, hudWidth, this.height);
        // HUD Right
        drawSolidRectangle((this.width - hudWidth),0, hudWidth, this.height);
        // HUD bottom
        drawSolidRectangle(0,(this.height - hudWidth), this.width, hudWidth);
        // HUD top
        drawSolidRectangle(0,0, this.width, hudHeight);
    }

    // Reference: https://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
    public boolean CalcDistCircleToSquare(double px, double py, double pr, double ex, double ey, double el, double eh) {
        double dPlayerX = Math.abs(px - ex);
        double dPlayerY = Math.abs(py - ey);

        // Check distance outside collider box beyond outer boundary
        if (dPlayerX > ((el / 2) + pr)) { return false; }
        if (dPlayerY > ((eh / 2) + pr)) { return false; }

        // Check whether circle is in collider box
        if (dPlayerX <= ((el / 2))) { return true; }
        if (dPlayerY <= ((eh / 2))) { return true; }

        // Check the corner distance
        double dCorner = Math.pow((dPlayerX - (el / 2)), 2) + Math.pow((dPlayerY - (eh / 2)), 2);
        return (dCorner <= Math.pow(pr, 2));
    }

    public void ColliderCheck() {
        double px;
        if (mouseHeadingX == -1.0) {
            px = this.mouseX - (this.fakePlayerLength / 2);
        } else {
            px = this.mouseX + (this.fakePlayerLength / 2);
        }
        double py = this.mouseY;
        double pr = this.fakePlayerHeadRadius;

        ArrayList<Enemy> removalValues = new ArrayList<Enemy>();
        for (Enemy en : this.enemies) {
            double ex = en.GetXPos();
            double ey = en.GetYPos();
            double el = en.GetColliderBodyLength();
            double eh = en.GetColliderBodyHeight();

            if (CalcDistCircleToSquare(px, py, pr, ex, ey, el, eh)) {
                int enSize = en.GetSize();
                if (this.fakePlayerSize >= enSize) {
                    int eatScore = en.GetSize() + 1;
                    this.env.SetScore((this.env.GetScore() + eatScore));
                    this.env.SetCurrentGoal(this.env.GetCurrentGoal() + 1);
                    removalValues.add(en);
                }
            }
        }

        this.RemoveEnemies(this.enemies, removalValues);
    }


    //-------------------------------------------------------
    // TODO: Required Section
    // TODO: Update fields if required, to work with team code
    // Enemy methods
    //-------------------------------------------------------
    int maxEnemies = 20; // Set to around 30
    ArrayList<Enemy> enemies;
    int maxShark = 5;
    ArrayList<Enemy> sharkEnemies;
    double enemySpawnTimer;
    double enemySpawnTimerPrevious;
    double enemySpawnDelay;

    public void InitEnemies() {
        // TODO: May need to update fields?
        this.enemies = new ArrayList<Enemy>();
        this.sharkEnemies = new ArrayList<Enemy>();
        this.rand = new Random();
        this.enemySpawnTimer = 0.0;
        this.enemySpawnTimerPrevious = 0.0;
        this.enemySpawnDelay = this.rand.nextDouble(2.0) + 1.0;
    }

    public void UpdateEnemies(double dt) {
        // Process the normal enemies
        ArrayList<Enemy> removalValues = new ArrayList<Enemy>();
        if (this.enemies.size() > 0) {
            for (int i = 0; i < this.enemies.size(); i++) {
                this.UpdateEnemyPosition(dt, this.enemies.get(i), true);

                // Check if the player has been bit
                this.CheckEnemyBitePlayer(this.enemies.get(i));

                this.CheckEnemyBounds(this.enemies.get(i), removalValues);
            }
        }
        this.RemoveEnemies(this.enemies, removalValues);

        // Process the shark enemy
        removalValues = new ArrayList<Enemy>();
        if (this.sharkEnemies.size() > 0) {
            for (int i = 0; i < this.sharkEnemies.size(); i++) {
                this.UpdateEnemyPosition(dt, this.sharkEnemies.get(i), false);

                // Check if the player has been bit
                this.CheckEnemyBitePlayer(this.sharkEnemies.get(i));
                this.CheckEnemyBounds(this.sharkEnemies.get(i), removalValues);
            }
        }
        this.RemoveEnemies(this.sharkEnemies, removalValues);
    }

    public void CheckEnemyBitePlayer(Enemy en) {
        // Enemy collider fields
        double ex;
        if (en.GetHeadingY() == -1.0) { ex = en.GetXPos() - en.GetColliderHeadXOffset(); }
        else { ex = en.GetXPos() + en.GetColliderHeadXOffset(); }
        double ey = en.GetYPos();
        double er = en.GetColliderHeadRadius();;

        // Player collider fields
        double px = this.mouseX;
        double py = this.mouseY;
        double pl = this.fakePlayerLength;
        double ph = this.fakePlayerHeight;

        // Player loses if they are "bit"
        if (CalcDistCircleToSquare(ex, ey, er, px, py, pl, ph)) {
            int enSize = en.GetSize();
            if (enSize > fakePlayerSize) { this.fakePlayerIsAlive = false; }
        }
    }

    public void UpdateEnemyPosition(double dt, Enemy en, boolean shouldRandomize) {
        if (shouldRandomize) {
            this.RandomizeEnemyMovement(en);
        }

        double currentXPos = en.GetXPos();
        double currentYPos = en.GetYPos();
        double currentXHeading = en.GetHeadingX();
        double currentYHeading = en.GetHeadingY();
        double currentVel = en.GetVelocity();

        double newXPos = currentXPos + currentXHeading * currentVel * dt;
        double newYPos = currentYPos + currentYHeading * currentVel * dt;

        en.SetPos(newXPos, newYPos);
    }

    public void RandomizeEnemyMovement(Enemy en) {
        // Roll each chance separately
        int changeChance = 1; // 1% chance
        // Randomize horizontal direction
        if (this.rand.nextInt(100) < changeChance) { en.SetRandomXHeading(); }
        // Randomize vertical direction
        if (this.rand.nextInt(100) < changeChance) { en.SetRandomYHeading(); }
        // Randomize velocity
        if (this.rand.nextInt(100) < changeChance) { en.SetRandomVelocity(25,100); }

        en.UpdateColliderHeadXOffset();
    }

    public void CheckEnemyBounds(Enemy en, ArrayList<Enemy> removalValues) {
        int chanceRemoveEnemy = this.rand.nextInt(100);
        boolean removeEnemy = (chanceRemoveEnemy < en.GetChanceToLeaveEnvironment());

        int playAreaWidth = this.env.GetPlayAreaWidth();
        int playAreaHeight = this.env.GetPlayAreaHeight();
        double playAreaXOffset = this.env.GetEnvironmentXOffset();
        double playAreaYOffset = this.env.GetEnvironmentYOffset();

        double bodyLengthOffset =  en.GetColliderBodyLength() / 2;
        double bodyHeightOffset =  en.GetColliderBodyHeight() / 2;

        // Check horizontal bounds
        if ((en.GetXPos() <= (playAreaXOffset - bodyLengthOffset)) && (en.GetHeadingX() == -1.0)) {
            if (removeEnemy) { removalValues.add(en); }
            else {
                en.SetXHeading(1.0);
                en.UpdateColliderHeadXOffset();
            }
        }
        if ((en.GetXPos() >= (playAreaXOffset + playAreaWidth + bodyLengthOffset)) && (en.GetHeadingX() == 1.0)) {
            if (removeEnemy) { removalValues.add(en); }
            else {
                en.SetXHeading(-1.0);
                en.UpdateColliderHeadXOffset();
            }
        }

        // Check vertical bounds
        if (en.GetYPos() <= (playAreaYOffset + bodyHeightOffset)) { en.SetYHeading(1.0); }
        if (en.GetYPos() >= (playAreaYOffset + playAreaHeight - bodyHeightOffset)) { en.SetYHeading(-1.0); }
    }

    public void RemoveEnemies(ArrayList<Enemy> enemies, ArrayList<Enemy> removalValues) {
        // Remove the enemies that have been selected for removal.
        if (removalValues.size() > 0) {
            for (Enemy toRemove : removalValues) {
                enemies.remove(toRemove);
            }
        }
    }

    // The method to spawn an enemy on click
    public void SpawnEnemy() {
        this.enemySpawnTimer = this.env.GetCountDownTimerWOffset() - this.enemySpawnTimerPrevious;
        if (this.enemySpawnTimer > this.enemySpawnDelay) {
            this.CreateEnemy();
            this.enemySpawnDelay = this.rand.nextDouble(2.0) + 1.0;
            this.enemySpawnTimerPrevious = this.enemySpawnTimer;
        }

        // TODO: consider when a shark should spawn
        if (this.shiftKey) {
            this.CreateShark();
        }
    }

    public void DrawEnemies(ArrayList<Enemy> enemies) {
        for (Enemy en : enemies) {
            this.DrawEnemy(en);
            this.DrawEnemyCollider(en);
            this.DrawEnemyHeadCollider(en);
        }
    }

    public void DrawEnemy(Enemy en) {
        // TODO: Calculate the enemy image offset, to align with the collision box
        double imgX = en.GetImageXOffset();
        double imgY = en.GetImageYOffset();
        double imgLen = en.GetImageLength();
        double imgHei = en.GetImageHeight();
        Image enImg = en.GetImage();

        // Draw the enemy image
        drawImage(enImg, imgX, imgY, imgLen, imgHei);
    }

    public void DrawEnemyCollider(Enemy en) {
        // Retrieve the enemy collision fields
        double xPosCol = en.GetColliderBodyXOffset();
        double yPosCol = en.GetColliderBodyYOffset();
        double len = en.GetLength();
        double hei = en.GetHeight();
        double xPos = en.GetXPos();
        double yPos = en.GetYPos();

        // Calculate the collision offsets
        double x1 = xPos + xPosCol;
        double y1 = yPos + yPosCol;
        double x2 = xPos + xPosCol + len;
        double y2 = yPos + yPosCol + hei;

        // Draw the collision boxes
        changeColor(0, 255, 0);
        drawLine(x1,y1,x2,y1); // Collision line
        drawLine(x1,y2,x2,y2); // Collision line
        drawLine(x1,y1,x1,y2); // Collision line
        drawLine(x2,y1,x2,y2); // Collision line
        changeColor(255, 0, 0);
        drawSolidCircle(xPos, yPos, 2);
    }

    public void DrawEnemyHeadCollider(Enemy en) {
        double xH = en.GetColliderHeadXOffset();
        double yH = en.GetColliderHeadYOffset();
        double rH = en.GetColliderHeadRadius();
        double x = en.GetXPos();
        double y = en.GetYPos();

        double x1 = x + xH;
        double y1 = y + yH;

        changeColor(255,0,0);
        drawCircle(x1, y1, rH);
    }

    public void CreateEnemy() {
        if (this.enemies.size() >= this.maxEnemies) { return; }

        // Randomize enemy selection
        // Should the spawn chances be modified such as:
        // - 45% small
        // - 33% medium
        // - 22% large
        final int enemySmall = 45;
        final int enemyMedium = 78; // 78 = 45 + 33
        int selectSize = this.rand.nextInt(100);

        int playAreaWidth = this.env.GetPlayAreaWidth();
        int playAreaHeight = this.env.GetPlayAreaHeight();
        double playAreaXOffset = this.env.GetEnvironmentXOffset();
        double playAreaYOffset = this.env.GetEnvironmentYOffset();

        Enemy en;
        if (selectSize < enemySmall) { // spawn a small fish
            en = new Enemy(this.enemyFish1, 0, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
            // TODO: Match these offset values to the actual image sizes
            en.SetImageOffsets(-32,-32,64,64);
        } else if (selectSize < enemyMedium) { // spawn a medium fish
            en = new Enemy(this.enemyFish2, 1, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
            // TODO: Match these offset values to the actual image sizes
            en.SetImageOffsets(-32,-32,64,64);
        } else { // spawn a large fish
            en = new Enemy(this.enemyFish3, 2, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
            // TODO: Match these offset values to the actual image sizes
            en.SetImageOffsets(-32,-32,64,64);
        }

        this.enemies.add(en);
    }

    // TODO: Add more functionality?
//    public void SharkWarning() {}
//    public void DrawSharkWarning() {}

    public void CreateShark() {
        if (this.sharkEnemies.size() >= this.maxShark) {
            System.out.println("Max sharks.");
            return;
        }

        int playAreaWidth = env.GetPlayAreaWidth();
        int playAreaHeight = env.GetPlayAreaHeight();
        double playAreaXOffset = env.GetEnvironmentXOffset();
        double playAreaYOffset = env.GetEnvironmentYOffset();

        Enemy shk = new Enemy(this.shark, 3, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
        shk.SetRandomVelocity(400, 400);
        shk.SetYHeading(0.0);
        shk.SetChanceToLeaveEnvironment(100);
        this.sharkEnemies.add(shk);
    }
}
