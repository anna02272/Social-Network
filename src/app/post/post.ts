import { User } from "../user";

export class Post {
  id: number;
  content: string;
  creationDate: Date;
  user: User; 
  
  expandedComments: boolean = false;

  constructor(id: number, content: string, creationDate: Date, user: User) {
    this.id = id;
    this.content = content;
    this.creationDate = creationDate;
    this.user = user;
  }
}
