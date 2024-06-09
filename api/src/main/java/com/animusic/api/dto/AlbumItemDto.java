package com.animusic.api.dto;

import com.animusic.album.dao.Album;

public record AlbumItemDto(
        Integer id,
        String name,
        CoverArtDto coverArt
) {
    public static AlbumItemDto fromAlbum(Album album) {
        return new AlbumItemDto(
                album.getId(),
                album.getName(),
                CoverArtDto.fromCoverArt(album.getCoverArt())
        );
    }
}
