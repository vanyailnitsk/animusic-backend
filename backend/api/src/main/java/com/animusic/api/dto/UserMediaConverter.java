package com.animusic.api.dto;

import com.animusic.core.db.model.Playlist;
import com.animusic.core.db.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMediaConverter {
    private final ModelMapper modelMapper;

    public UserMediaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        modelMapper.createTypeMap(Playlist.class, PlaylistDto.class)
                .addMapping(Playlist::getName, PlaylistDto::setName)
                .addMapping(Playlist::getSoundtracks, PlaylistDto::setSoundtracks)
                .addMapping(Playlist::getUser, PlaylistDto::setAddedBy);

        modelMapper.createTypeMap(User.class, PlaylistOwnerDto.class)
                .addMapping(User::getName, PlaylistOwnerDto::setName);
    }

    public PlaylistDto convertToDto(Playlist playlist) {
        return modelMapper.map(playlist, PlaylistDto.class);
    }

}