import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import { Reaction } from '../reaction/reaction';
import { Observable } from 'rxjs';


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
    return this.apiService.get(this.config.reaction_url + "/find/" + postId + "/" + userId );
   }

   countReactionsByPost(postId : number) {
    return this.apiService.get(this.config.reaction_url + "/count/" + postId );
   }
   
  
    }

