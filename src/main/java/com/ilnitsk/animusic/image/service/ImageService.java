package com.ilnitsk.animusic.image.service;

import com.ilnitsk.animusic.image.dao.Image;
import com.ilnitsk.animusic.image.repository.ImageRepository;
import com.ilnitsk.animusic.s3.S3Service;
import com.ilnitsk.animusic.soundtrack.dao.Soundtrack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final S3Service s3Service;
    private static final String ANIME_IMAGES_PATH = "%s/images/%s";
    private static final String USER_IMAGES_PATH = "users/%d/%s";
    private final String CONTENT_TYPE = "image/jpeg";

    public void createSoundtrackImage(Soundtrack soundtrack, MultipartFile image) {
        Image imageEntity = new Image();
        String fileName = ANIME_IMAGES_PATH.formatted(soundtrack.getAnime().getFolderName(), soundtrack.getAnimeTitle());
        String blobKey = s3Service.createBlob(fileName, image,CONTENT_TYPE);
        imageEntity.setSource(blobKey);
        imageRepository.save(imageEntity);
        soundtrack.setImage(imageEntity);
    }

    public void deleteImage(Image image) {
        s3Service.deleteObject(image.getSource());
        imageRepository.delete(image);
    }

    @Transactional
    public Image createAnimeImage(String animeName, String imageName, MultipartFile image) {
        Image imageEntity = new Image();
        String fileName = ANIME_IMAGES_PATH.formatted(animeName, imageName);
        String blobKey = s3Service.createBlob(fileName, image, CONTENT_TYPE);
        imageEntity.setSource(blobKey);
        return imageRepository.save(imageEntity);
    }

    public Image createImageForUser(Integer userId,String imageName,MultipartFile image) {
        Image imageEntity = new Image();
        String fileName = USER_IMAGES_PATH.formatted(userId,imageName);
        String blobKey = s3Service.createBlob(fileName, image, CONTENT_TYPE);
        imageEntity.setSource(blobKey);
        return imageRepository.save(imageEntity);
    }

    public Image getImage(Integer id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

}
