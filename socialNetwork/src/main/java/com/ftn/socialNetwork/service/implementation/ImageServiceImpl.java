package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.entity.Comment;
import com.ftn.socialNetwork.model.entity.Image;
import com.ftn.socialNetwork.model.entity.Post;
import com.ftn.socialNetwork.repository.ImageRepository;
import com.ftn.socialNetwork.service.ImageService;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FileUtils;


@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;


  public String saveImage(MultipartFile imageFile) {
    if (imageFile != null && !imageFile.isEmpty()) {
      try {
        String fileName = generateUniqueFileName(imageFile.getOriginalFilename());
        String imagePath = "assets\\post_images\\" + fileName;
        String saveImageToPath = "C:\\Users\\domon\\Desktop\\SR\\Serverske veb tehnologije\\ProjekatSVTKVT\\sr46-2021-svt_kvt2023-projekat\\socialNetwork_frontend\\src\\assets\\post_images\\"
          + fileName;
        File file = new File(saveImageToPath);
        FileUtils.writeByteArrayToFile(file, imageFile.getBytes());
        return imagePath;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  private String generateUniqueFileName(String originalFileName) {
    String fileExtension = StringUtils.getFilenameExtension(originalFileName);
    String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
    return uniqueFileName;
  }
  @Override
  public Image save(Image image) {

    return imageRepository.save(image);
  }
  @Override
  public Image delete(Long id) {
    imageRepository.deleteById(id);
    return null;
  }

}


