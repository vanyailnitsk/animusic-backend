package com.animusic.api.dto;

import com.animusic.content.soundtrack.SoundtrackSavedHelper;
import com.animusic.core.db.model.Soundtrack;

public record SoundtrackEntityDto(
        Integer id,
        String originalTitle,
        String animeTitle,
        String audioFile,
        ImageDto image,
        Integer duration,
        boolean saved,
        AlbumItemDto album,
        AnimeItemDto anime
) {
    public static SoundtrackEntityDto fromSoundtrack(Soundtrack soundtrack) {
        return new SoundtrackEntityDto(
                soundtrack.getId(),
                soundtrack.getOriginalTitle(),
                soundtrack.getAnimeTitle(),
                soundtrack.getAudioFile(),
                ImageDto.fromImage(soundtrack.getImage()),
                soundtrack.getDuration(),
                SoundtrackSavedHelper.isSaved(soundtrack),
                AlbumItemDto.fromAlbum(soundtrack.getAlbum()),
                AnimeItemDto.fromAnime(soundtrack.getAnime())
        );
    }
}
