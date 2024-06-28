package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.exceptionhandling.exception.NotFoundException;
import com.ftn.socialNetwork.indexservice.interfaces.GroupIndexingService;
import com.ftn.socialNetwork.indexservice.interfaces.PostIndexingService;
import com.ftn.socialNetwork.model.entity.Post;
import com.ftn.socialNetwork.repository.PostRepository;
import com.ftn.socialNetwork.service.intefraces.PostService;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostIndexingService postIndexService;
    private final GroupIndexingService groupIndexService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           @Qualifier("postIndexingServiceImpl") PostIndexingService postIndexService,
                           @Qualifier("groupIndexingServiceImpl") GroupIndexingService groupIndexService) {
      this.postRepository = postRepository;
      this.postIndexService = postIndexService;
      this.groupIndexService = groupIndexService;
    }

    @Override
    public Post createPost(Post post, MultipartFile pdfFile) {
        Post createdPost = postRepository.save(post);
        postIndexService.indexPost(pdfFile, createdPost);
        if (post.getGroup() != null) {
            var groupId = post.getGroup().getId().toString();
            groupIndexService.updatePostCount(groupId);
        }
        return createdPost;
    }

    @Override
    public Post updatePost(Post post, MultipartFile pdfFile) {
        Post updatedPost = postRepository.save(post);
        if (pdfFile != null && !pdfFile.isEmpty()) {
            postIndexService.indexPost(pdfFile, updatedPost);
        } else {
            postIndexService.updatePostIndex(updatedPost);
        }
        return updatedPost;
    }

    @Override
    public Post deletePost(Post post) {
        Post postToDelete = postRepository.findById(post.getId())
                .orElseThrow(() -> new NotFoundException("Post not found"));
        postRepository.save(postToDelete);
        postIndexService.deletePostIndex(postToDelete);
        if (post.getGroup() != null) {
            var groupId = post.getGroup().getId().toString();
            groupIndexService.deletePostCount(groupId);
        }

        return postToDelete;
    }

    @Override
    public Post findOneById(Long id) throws ChangeSetPersister.NotFoundException {
        return postRepository.findById(id)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }
  @Override
  public List<Post> findAllByGroupId(Long groupId) {
    return postRepository.findAllByGroupIdAndIsDeleted(groupId, false);
  }

  @Autowired
  private EntityManager entityManager;

  public List<Post> findAllByIsDeleted(boolean isDeleted) {
    Session session = entityManager.unwrap(Session.class);
    Filter filter = session.enableFilter("deletedPostFilter");
    filter.setParameter("isDeleted", isDeleted);
    List<Post> posts = postRepository.findAll();
    session.disableFilter("deletedPostFilter");
    return posts;
  }

    @Override
    public long countAllByGroupId(Long groupId) {
        return postRepository.countAllByGroupIdAndIsDeleted(groupId, false);
    }

}
