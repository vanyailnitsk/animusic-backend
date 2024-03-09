package com.ilnitsk.animusic.user.controller;

import com.ilnitsk.animusic.user.dao.UserPlaylist;
import com.ilnitsk.animusic.user.service.UserMediaLibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-media-library")
@RequiredArgsConstructor
public class UserMediaLibraryController {
    private final UserMediaLibraryService userMediaLibraryService;

    @GetMapping("/favourites")
    public UserPlaylist getFavouriteTracksPlaylist() {
        return userMediaLibraryService.getFavouriteTracksPlaylist();
    }

    @PostMapping("/favourites")
    public void addTrackToFavourites(@RequestParam Integer trackId) {
        userMediaLibraryService.addTrackToFavourites(trackId);
    }

    @DeleteMapping("/favourites")
    public void deleteTrackFromFavourites(@RequestParam Integer trackId) {
        userMediaLibraryService.deleteTrackFromFavourites(trackId);
    }

    @PostMapping
    public UserPlaylist createPlaylist(@RequestParam String playlistName) {
        return userMediaLibraryService.createPlaylist(playlistName);
    }


}
