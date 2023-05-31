/*
 * Author: Robert Tubman
 * ID: 11115713
 *
 * Reference: Paul (Zeju Fan)
 * ID:
 *
 * The initial state for the audio handling was made by Paul in the StartMenu class, but
 * a more comprehensive audio handling class was required to keep the audio processing
 * in one location, so the code from the StartMenu class was refactored here.
 *
 * The AudioHandler Class
 *
 * Use this class to instantiate the game audio.
 * This will be used for music and in game sound effects.
 */

package Assignment2;

public class AudioHandler {
    private final GameEngine engine;
    private GameEngine.AudioClip[] music;
    private GameEngine.AudioClip[] sfx;
    private final int audioMusicCap;
    private final int audioSFXCap;
    private float volume;
    private boolean pauseMusic;
    private boolean pauseSFX;
    private boolean[] musicIsPlaying;
    private int currentPlayingMusic;

    public AudioHandler(GameEngine engine, int audioMusicCap, int audioSFXCap) {
        this.engine = engine;
        this.audioMusicCap = audioMusicCap;
        this.audioSFXCap = audioSFXCap;

        musicIsPlaying = new boolean[audioMusicCap];
        pauseMusic = false;
        pauseSFX = false;
        volume = 1.0f;
        music = new GameEngine.AudioClip[audioMusicCap];
        sfx = new GameEngine.AudioClip[audioSFXCap];
    }



    //**************************************************
    // Setter
    //**************************************************
    public boolean setAudioMusicClips(String[] audioMusicPaths) throws IllegalArgumentException {
//        System.out.println("audioMusicPaths len: " + audioMusicPaths.length);
        if ((audioMusicPaths.length == 0) || (audioMusicPaths.length > audioMusicCap)) { throw new IllegalArgumentException("The audio music path cannot out of bounds"); }
        try {
            // Retrieve audio files from paths
            for (int i = 0; i < audioMusicCap; i++) { music[i] = engine.loadAudio(audioMusicPaths[i]); }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean setAudioSFXClips(String[] audioSFXPaths) throws IllegalArgumentException {
        if ((audioSFXPaths.length == 0) || (audioSFXPaths.length > audioSFXCap)) { throw new IllegalArgumentException("The audio sfx path cannot out of bounds"); }
        try {
            // Retrieve audio files from paths
            for (int i = 0; i < audioSFXCap; i++) { sfx[i] = engine.loadAudio(audioSFXPaths[i]); }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public void setVolume(float volume) { this.volume = volume; }
    public void setMusicIsPlaying(int index, boolean musicIsPlaying) {
        if ((index < 0) || (index >= audioMusicCap)) { throw new IllegalArgumentException("Cannot set music is playing, index is out of bounds"); }
        this.musicIsPlaying[index] = musicIsPlaying;
    }
    public void setCurrentMusic(int currentPlayingMusic) { this.currentPlayingMusic = currentPlayingMusic; }
    public void setPauseMusic(boolean pauseMusic) { this.pauseMusic = pauseMusic; }
    public void setPauseSFX(boolean pauseSFX) { this.pauseSFX = pauseSFX; }



    //**************************************************
    // Getter
    //**************************************************
    public float getVolume() { return volume; }
    public boolean getMusicIsPlaying() {
        return getMusicIsPlaying(currentPlayingMusic);
    }
    public boolean getMusicIsPlaying(int index) {
        if ((index < 0) || (index >= audioMusicCap)) { throw new IllegalArgumentException("Cannot get music is playing, index is out of bounds"); }
        return musicIsPlaying[index];
    }
    public int getCurrentPlayingMusic() { return currentPlayingMusic; }
    public boolean getPauseMusic() { return pauseMusic; }
    public boolean getPauseSFX() { return pauseSFX; }



    //**************************************************
    // Other methods
    //**************************************************
    public void startAudioMusicLoop(int index) throws IllegalArgumentException {
        if (pauseMusic) { return; }
        if ((index < 0) || (index >= audioMusicCap)) { throw new IllegalArgumentException("Cannot start audio music, index is out of bounds"); }
        engine.startAudioLoop(music[index], volume);
        currentPlayingMusic = index;
    }
    public void startCurrentAudioMusic() {
        if (pauseMusic) { return; }
        engine.startAudioLoop(music[currentPlayingMusic], volume);
    }
    public void stopAudioMusicLoop(int index) throws IllegalArgumentException {
        if ((index < 0) || (index >= audioMusicCap)) { throw new IllegalArgumentException("Cannot stop audio music, index is out of bounds"); }
        engine.stopAudioLoop(music[index]);
    }
    public void stopCurrentAudioMusic() {
        engine.stopAudioLoop(music[currentPlayingMusic]);
    }
    public void playAudioSFX(int index) throws IllegalArgumentException {
        if (pauseSFX) { return; }
        if ((index < 0) || (index >= audioSFXCap)) { throw new IllegalArgumentException("Cannot play audio sfx, index is out of bounds"); }
        engine.playAudio(sfx[index], volume);
    }

}
