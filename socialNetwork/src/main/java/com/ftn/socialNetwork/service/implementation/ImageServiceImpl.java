package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.entity.Image;
import com.ftn.socialNetwork.repository.ImageRepository;
import com.ftn.socialNetwork.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

  public String saveImage(MultipartFile imageFile) {
    String fileName = UUID.randomUUID().toString();
    try {
      Files.copy(imageFile.getInputStream(), Paths.get("/c/Users/Desktop", fileName));
      return "/c/Users/Desktop/" + fileName;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}


