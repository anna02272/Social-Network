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

  deletePost(id: number) {
  return this.apiService.delete(this.config.post_url + '/delete/' + id);
}
  
updatePost(postId: number, post: Post) {
  const url = `${this.config.post_url}/update/${postId}`;
  return this.apiService.put(url, post);
}

  getAllPosts() {
    return this.apiService.get(this.config.post_url + "/all");
   }
   getPostById(id : number) {
    return this.apiService.get(this.config.post_url + "/find/" + id );
   }
  


}
