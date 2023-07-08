import { User } from "../user";
import { Reaction } from "../reaction/reaction";
import { Report } from "../report/report";
import { Post } from "../post/post";

export class Comment {
  id: number;
  text: string;
  timeStamp: Date;
  isDeleted: boolean;
  user: User;
  replies: Comment[];
  //  parentComment: Comment ;
  posts: Post[];
  reactions: Reaction[];
  report: Report[];

  constructor(
    id: number,
    text: string,
    timeStamp: Date,
    isDeleted: boolean,
    user: User,
    replies: Comment[],
    // parentComment: Comment,
    posts: Post[],
    reactions: Reaction[],
    report: Report[]
  ) {
    this.id = id;
    this.text = text;
    this.timeStamp = timeStamp;
    this.isDeleted = isDeleted;
    this.user = user;
    this.replies = replies;
    // this.parentComment = parentComment;
    this.posts = posts;
    this.reactions = reactions;
    this.report = report;
  }
}
