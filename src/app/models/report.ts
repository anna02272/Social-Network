import { User } from "./user";
import { Post } from "./post";
import { EReportReason } from "./eReportReason";
import { Comment } from "./comment";

export class Report {
  id: number;
  reason: EReportReason;
  timestamp: Date;
  accepted: boolean;
  isDeleted: boolean;
  user: User  | undefined;
  post: Post | undefined;
  comment: Comment | undefined;
  reportedUser: User | undefined; 

  constructor(
    id: number,
    reason: EReportReason,
    timestamp: Date,
    accepted: boolean,
    isDeleted: boolean,
    user: User,
    post: Post,
    comment: Comment,
    reportedUser: User
  ) {
    this.id = id;
    this.reason = reason;
    this.timestamp = timestamp;
    this.accepted = accepted;
    this.isDeleted = isDeleted;
    this.user = user;
    this.post = post;
    this.comment = comment;
    this.reportedUser = reportedUser;
  }
}
