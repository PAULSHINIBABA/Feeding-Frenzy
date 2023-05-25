///*
// * Author: Paul
// * ID:
// *
// * Co-Author: Robert Tubman (Tweaked to merge with team code)
// * ID: 11115713
// */
//
//package Assignment2;
//
//import java.awt.*;
//import java.awt.event.MouseEvent;
//
//public class INGAMEMENU extends GameEngine{
//
//    private StartMenu startMenu;
//
//    public INGAMEMENU(StartMenu startMenu) {
//        this.startMenu = startMenu;
//    }
//
//
////    public static void main(String args[]) {
////        createGame(new INGAMEMENU(null));
////    }
//
//    Image background;
//
//    Image backicon;
//
//    Image MusicButton;
//
//    public void init(){
//        background = loadImage("src/NEW IMAGE/4.png");
//        backicon = loadImage("src/NEW IMAGE/Backicon.png");
//        MusicButton = loadImage("src/NEW IMAGE/MUSICbutton.png");
//
//    }
//
//    public void drawbackground(){
//        saveCurrentTransform();
//        drawImage(background, 0, 0, 300, 300);
//        restoreLastTransform();
//    }
//
//    public void drawbackicon(){
//        saveCurrentTransform();
//        drawImage(backicon, 0, 0, 50, 50);
//        restoreLastTransform();
//    }
//
//    public void drawMusicButton(){
//        saveCurrentTransform();
//        drawImage(MusicButton, 120, 50, 50, 50);
//        restoreLastTransform();
//    }
//
//    public void mouseClicked(MouseEvent e) {
//        double mouseX = e.getX();
//        double mouseY = e.getY();
//
//        double BmouseX = 25;
//        double BmouseY = 25;
//        double radius = 25;
//        if (clickButton(mouseX, mouseY, BmouseX, BmouseY, radius)) {
//            StartMenu startMenu = new StartMenu();
//            createGame(startMenu);
//        }
//
//        double MmouseX = 145;
//        double MmouseY = 75;
//        double Mradius = 25;
//        if (clickButton(mouseX, mouseY, MmouseX, MmouseY, Mradius)) {
//
//                startMenu.pauseMusic();
//        }
//    }
//
//    public boolean clickButton(double mouseX, double mouseY, double buttonX, double buttonY, double radius) {
//        double dx = mouseX - buttonX;
//        double dy = mouseY - buttonY;
//        return (dx * dx + dy * dy) <= (radius * radius);
//    }
//
//    @Override
//    public void update(double dt) {
//
//    }
//
//    @Override
//    public void paintComponent() {
//
//        setWindowSize(300,300);
//
//        drawbackground();
//        drawbackicon();
//        drawMusicButton();
//
//    }
//
//    @Override
//    public int width() {
//        return 300;
//    }
//
//    @Override
//    public int height() {
//        return 300;
//    }
//}