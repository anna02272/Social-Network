package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.entity.Comment;
import com.ftn.socialNetwork.model.entity.Image;
import com.ftn.socialNetwork.model.entity.Post;
import com.ftn.socialNetwork.model.entity.User;
import com.ftn.socialNetwork.security.TokenUtils;
import com.ftn.socialNetwork.service.ImageService;
import com.ftn.socialNetwork.service.PostService;
import com.ftn.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {
  @Autowired
  private PostService postService;

  @Autowired
  private UserService userService;

  @Autowired
  private ImageService imageService;

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestBody Post post, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        post.setUser(user);
        post.setCreationDate(LocalDateTime.now());
        post.setIsDeleted(false);
        Post createdPost = postService.createPost(post);

        return ResponseEntity.ok(createdPost);
}
//  @PostMapping("/create")
//  public ResponseEntity<Post> createPost(@RequestBody Post post,
//                                         @RequestPart(required = false) MultipartFile[] images,
//                                       Principal principal) {
//        String username = principal.getName();
//        User user = userService.findByUsername(username);
//        post.setUser(user);
//        post.setCreationDate(LocalDateTime.now());
//        post.setIsDeleted(false);
//      List<Image> savedImages = new ArrayList<>();
//      if (images != null && images.length > 0) {
//        for (MultipartFile imageFile : images) {
//          Image image = new Image();
//          String imagePath = imageService.saveImage(imageFile);
//          image.setPath(imagePath);
//          image.setPost(post);
//          savedImages.add(image);
//        }
//      }
//       post.setImages(savedImages);
//        Post createdPost = postService.createPost(post);
//
//        return ResponseEntity.ok(createdPost);
//}



  @PutMapping("/update/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") Long postId, @RequestBody Post post) throws ChangeSetPersister.NotFoundException {
        Post existingPost = postService.findOneById(postId);

        if (existingPost == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (post.getContent() != null) {
            existingPost.setContent(post.getContent());
        }
        if (post.getCreationDate() != null){
            existingPost.setCreationDate(post.getCreationDate());
        }
        if (post.getUser() != null) {
            existingPost.setUser(post.getUser());
        }
        if (post.getImages()!= null){
            existingPost.setImages(post.getImages());
        }
      if (post.getIsDeleted()!= null){
        existingPost.setIsDeleted(post.getIsDeleted());
      }


        Post updatedPost = postService.updatePost(existingPost);

        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
//        postService.deletePost(id);
//        return ResponseEntity.noContent().build();
//    }
    @PutMapping("/delete/{id}")
    public ResponseEntity<Post> delete(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
      Post existing =postService.findOneById(id);

      if (existing == null) {
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
      }

      existing.setIsDeleted(true);
      Post updated = postService.updatePost(existing);

      return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        Post post = postService.findOneById(id);
        return ResponseEntity.ok(post);
    }

  @GetMapping("/all")
  public ResponseEntity<List<Post>> getAllPosts() {
    List<Post> posts = postService.findAllByIsDeleted(false);
    Collections.sort(posts, (post1, post2) -> post2.getCreationDate().compareTo(post1.getCreationDate()));

    return ResponseEntity.ok(posts);
  }
  @GetMapping("/ascendingAll")
  public ResponseEntity<List<Post>> getAllAscending(){
    List<Post> posts = postService.findAllByIsDeleted(false);
    Collections.sort(posts, (post1, post2) -> post1.getCreationDate().compareTo(post2.getCreationDate()));

    return ResponseEntity.ok(posts);
  }
  @GetMapping("/descendingAll")
  public ResponseEntity<List<Post>> getAllDescending(){
    List<Post> posts = postService.findAllByIsDeleted(false);
    Collections.sort(posts, (post1, post2) -> post2.getCreationDate().compareTo(post1.getCreationDate()));

    return ResponseEntity.ok(posts);
  }

}
