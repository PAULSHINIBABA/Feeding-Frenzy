package Assignment2;

import java.awt.*;
import java.awt.event.MouseEvent;

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
        updateMenu();
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
    public int windowX;
    public int windowY;
    public int windowWidth;
    public int windowHeight;
    public int startMenuX;
    public int startMenuY;
    public int startMenuWidth;
    public int startMenuHeight;
    public int startMenuButtonWidth;
    public int startMenuButtonHeight;
    public int startMenuButtonXOffset;
    public int startMenuButtonYOffset;
    public int startMenuMusicButtonX;
    public int startMenuMusicButtonY;
//    public int startMenuMusicButtonRadius;

    public int startMenuMusicButtonWidth;
    public int startMenuMusicButtonHeight;


    public void InitSystem() {
        // Window fields
        this.windowX = 0;
        this.windowY = 0;
        this.windowWidth = 500;
        this.windowHeight = 500;

        // Start menu fields
        this.startMenuX = 110;
        this.startMenuY = 0;
        this.startMenuWidth = 300;
        this.startMenuHeight = 150;

        this.startMenuButtonWidth = 120;
        this.startMenuButtonHeight = 50;
        this.startMenuButtonXOffset = -30;
        this.startMenuButtonYOffset = -30;

        this.startMenuMusicButtonX = 450;
        this.startMenuMusicButtonY = 450;
//        this.startMenuMusicButtonRadius = 25;
        this.startMenuMusicButtonWidth = 50;
        this.startMenuMusicButtonHeight = 50;

    }



    //-------------------------------------------------------
    // Mouse Handler
    //-------------------------------------------------------
    public double mouseX;
    public double mouseY;
    boolean mouse1Pressed = false;

    // MousePressed Event Handler
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) { this.mouse1Pressed = true; }
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
    // Menu methods
    //-------------------------------------------------------
    public StartMenu sm;
    public int menuState;
    public LoadingPage lp;

    public void initMenu() {
        sm = new StartMenu(this.windowX, this.windowY, this.windowWidth, this.windowHeight,
                this.startMenuX, this.startMenuY, this.startMenuWidth, this.startMenuHeight,
                this.startMenuButtonWidth, this.startMenuButtonHeight, this.startMenuButtonXOffset, this.startMenuButtonYOffset,
                this.startMenuMusicButtonX, this.startMenuMusicButtonY, this.startMenuMusicButtonWidth, this.startMenuMusicButtonHeight);

//        MUSICbutton = loadImage("src/Assignment2/NEW IMAGE/MUSICbutton.png");

        // Set background image
        this.sm.SetBackgroundImage( loadImage("Assignment2/assets/image/B1.png") );
        // Set title image
        this.sm.SetTitleImage( loadImage("Assignment2/assets/image/title.png") );

        // Set button images
        Image[] buttonImages = new Image[5];
        buttonImages[0] = loadImage("Assignment2/assets/image/StartButton.png");
        buttonImages[1] = loadImage("Assignment2/assets/image/TIMERACE.png");
        buttonImages[2] = loadImage("Assignment2/assets/image/SETTINGS.png");
        buttonImages[3] = loadImage("Assignment2/assets/image/QUIT.png");
        buttonImages[4] = loadImage("Assignment2/assets/image/HELP.png");
        this.sm.SetButtonImages(buttonImages);

        // Set music button image
        this.sm.SetMusicButtonImage(loadImage("Assignment2/assets/image/MUSICbutton.png"));

//        lp = new LoadingPage();

        this.menuState = -1; // Nothing?
    }
    public void updateMenu() {
        this.menuState = sm.MenuMouseClicked(this.mouseX, this.mouseY);
        if (this.menuState == 0) {
            System.out.println("Time attack?");

        } else if (this.menuState == 1) {
            System.out.println("Single player");

        }
    }
    public void drawMenu() {
        saveCurrentTransform();

        // Draw start menu background
        drawImage(this.sm.GetBackgroundImage(),
                this.sm.GetBackgroundX(), this.sm.GetBackgroundY(),
                this.sm.GetBackgroundWidth(), this.sm.GetBackgroundHeight());

        // Draw start menu title
        drawImage(this.sm.GetTitleImage(),
                this.sm.GetTitleX(), this.sm.GetTitleY(),
                this.sm.GetTitleWidth(), this.sm.GetTitleHeight());

        // Draw start menu buttons
        int numberOfButtons = this.sm.GetNumberOfButtons();
        for (int i = 0; i < numberOfButtons; i++) {
            double x = this.sm.GetButtonXByIndex(i);
            double y = this.sm.GetButtonYByIndex(i);
            double w = this.sm.GetButtonsWidth();
            double h = this.sm.GetButtonsHeight();
//            System.out.println("i:" + i + "\tx:" + x + "\ty:" + y + "\tw:" + w + "\th:" + h);
            drawImage(this.sm.GetButtonImageByIndex(i), x, y, w, h);
        }

        drawImage(this.sm.GetMusicButton(),
                this.sm.GetMusicButtonX(), this.sm.GetMusicButtonY(),
                this.sm.GetMusicButtonWidth(), this.sm.GetButtonsHeight());


        restoreLastTransform();

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
