package org.Logic;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.media.*;
import javafx.util.Duration;
import org.Data.ReadFiles;


/**
 * The Music player class allow you to listen music during the drive the car.
 */
public class MusicPlayer {
    private ReadFiles loadFiles = new ReadFiles();
    private ArrayList<Media> playlist = new ArrayList<>();
    private MediaPlayer mediaPlayer = null;
    private double volume = 75;
    private int currentSong = 0;
    private String playlistDirectoryPath;
    private Runnable autoPlayNext = null;

    /**
     * Gets title of song.
     *
     * @return the title of song
     */
    public String getTitle() {
        if(playlist.isEmpty())
            return "Playlist is empty";
        String title;
        try{
            title = mediaPlayer.getMedia().getMetadata().get("title").toString();
        } catch (NullPointerException e) {
            return new File(playlist.get(currentSong).getSource()).getName().replaceFirst(".mp3", "");
        }
        return title;
    }

    /**
     * Gets artist of song.
     *
     * @return the artist of song
     * @throws InterruptedException the interrupted exception
     */
    public String getArtist() throws InterruptedException {
        if(playlist.isEmpty())
            return "Unknown";
        String artistName;
        try{
            artistName = mediaPlayer.getMedia().getMetadata().get("artist").toString();
        } catch (NullPointerException e) {
            return "Unknown";
        }

        return artistName;
    }

    /**
     * Play song.
     */
    public void playSong() {
        if(playlist.isEmpty())
            return;
        else if(mediaPlayer == null)
            mediaPlayer = new MediaPlayer(playlist.get(currentSong));

        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
        if(autoPlayNext != null)
            mediaPlayer.setOnEndOfMedia(autoPlayNext);
    }

    /**
     * Auto play next (shuffle mode).
     *
     * @param runnable method that allows to play next song
     */
    public void autoPlayNext(Runnable runnable) {
        if(mediaPlayer != null)
            mediaPlayer.setOnEndOfMedia(runnable);
    }

    /**
     * Pause song.
     */
    public void pauseSong() {
        if(mediaPlayer != null)
            mediaPlayer.pause();
    }

    /**
     * Stop song.
     */
    public void stopSong() {
        if(mediaPlayer != null)
            mediaPlayer.stop();
    }

    /**
     * Dispose actual MediaPlayer object.
     */
    public void dispose() {
        if(mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
            currentSong = 0;
        }
    }

    /**
     * Sets auto play next.
     *
     * @param autoPlayNext the auto play next
     */
    public void setAutoPlayNext(Runnable autoPlayNext) {
        this.autoPlayNext = autoPlayNext;
    }

    /**
     * Shuffle playlist.
     */
    public void shufflePlaylist() {
        if(!playlist.isEmpty())
            Collections.shuffle(playlist);
    }

    /**
     * Next song.
     */
    public void nextSong() {
        if(playlist.isEmpty())
            return;
        boolean wasPlaying = isPlaying();
        if(mediaPlayer != null)
            mediaPlayer.stop();
        currentSong += 1;
        if(currentSong >= playlist.size()) currentSong = 0;
        mediaPlayer = new MediaPlayer(playlist.get(currentSong));
        if(wasPlaying)
            playSong();
    }

    /**
     * Previous song.
     */
    public void previousSong() {
        if(playlist.isEmpty())
            return;
        boolean wasPlaying = isPlaying();
        if(mediaPlayer != null)
            mediaPlayer.stop();
        currentSong -= 1;
        if(currentSong < 0) currentSong = playlist.size()-1;
        mediaPlayer = new MediaPlayer(playlist.get(currentSong));
        if(wasPlaying)
            playSong();
    }

    /**
     * Is playing boolean.
     *
     * @return the boolean
     */
    public boolean isPlaying() {
        if(mediaPlayer == null)
            return false;

        return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    /**
     * Load songs.
     *
     * @param directoryPath the directory path
     * @throws IOException the io exception
     */
    public void loadSongs(String directoryPath) throws IOException {
        playlistDirectoryPath = directoryPath;
        List<String> result = loadFiles.loadMp3Files(directoryPath);
        playlist = new ArrayList<>();
        MediaPlayer initMedia = null;
        for(int i = 0; i < result.size(); i++) {
            playlist.add(new Media(new File(result.get(i)).toURI().toString()));
            initMedia = new MediaPlayer(playlist.get(i));
        }
    }

    /**
     * Change volume.
     *
     * @param volume the volume
     */
    public void changeVolume(double volume) {
        if(mediaPlayer != null)
            mediaPlayer.setVolume(volume);

        this.volume = volume;
    }

    /**
     * Gets total duration of song.
     *
     * @return the total duration of song
     */
    public Duration getTotalDuration() {
        if(mediaPlayer == null)
            return Duration.UNKNOWN;

        return mediaPlayer.getStopTime();
    }

    /**
     * Gets current time of song.
     *
     * @return the current time of song
     */
    public double getCurrentTime() {
        if(mediaPlayer == null)
            return 0;
        return mediaPlayer.getCurrentTime().toSeconds();
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return playlist.isEmpty();
    }


    /**
     * Sets playlist directory path.
     *
     * @param playlistDirectoryPath the playlist directory path
     */
    public void setPlaylistDirectoryPath(String playlistDirectoryPath) {
        this.playlistDirectoryPath = playlistDirectoryPath;
    }
}
