package com.ilnitsk.animusic.repositories;


import com.ilnitsk.animusic.album.dao.Album;
import com.ilnitsk.animusic.album.dto.CreateAlbumDto;
import com.ilnitsk.animusic.album.repository.AlbumRepository;
import com.ilnitsk.animusic.anime.dao.Anime;
import com.ilnitsk.animusic.anime.repository.AnimeRepository;
import com.ilnitsk.animusic.soundtrack.dao.Soundtrack;
import com.ilnitsk.animusic.soundtrack.repository.SoundtrackRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AlbumRepositoryIntegrationTest {


    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SoundtrackRepository soundtrackRepository;

    @Autowired
    private AnimeRepository animeRepository;


    @Test
    public void testCreatePlaylistWithRelatedEntities() {
        Anime anime = new Anime("Naruto", "mock",  "", "");
        animeRepository.save(anime);
        CreateAlbumDto request = new CreateAlbumDto(
                anime.getId(),
                "My Playlist"
        );
        Album album = request.getAlbumData();
        album.setAnime(anime);
        Album createdAlbum = albumRepository.save(album);
        assertThat(createdAlbum).isNotNull();
        assertThat(createdAlbum.getAnime()).isNotNull();
        assertThat(albumRepository.getAlbumsByAnimeId(1)).isNotEmpty();
        assertThat(createdAlbum).isEqualTo(album);
        assertThat(createdAlbum.getSoundtracks()).isNull();
    }

    @Test
    public void testDeletePlaylistWithRelatedEntities() {
        Anime anime = new Anime("Naruto", "mock", "", "");
        animeRepository.save(anime);
        Album album = new Album();
        album.setAnime(anime);
        album.setName("My Playlist");
        Soundtrack soundtrack1 = Soundtrack.builder()
                .animeTitle("Song 1")
                .anime(anime)
                .build();
        soundtrack1.setAnime(anime);
        soundtrackRepository.save(soundtrack1);
        Soundtrack soundtrack2 = Soundtrack.builder()
                .animeTitle("Song 2")
                .anime(anime)
                .build();
        soundtrack2.setAnime(anime);
        soundtrackRepository.save(soundtrack2);
        album.addSoundtrack(soundtrack1);
        album.addSoundtrack(soundtrack2);
        albumRepository.save(album);
        Album createdAlbum = albumRepository.findById(album.getId()).get();
        assertThat(createdAlbum.getSoundtracks()).hasSize(2);
        albumRepository.deleteById(album.getId());
        Soundtrack afterDelete = soundtrackRepository.findById(1).get();
        assertThat(albumRepository.findById(1)).isEmpty();
        assertThat(afterDelete).isNotNull();
        assertThat(afterDelete.getAlbum()).isNull();
        assertThat(animeRepository.findById(1)).isNotNull();
    }
}

