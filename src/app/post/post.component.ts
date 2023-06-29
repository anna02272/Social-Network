import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { Post } from './post';
import { PostService } from '../service';
import { PostRefreshService } from '../service/postrefresh.service';



declare var $: any;
@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  posts: Post[] = [];
  post: Post = new Post(0, '',  new Date(), this.userService.currentUser);


  constructor(
    private userService: UserService,
    private postService: PostService,
    private postRefreshService: PostRefreshService
  ) {}

  ngOnInit() {
    this.loadPosts();
    this.subscribeToRefreshPosts();
    this.subscribeToOpenModal();
  }



  loadPosts() {
    this.postService.getAllPosts().subscribe((data: Post[]) => {
      this.posts = data;
    });
  }

  editPost(post: Post): void {
    this.postRefreshService.setPost({ ...post });
    this.openModal();
  }
  
  
  
  deletePost(post: Post): void {
    this.postService.deletePost(post.id).subscribe(() => {
      this.posts = this.posts.filter(p => p.id !== post.id);
    });
  }

  private subscribeToRefreshPosts() {
    this.postRefreshService.getRefreshObservable().subscribe(() => {
      this.loadPosts();
    });
  }

  private subscribeToOpenModal() {
    this.postRefreshService.getOpenModalObservable().subscribe(() => {
      this.openModal();
    });
  }
  

  openModal() {
    $('#createModal').modal('show');
  }


  hasSignedIn() {
    return !!this.userService.currentUser;
  }

  userName() {
    const user = this.userService.currentUser;
    return user ? user.username : '';
  }
}
