package com.ftn.socialNetwork.service.intefraces;

import com.ftn.socialNetwork.model.entity.*;

import java.util.Map;

public interface ReactionService {

  Reaction create(Reaction reaction);

  Reaction findReactionByPostAndUser(Post post, User user);

  Reaction findReactionByCommentAndUser(Comment comment, User user);

  Map<EReactionType, Integer> countReactionsByPost(Post post);

  Map<EReactionType, Integer> countReactionsByComment(Comment comment);
}
