package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.entity.Comment;
import com.ftn.socialNetwork.model.entity.FriendRequest;
import com.ftn.socialNetwork.model.entity.Post;
import com.ftn.socialNetwork.repository.PostRepository;
import com.ftn.socialNetwork.service.PostService;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post deletePost(Long id) {
        postRepository.deleteById(id);
        return null;
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
}
