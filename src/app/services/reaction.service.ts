import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import { Reaction } from '../models/reaction';



@Injectable()
export class ReactionService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) {}
  
  
  reactToPost(postId: number, reaction: Reaction){
   const url = `${this.config.reaction_url}/reactToPost/${postId}`;
    return this.apiService.post(url, reaction);
  }
  findReactionByPostAndUser(postId : number, userId : number) {
    return this.apiService.get(this.config.reaction_url + "/find/post/" + postId +  "/user/"  + userId );
   }

   countReactionsByPost(postId : number) {
    return this.apiService.get(this.config.reaction_url + "/count/post/" + postId );
   }
   reactToComment(commentId: number, reaction: Reaction){
    const url = `${this.config.reaction_url}/reactToComment/${commentId}`;
     return this.apiService.post(url, reaction);
   }
   findReactionByCommentAndUser(commentId : number, userId : number) {
    return this.apiService.get(this.config.reaction_url + "/find/comment/" + commentId + "/user/" + userId );
   }

   countReactionsByComment(commentId : number) {
    return this.apiService.get(this.config.reaction_url + "/count/comment/" + commentId );
   }
    }

