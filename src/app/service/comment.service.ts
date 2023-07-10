import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import { Comment } from '../comment/comment';


@Injectable()
export class CommentService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) {
  }
    create(postId: number, comment: Comment){
      const url = `${this.config.comment_url}/create/${postId}`;
       return this.apiService.post(url, comment);
     }
  
    delete(id: number) {
    return this.apiService.put(this.config.comment_url + '/delete/' + id);
  }
    
  update(commentId: number, comment: Comment) {
    const url = `${this.config.comment_url}/update/${commentId}`;
    return this.apiService.put(url, comment);
  }
  
    getAll() {
      return this.apiService.get(this.config.comment_url + "/all");
     }
     getById(id : number) {
      return this.apiService.get(this.config.comment_url + "/find/" + id );
     }

     getCommentsByPostId(postId : number) {
      return this.apiService.get(this.config.comment_url + "/findByPost/" + postId );
     }
    
  
  }

