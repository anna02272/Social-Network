import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { ConfigService } from "./config.service";
import { FriendRequest } from "../models/friendRequest";

@Injectable()
export class FriendRequestService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) {
  }
  create(userId: number, friendRequest: FriendRequest){
    const url = `${this.config.friendRequest_url}/create/${userId}`;
    return this.apiService.post(url, friendRequest);
   }

  approve(id: number) {
    return this.apiService.put(this.config.friendRequest_url + '/approve/' + id);
    }

    decline(id: number) {
    return this.apiService.put(this.config.friendRequest_url + '/decline/' + id);
    }
  
  getAll() {
    return this.apiService.get(this.config.friendRequest_url + "/all");
   }
   getByForUser(forUserId: number) {
    return this.apiService.get(`${this.config.friendRequest_url}/all/${forUserId}`);
  }

   getById(id : number) {
    return this.apiService.get(this.config.friendRequest_url + "/find/" + id );
   }
  
   getApprovedFriendsForUser(userId: number) {
    const url = `${this.config.friendRequest_url}/friends/${userId}`;
    return this.apiService.get(url);
  }
  searchUsers(keyword: string) {
    const url = `${this.config.friendRequest_url}/search?keyword=${keyword}`;
    return this.apiService.get(url);
  }
}