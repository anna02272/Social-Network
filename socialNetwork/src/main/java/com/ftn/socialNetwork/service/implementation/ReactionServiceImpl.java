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
  @Autowired
  public ReactionServiceImpl(ReactionRepository reactionRepository) {
    this.reactionRepository = reactionRepository;
  }


  @Override
  public Reaction create(Reaction reaction) {
    return reactionRepository.save(reaction);
  }


}
