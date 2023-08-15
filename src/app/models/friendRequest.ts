import { User } from "./user";

export class FriendRequest {
    id: number;
    approved: boolean ;
    created_at: Date;
    at: Date | null;
    fromUser: User;
    forUser: User;

  constructor(id: number,  approved: boolean ,created_at: Date, at: Date | null, fromUser: User, forUser: User
  ) { 
    this.id = id;
    this.approved = approved;
    this.created_at = created_at;
    this.at = at;
    this.fromUser = fromUser;
    this.forUser = forUser;

}
}