import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import { Post } from '../post/post';

@Injectable()
export class PostService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService
    
  ) {
  }

  createPost(post: Post){
    return this.apiService.post(this.config.post_url + "/create", post);
  }


  getAllPosts() {
    return this.apiService.get(this.config.post_url + "/all");
  }



}
