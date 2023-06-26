package com.ftn.socialNetwork.service;

import com.ftn.socialNetwork.model.entity.Image;
import com.ftn.socialNetwork.model.entity.User;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface ImageService {
    Image createImage(Image image);
    Image updateImage(Image image);
    Image deleteImage(Long id);
    Image findOneById(Long id) throws ChangeSetPersister.NotFoundException;
    Image findByPath(String path);

    Image findBypath(String path);

    List<Image> findAll();
}
