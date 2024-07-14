package com.animusic.playlist;

public class PlaylistNotFoundException extends RuntimeException {
    public PlaylistNotFoundException(Integer id) {
        super("Playlist with id " + id + " not found");
    }
}
