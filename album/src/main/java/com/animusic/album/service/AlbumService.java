package com.animusic.album.service;

import com.animusic.album.AlbumAlreadyExistsException;
import com.animusic.album.AlbumNotFoundException;
import com.animusic.album.dao.Album;
import com.animusic.album.repository.AlbumRepository;
import com.animusic.anime.AnimeNotFoundException;
import com.animusic.anime.dao.Anime;
import com.animusic.anime.service.AnimeService;
import com.animusic.image.dao.CoverArt;
import com.animusic.image.service.CoverArtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final AnimeService animeService;
    private final CoverArtService coverArtService;

    @Transactional
    public Album createAlbum(Album album, Integer animeId) {
        Anime anime = animeService.getAnimeInfo(animeId);
        if (albumRepository.existsByNameAndAnimeId(album.getName(), animeId)) {
            throw new AlbumAlreadyExistsException(album.getName(), animeId);
        }
        album.setAnime(anime);
        albumRepository.save(album);
        return album;
    }

    public List<Album> getAlbumsByAnimeId(Integer animeId) {
        Optional<List<Album>> albums = albumRepository.getAlbumsByAnimeId(animeId);
        if (albums.isEmpty()) {
            throw new AnimeNotFoundException(animeId);
        }
        return albums.get();
    }

    public Album getAlbumById(Integer id) {
        Optional<Album> entity = albumRepository.findById(id);
        if (entity.isEmpty()) {
            throw new AlbumNotFoundException(id);
        }
        Album album = entity.get();
        String albumName = album.getName();
        if (albumName.equals("Openings") || albumName.equals("Endings")) {
            album.getSoundtracks().sort(Comparator.comparingInt(
                    a -> {
                        String title = a.getAnimeTitle();
                        if (title.matches(".*\\d+-\\d+.*")) {
                            // Формат "Ending 5-6", извлекаем оба номера и возвращаем их среднее
                            String[] parts = title.split("-");
                            int number1 = Integer.parseInt(parts[0].replaceAll("\\D+", ""));
                            int number2 = Integer.parseInt(parts[1].replaceAll("\\D+", ""));
                            return (number1 + number2) / 2;
                        } else {
                            // Стандартный формат, извлекаем номер
                            return Integer.parseInt(title.replaceAll("\\D+", ""));
                        }
                    }
            ));
        }
        return entity.get();
    }

    public void deleteAlbum(Integer id) {
        albumRepository.deleteById(id);
    }

    @Transactional
    public Album updateAlbumName(String newName, Integer albumId) {
        return albumRepository.findById(albumId).map(
                album -> {
                    album.setName(newName);
                    return album;
                }
        ).orElseThrow(() -> new AlbumNotFoundException(albumId));
    }

    @Transactional
    public CoverArt createCoverArt(Integer albumId, MultipartFile imageFile, String colorLight, String colorDark) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException(albumId));
        CoverArt coverArt = CoverArt.builder()
                .colorLight(colorLight)
                .colorDark(colorDark)
                .build();
        coverArt = coverArtService.createAlbumCoverArt(
                album.getAnime().getFolderName(),
                album.getName().toUpperCase(),
                imageFile,
                coverArt);
        album.setCoverArt(coverArt);
        return coverArt;
    }
}

