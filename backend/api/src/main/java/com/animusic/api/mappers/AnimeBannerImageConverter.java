package com.animusic.api.mappers;

import com.animusic.api.dto.AnimeBannerImageDto;
import com.animusic.core.db.model.AnimeBannerImage;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AnimeBannerImageConverter {
    private final ModelMapper modelMapper;

    public AnimeBannerImageConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        modelMapper.createTypeMap(AnimeBannerImage.class, AnimeBannerImageDto.class);
    }

    public AnimeBannerImageDto convertToDto(AnimeBannerImage bannerImage) {
        return modelMapper.map(bannerImage, AnimeBannerImageDto.class);
    }
}