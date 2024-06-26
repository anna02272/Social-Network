package com.ftn.socialNetwork.service.intefraces;

import com.ftn.socialNetwork.model.entity.Comment;
import com.ftn.socialNetwork.model.entity.Image;
import com.ftn.socialNetwork.model.entity.User;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
   String saveImage(MultipartFile imageFile);
   Image save(Image image);
    Image delete(Long id) ;

}
