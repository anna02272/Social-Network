package com.ftn.socialNetwork.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.socialNetwork.model.entity.*;
import com.ftn.socialNetwork.service.intefraces.GroupService;
import com.ftn.socialNetwork.service.intefraces.ImageService;
import com.ftn.socialNetwork.service.intefraces.PostService;
import com.ftn.socialNetwork.service.intefraces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("api/posts")
public class PostController {
  @Autowired
  private PostService postService;

  @Autowired
  private UserService userService;

  @Autowired
  private GroupService groupService;

  @Autowired
  private ImageService imageService;

//  @PostMapping("/create")
//  public ResponseEntity<Post> createPost(@RequestBody Post post, Principal principal) {
//    String username = principal.getName();
//    User user = userService.findByUsername(username);
//    post.setUser(user);
//    post.setCreationDate(LocalDateTime.now());
//    post.setIsDeleted(false);
//    Post createdPost = postService.createPost(post);
//
//    return ResponseEntity.ok(createdPost);
//  }

//  @PostMapping("/create/{id}")
//  public ResponseEntity<Post> createGroupPost(@PathVariable("id") Long groupId, @RequestBody Post post, Principal principal) throws ChangeSetPersister.NotFoundException {
//    String username = principal.getName();
//    User user = userService.findByUsername(username);
//    Group group = groupService.findOneById(groupId);
//    post.setUser(user);
//    post.setCreationDate(LocalDateTime.now());
//    post.setIsDeleted(false);
//    post.setGroup(group);
//    Post createdPost = postService.createPost(post);
//
//    return ResponseEntity.ok(createdPost);
//  }

  @PostMapping("/create")
  public ResponseEntity<Post> createPost(@RequestPart("post") String postJson,
                                         @RequestPart(name = "images", required = false) List<MultipartFile> imageFiles,
                                         @RequestPart(value = "pdfFile") MultipartFile pdfFile,
                                         Principal principal) throws JsonProcessingException {

    Post post = new ObjectMapper().readValue(postJson, Post.class);

    String username = principal.getName();
    User user = userService.findByUsername(username);

    post.setUser(user);
    post.setCreationDate(LocalDateTime.now());
    post.setIsDeleted(false);

    return getPostResponseEntity(imageFiles, post, pdfFile);
  }
  @PostMapping("/create/{id}")
  public ResponseEntity<Post> createGroupPost(@PathVariable("id") Long groupId,
                                              @RequestPart("post") String postJson,
                                              @RequestPart(name = "images", required = false) List<MultipartFile> imageFiles,
                                              @RequestPart(value = "pdfFile") MultipartFile pdfFile,
                                              Principal principal) throws ChangeSetPersister.NotFoundException, JsonProcessingException {

    Post post = new ObjectMapper().readValue(postJson, Post.class);

    String username = principal.getName();
    User user = userService.findByUsername(username);
    Group group = groupService.findOneById(groupId);
    post.setUser(user);
    post.setCreationDate(LocalDateTime.now());
    post.setIsDeleted(false);
    post.setGroup(group);

    return getPostResponseEntity(imageFiles, post, pdfFile);
  }

  private ResponseEntity<Post> getPostResponseEntity(@RequestPart(name = "images", required = false) List<MultipartFile> imageFiles,
                                                     Post post,
                                                     MultipartFile pdfFile ) {
    Post createdPost = postService.createPost(post, pdfFile);
    if (imageFiles != null && !imageFiles.isEmpty()) {
      List<Image> images = new ArrayList<>();
      for (MultipartFile imageFile : imageFiles) {
        String imagePath = imageService.saveImage(imageFile);
        Image image = new Image();
        image.setPath(imagePath);
        imageService.save(image);

        image.setPost(createdPost);
        images.add(image);
      }
      createdPost.setImages(images);
    }
    Post updatedPost = postService.updatePost(createdPost, pdfFile);

    return ResponseEntity.ok(updatedPost);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<Post> updatePost(
    @PathVariable("id") Long postId,
    @RequestPart("post") String postJson,
    @RequestPart(name = "images", required = false) List<MultipartFile> imageFiles,
    @RequestPart(value = "pdfFile", required = false) MultipartFile pdfFile) throws ChangeSetPersister.NotFoundException, JsonProcessingException {

    Post post = new ObjectMapper().readValue(postJson, Post.class);

    Post existingPost = postService.findOneById(postId);

    if (existingPost == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    if (imageFiles != null && !imageFiles.isEmpty()) {
      List<Image> images = new ArrayList<>();
      for (MultipartFile imageFile : imageFiles) {
        String imagePath = imageService.saveImage(imageFile);
        Image image = new Image();
        image.setPath(imagePath);
        imageService.save(image);

        image.setPost(existingPost);
        images.add(image);
      }
      existingPost.setImages(images);
    }

    existingPost.setTitle(post.getTitle());
    existingPost.setContent(post.getContent());
    Post updatedPost = postService.updatePost(existingPost, pdfFile);

    return new ResponseEntity<>(updatedPost, HttpStatus.OK);
  }

//  @PutMapping("/update/{id}")
//    public ResponseEntity<Post> updatePost(@PathVariable("id") Long postId, @RequestBody Post post) throws ChangeSetPersister.NotFoundException {
//        Post existingPost = postService.findOneById(postId);
//
//        if (existingPost == null) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//
//        if (post.getContent() != null) {
//            existingPost.setContent(post.getContent());
//        }
//        if (post.getCreationDate() != null){
//            existingPost.setCreationDate(post.getCreationDate());
//        }
//        if (post.getUser() != null) {
//            existingPost.setUser(post.getUser());
//        }
//      if (post.getIsDeleted()!= null){
//        existingPost.setIsDeleted(post.getIsDeleted());
//      }
//
//
//        Post updatedPost = postService.updatePost(existingPost);
//
//        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageService.delete(id);
        return ResponseEntity.noContent().build();
    }

  @PutMapping("/delete/{id}")
    public ResponseEntity<Post> delete(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
      Post existing =postService.findOneById(id);

      if (existing == null) {
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
      }

      existing.setIsDeleted(true);
      Post updated = postService.deletePost(existing);

      return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        Post post = postService.findOneById(id);
        return ResponseEntity.ok(post);
    }
  @GetMapping("/all/{groupId}")
  public ResponseEntity<List<Post>> getByGroup(@PathVariable Long groupId) {
    List<Post> posts = postService.findAllByGroupId(groupId);
    return ResponseEntity.ok(posts);
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
  @GetMapping("/allAsc/{groupId}")
  public ResponseEntity<List<Post>> getByGroupAscending(@PathVariable Long groupId) {
    List<Post> posts = postService.findAllByGroupId(groupId);
    Collections.sort(posts, (post1, post2) -> post1.getCreationDate().compareTo(post2.getCreationDate()));

    return ResponseEntity.ok(posts);
  }
  @GetMapping("/allDesc/{groupId}")
  public ResponseEntity<List<Post>> getByGroupDescending(@PathVariable Long groupId) {
    List<Post> posts = postService.findAllByGroupId(groupId);
    Collections.sort(posts, (post1, post2) -> post2.getCreationDate().compareTo(post1.getCreationDate()));

    return ResponseEntity.ok(posts);
  }

}
