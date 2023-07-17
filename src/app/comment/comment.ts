import { User } from "../user";

export class Comment {
  id: number;
  text: string;
  timeStamp: Date;
  isDeleted: boolean;
  user: User;
  parentComment: Comment | null;
  childrenComments: Comment[] = [];
  ;

  constructor(
    id: number,
    text: string,
    timeStamp: Date,
    isDeleted: boolean,
    user: User,
    parentComment: Comment | null = null
  ) {
    this.id = id;
    this.text = text;
    this.timeStamp = timeStamp;
    this.isDeleted = isDeleted;
    this.user = user;
    this.parentComment = parentComment;
   
  }
}
