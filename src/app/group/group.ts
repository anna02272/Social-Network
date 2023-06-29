import { User } from "../user";

export class GroupAdmin {
  id: number;
  groupId: number;
  userId: number;

  constructor(id: number, groupId: number, userId: number) {
    this.id = id;
    this.groupId = groupId;
    this.userId = userId;
  }
}

export class Group {
  id: number;
  name: string;
  description: string;
  creationDate: Date;
  isSuspended: boolean;
  suspendedReason: string;
  groupAdmin: string;

  constructor(
    id: number,
    name: string,
    description: string,
    creationDate: Date,
    isSuspended: boolean,
    suspendedReason: string,
    groupAdmin: string 
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.creationDate = creationDate;
    this.isSuspended = isSuspended;
    this.suspendedReason = suspendedReason;
    this.groupAdmin = groupAdmin; 
  }
}
