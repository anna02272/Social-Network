package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.entity.*;
import com.ftn.socialNetwork.repository.ReactionRepository;
import com.ftn.socialNetwork.repository.PostRepository;
import com.ftn.socialNetwork.repository.CommentRepository;
import com.ftn.socialNetwork.repository.UserRepository;
import com.ftn.socialNetwork.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReactionServiceImpl implements ReactionService {

  @Autowired
  private ReactionRepository reactionRepository;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CommentRepository commentRepository;

  @Override
  public Reaction create(Reaction reaction) {
    return reactionRepository.save(reaction);
  }

  @Override
  public Reaction findReactionByPostAndUser(Post post, User user) {
    return reactionRepository.findByPostAndUser(post, user);
  }

  @Override
  public Reaction findReactionByCommentAndUser(Comment comment, User user) {
    return reactionRepository.findByCommentAndUser(comment, user);
  }

  @Override
  public Map<EReactionType, Integer> countReactionsByPost(Post post) {
    List<Reaction> reactions = reactionRepository.findByPost(post);

    Map<EReactionType, Integer> reactionCounts = new HashMap<>();
    for (Reaction reaction : reactions) {
      EReactionType reactionType = reaction.getType();
      reactionCounts.put(reactionType, reactionCounts.getOrDefault(reactionType, 0) + 1);
    }

    return reactionCounts;
  }



}
