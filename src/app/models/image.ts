import { User } from "./user";
import { Post } from "./post";

export class Image {
    id: number;
    path: string;
    user: User;
    post: Post;
    
    constructor(id: number, path: string, user: User, post: Post) {
      this.id = id;
      this.path = path;
      this.user = user;
      this.post = post;
    }
  }
  