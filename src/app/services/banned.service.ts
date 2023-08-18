import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { ConfigService } from "./config.service";
import { Banned } from "../models/banned";

@Injectable()
export class BannedService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) {
  }

    blockUser(userId: number, banned: Banned){
      return this.apiService.post(this.config.banned_url + `/blockUser/${userId}`, banned);
    }
  
    unblockUser(id: number) {
      return this.apiService.put(this.config.banned_url + `/unblockUser/${id}`);
    }
    
    getAllBlockedUsers() {
      return this.apiService.get(this.config.banned_url + "/allBlockedUsers");
     }
    
  }

