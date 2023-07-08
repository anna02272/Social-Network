import { User } from "../user";
import { Post } from "../post/post";
import { Comment } from "../comment/comment";
import { EReactionType } from "./eReactionType";

export class Reaction {
  id: number;
  type: EReactionType;
  timeStamp: Date;
  user: User;
  post: Post;
  comment: Comment;

  constructor(
    id: number,
    type: EReactionType,
    timeStamp: Date,
    user: User,
    post: Post,
    comment: Comment
  ) {
    this.id = id;
    this.type = type;
    this.timeStamp = timeStamp;
    this.user = user;
    this.post = post;
    this.comment = comment;
  }
}
