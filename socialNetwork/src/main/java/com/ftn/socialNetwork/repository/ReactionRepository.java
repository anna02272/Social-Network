package com.ftn.socialNetwork.repository;

import com.ftn.socialNetwork.model.entity.Comment;
import com.ftn.socialNetwork.model.entity.Post;
import com.ftn.socialNetwork.model.entity.Reaction;
import com.ftn.socialNetwork.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

  Reaction findByPostAndUser(Post post, User user);

  Reaction findByCommentAndUser(Comment comment, User user);

    List<Reaction> findByPost(Post post);
}
