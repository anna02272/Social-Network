package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.entity.Image;
import com.ftn.socialNetwork.model.entity.User;
import com.ftn.socialNetwork.repository.ImageRepository;
import com.ftn.socialNetwork.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image createImage(Image image) {
        try {
            return imageRepository.save(image);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }



    @Override
    public Image updateImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Image deleteImage(Long id) {
        imageRepository.deleteById(id);
        return null;
    }

    @Override
    public Image findOneById(Long id) throws ChangeSetPersister.NotFoundException {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }

    @Override
    public Image findByPath(String path) {
        Optional<Image> image = imageRepository.findFirstByPath(path);
        if (!image.isEmpty()) {
            return image.get();
        }
        return null;
    }


    @Override
    public Image findBypath(String path) {
        Optional<Image> image = imageRepository.findFirstByPath(path);
        if (!image.isEmpty()) {
            return image.get();
        }
        return null;
    }
    @Override
    public List<Image> findAll() {
        return imageRepository.findAll();
    }
}


