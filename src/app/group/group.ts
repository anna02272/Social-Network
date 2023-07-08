import { GroupAdmin } from "./groupAdmin";
export class Group {
  id: number;
  name: string;
  description: string;
  creationDate: Date;
  isPublic: boolean;
  imageUrl: string;
  groupAdmin: GroupAdmin; 

  constructor(id: number, name: string, description: string, creationDate: Date, isPublic: boolean, imageUrl: string, groupAdmin: GroupAdmin) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.creationDate = creationDate;
    this.isPublic = isPublic;
    this.imageUrl = imageUrl;
    this.groupAdmin = groupAdmin; 
  }
}
