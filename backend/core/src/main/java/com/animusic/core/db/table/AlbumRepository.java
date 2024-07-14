package com.animusic.core.db.table;

import java.util.List;
import java.util.Optional;

import com.animusic.core.db.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlbumRepository extends JpaRepository<Album, Integer> {
    Optional<List<Album>> getAlbumsByAnimeId(Integer animeId);

    @Query("SELECT COUNT(p) > 0 FROM Album p WHERE p.name = :albumName AND p.anime.id = :animeId")
    boolean existsByNameAndAnimeId(@Param("albumName") String albumName, @Param("animeId") Integer animeId);

}
