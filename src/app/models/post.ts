import { User } from "./user";
import { Image } from "./image";
import { Group } from "./group";

export class Post {
  id: number;
  content: string;
  creationDate: Date;
  user: User; 
  group : Group | null;
  images: Image[];


  constructor(id: number, content: string, creationDate: Date, user: User, group: Group | null , images: Image[]) {
    this.id = id;
    this.content = content;
    this.creationDate = creationDate;
    this.user = user;
    this.group = group;
    this.images = images;
  }
}
