import { User } from "./user";
import { Image } from "./image";

export class Post {
  id: number;
  content: string;
  creationDate: Date;
  user: User; 
  images: Image[];

  constructor(id: number, content: string, creationDate: Date, user: User,  images: Image[]) {
    this.id = id;
    this.content = content;
    this.creationDate = creationDate;
    this.user = user;
    this.images = images;
  }
}
