package com.animusic.playlist.dao;

import com.animusic.soundtrack.dao.Soundtrack;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "playlist_soundtrack")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlaylistSoundtrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    @JsonBackReference
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "soundtrack_id")
    private Soundtrack soundtrack;

    @Column(name = "added_at")
    private Date addedAt;
}
