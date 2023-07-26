import { User } from "./user";
import { Group } from "./group";
export class GroupAdmin {
    id: number;
    group: Group;
    user: User;
  
    constructor(id: number, group: Group, user: User) {
      this.id = id;
      this.group = group;
      this.user = user;
    }
  }
  