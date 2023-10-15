package com.ilnitsk.animusic.services;

import com.ilnitsk.animusic.models.Playlist;
import com.ilnitsk.animusic.repositories.AnimeRepository;
import com.ilnitsk.animusic.repositories.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {
    private final AnimeRepository animeRepository;
    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistService(AnimeRepository animeRepository, PlaylistRepository playlistRepository) {
        this.animeRepository = animeRepository;
        this.playlistRepository = playlistRepository;
    }

    public Playlist createPlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public List<Playlist> getPlaylistsByAnimeId(Integer animeId) {
        return playlistRepository.getPlaylistsByAnimeId(animeId);
    }

    public Playlist getPlaylistsById(Integer id) {
        Optional<Playlist> entity = playlistRepository.findById(id);
        if (entity.isEmpty())  {
            throw new IllegalArgumentException("No playlist with id="+id);

        }
        Playlist playlist = entity.get();
        String animeTitle = playlist.getAnime().getTitle();
        playlist.getSoundtracks()
                .forEach(s -> s.setAnimeName(animeTitle));
        String playlistName = playlist.getName();
        if (playlistName.equals("Openings") || playlistName.equals("Endings")) {
            playlist.getSoundtracks().sort(Comparator.comparingInt(
                    a -> Integer.parseInt(a.getAnimeTitle().split(" ")[1]))
            );
        }
        return entity.get();
    }

}

