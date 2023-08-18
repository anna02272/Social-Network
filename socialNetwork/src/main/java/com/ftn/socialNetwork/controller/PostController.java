package com.ftn.socialNetwork.controller;
import com.ftn.socialNetwork.model.entity.*;
import com.ftn.socialNetwork.service.GroupService;
import com.ftn.socialNetwork.service.ImageService;
import com.ftn.socialNetwork.service.PostService;
import com.ftn.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/posts")
public class PostController {
  @Autowired
  private PostService postService;

  @Autowired
  private UserService userService;

  @Autowired
  private GroupService groupService;

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
  @PostMapping("/create/{id}")
  public ResponseEntity<Post> createGroupPost(@PathVariable("id") Long groupId, @RequestBody Post post, Principal principal) throws ChangeSetPersister.NotFoundException {
    String username = principal.getName();
    User user = userService.findByUsername(username);
    Group group = groupService.findOneById(groupId);
    post.setUser(user);
    post.setCreationDate(LocalDateTime.now());
    post.setIsDeleted(false);
    post.setGroup(group);
    Post createdPost = postService.createPost(post);

    return ResponseEntity.ok(createdPost);
  }

//  @PostMapping(value = {"/create"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//public ResponseEntity<Post> createPost(@RequestPart("post") Post post,
//                                       @RequestPart(name = "imageFile", required = false) MultipartFile[] file,
//                                       Principal principal) {
//  String username = principal.getName();
//  User user = userService.findByUsername(username);
//  post.setUser(user);
//  post.setCreationDate(LocalDateTime.now());
//  post.setIsDeleted(false);
//      try {
//        Set<Image> images = uploadImage(file);
//        post.setImages(images);
//      } catch (Exception e) {
//      }
//  Post createdPost = postService.createPost(post);
//  return ResponseEntity.ok(createdPost);
//}


  public Set<Image> uploadImage(MultipartFile[] multipartFiles) throws IOException {
    Set<Image> images = new HashSet<>();
    for (MultipartFile file : multipartFiles) {
      Image image = new Image(file.getOriginalFilename(), file.getContentType(), file.getBytes());
      images.add(image);
    }
    return images;
  }

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
