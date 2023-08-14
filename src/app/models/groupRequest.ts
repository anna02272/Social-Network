import { Group } from "./group";
import { User } from "./user";

export class GroupRequest {
    id: number;
    approved: boolean ;
    created_at: Date;
    at: Date | null;
    user: User;
    group: Group;

  constructor(id: number,  approved: boolean ,created_at: Date, at: Date | null, user: User, group: Group
  ) { 
    this.id = id;
    this.approved = approved;
    this.created_at = created_at;
    this.at = at;
    this.user = user;
    this.group = group;

}

}
  