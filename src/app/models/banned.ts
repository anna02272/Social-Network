import { Group } from "./group";
import { GroupAdmin } from "./groupAdmin";
import { User } from "./user";

export class Banned {
    id: number;
    timeStamp: Date;
    isBlocked: boolean;
    groupAdmin: GroupAdmin | null;
    group: Group | null;
    user: User;
    bannedUser : User | null;
  
    constructor(
      id: number,
      timeStamp: Date,
      isBlocked: boolean,
      groupAdmin: GroupAdmin | null,
      group: Group | null,
      user: User,
      bannedUser : User | null
    ) {
      this.id = id;
      this.isBlocked = isBlocked;
      this.timeStamp = timeStamp;
      this.groupAdmin = groupAdmin;
      this.group = group;
      this.user = user;
      this.bannedUser = bannedUser;
    }
  }
  