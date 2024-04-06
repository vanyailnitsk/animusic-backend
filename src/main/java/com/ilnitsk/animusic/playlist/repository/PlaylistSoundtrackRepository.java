package com.ilnitsk.animusic.playlist.repository;

import com.ilnitsk.animusic.playlist.dao.PlaylistSoundtrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PlaylistSoundtrackRepository extends JpaRepository<PlaylistSoundtrack,Long> {
    @Query("SELECT COUNT(s) > 0 FROM PlaylistSoundtrack s where s.playlist.id= :playlist_id and s.soundtrack.id= :soundtrack_id")
    boolean playlistAlreadyContainsSoundtrack(
            @Param("playlist_id") Integer playlist_id, @Param("soundtrack_id") Integer soundtrack_id);

    @Modifying
    @Transactional
    @Query("DELETE FROM PlaylistSoundtrack s where s.playlist.id = :playlist_id and s.soundtrack.id = :soundtrack_id")
    void deleteTrackFromPlaylist(
            @Param("playlist_id") Integer playlist_id, @Param("soundtrack_id") Integer soundtrack_id);
}
