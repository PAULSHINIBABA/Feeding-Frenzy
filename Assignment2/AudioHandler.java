/*
 * Author: Robert Tubman
 * ID: 11115713
 *
 * Reference: Paul (Zeju Fan)
 * ID: 21019135
 *
 * Team:
 * David, 22004319
 * Lucas (Xidi Kuang), 21008041
 * Paul (Zeju Fan), 21019135
 * Robert Tubman, 11115713
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
    // Final fields
    private final GameEngine engine;
    private final int audioMusicCap;
    private final int audioSFXCap;

    // Non-final fields
    private GameEngine.AudioClip[] music;
    private GameEngine.AudioClip[] sfx;
    private float volume;
    private boolean pauseMusic;
    private boolean pauseSFX;
    private boolean[] musicIsPlaying;
    private int currentPlayingMusic;

    // Constructor
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
    // Set the audio music clips from the file path
    public boolean setAudioMusicClips(String[] audioMusicPaths) throws IllegalArgumentException {
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

    // Set the audio sfx clips from the file path
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

    // Set the volume of all audio
    public void setVolume(float volume) { this.volume = volume; }

    // Set the whether the music at the index is playing
    public void setMusicIsPlaying(int index, boolean musicIsPlaying) {
        if ((index < 0) || (index >= audioMusicCap)) { throw new IllegalArgumentException("Cannot set music is playing, index is out of bounds"); }
        this.musicIsPlaying[index] = musicIsPlaying;
    }

    // Set the currently selected music to be the music at the index
    public void setCurrentMusic(int currentPlayingMusic) { this.currentPlayingMusic = currentPlayingMusic; }

    // set the music to be audible or not
    public void setPauseMusic(boolean pauseMusic) { this.pauseMusic = pauseMusic; }

    // Set the sfx to be audible or not
    public void setPauseSFX(boolean pauseSFX) { this.pauseSFX = pauseSFX; }



    //**************************************************
    // Getter
    //**************************************************
    // Get the current volume of all audio
    public float getVolume() { return volume; }

    // Get whether the currently selected music is playing
    public boolean getMusicIsPlaying() {
        return getMusicIsPlaying(currentPlayingMusic);
    }

    // Get the boolean state of whether the music at index is playing or not
    public boolean getMusicIsPlaying(int index) {
        if ((index < 0) || (index >= audioMusicCap)) { throw new IllegalArgumentException("Cannot get music is playing, index is out of bounds"); }
        return musicIsPlaying[index];
    }

    // Get the index of the currently playing music
    public int getCurrentPlayingMusic() { return currentPlayingMusic; }

    // Get whether the music should be audible or not
    public boolean getPauseMusic() { return pauseMusic; }

    // Get whether the sfx should be audible or not
    public boolean getPauseSFX() { return pauseSFX; }



    //**************************************************
    // Other methods
    //**************************************************
    // Start the audio music to loop
    public void startAudioMusicLoop(int index) throws IllegalArgumentException {
        if (pauseMusic) { return; }
        if ((index < 0) || (index >= audioMusicCap)) { throw new IllegalArgumentException("Cannot start audio music, index is out of bounds"); }
        engine.startAudioLoop(music[index], volume);
        currentPlayingMusic = index;
    }

    // Start the currently selected music
    public void startCurrentAudioMusic() {
        if (pauseMusic) { return; }
        engine.startAudioLoop(music[currentPlayingMusic], volume);
    }

    // Stop the music at index
    public void stopAudioMusicLoop(int index) throws IllegalArgumentException {
        if ((index < 0) || (index >= audioMusicCap)) { throw new IllegalArgumentException("Cannot stop audio music, index is out of bounds"); }
        engine.stopAudioLoop(music[index]);
    }

    // Stop the currently playing music (based on the current music index)
    public void stopCurrentAudioMusic() {
        engine.stopAudioLoop(music[currentPlayingMusic]);
    }

    // Play the audio sfx at index
    public void playAudioSFX(int index) throws IllegalArgumentException {
        if (pauseSFX) { return; }
        if ((index < 0) || (index >= audioSFXCap)) { throw new IllegalArgumentException("Cannot play audio sfx, index is out of bounds"); }
        engine.playAudio(sfx[index], volume);
    }

}
