package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.model.entity.Image;
import com.ftn.socialNetwork.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/images")
public class ImageController {

  public String saveImage(MultipartFile image) {
    if (image != null && !image.isEmpty()) {
      try {
        // Generate a unique filename for the image
        String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();

        // Specify the directory where you want to save the images
        String uploadDir = "path/to/your/upload/directory/";

        // Create the directory if it doesn't exist
        File dir = new File(uploadDir);
        if (!dir.exists()) {
          dir.mkdirs();
        }

        // Save the image file to the specified directory
        String imagePath = uploadDir + filename;
        Path filePath = Paths.get(imagePath);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the path or URL of the saved image
        return imagePath;
      } catch (IOException e) {
        // Handle the exception according to your application's error handling mechanism
        e.printStackTrace();
      }
    }

    return null;
  }

}
