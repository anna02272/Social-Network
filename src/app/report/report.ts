import { User } from "../user";
import { Post } from "../post/post";
import { Comment } from "@angular/compiler";
import { EReportReason } from "./eReportReason";

export class Report {
  id: number;
  reason: EReportReason;
  timestamp: Date;
  accepted: boolean;
  user: User;
  post: Post;
  comment: Comment;

  constructor(
    id: number,
    reason: EReportReason,
    timestamp: Date,
    accepted: boolean,
    user: User,
    post: Post,
    comment: Comment
  ) {
    this.id = id;
    this.reason = reason;
    this.timestamp = timestamp;
    this.accepted = accepted;
    this.user = user;
    this.post = post;
    this.comment = comment;
  }
}
