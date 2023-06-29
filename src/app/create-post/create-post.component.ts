import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { UserService } from '../service/user.service';
import { Post } from '../post/post';
import { PostService } from '../service';
import { PostRefreshService } from '../service/postrefresh.service';


@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit {
  post!: Post;

  @ViewChild('createModal') createModal!: ElementRef;

  constructor(
    private userService: UserService,
    private postService: PostService,
    public postRefreshService: PostRefreshService
  ) {}

  ngOnInit() {
    this.userService.getMyInfo().subscribe(user => {
      this.post = new Post(0, '', new Date(), user);
    });
  
    const selectedPost = this.postRefreshService.getSelectedPost();
    if (selectedPost) {
      this.post = { ...selectedPost };
      this.openModal();
    }
  }
  
  
  
  onSubmit() {
    if (this.post.id) {
      this.updatePost();
    } else {
      this.createPost();
    }
    
  }

  createPost() {
    this.postService.createPost(this.post).subscribe(() => {
      this.postRefreshService.refreshPosts();
      this.post.content = '';
      this.closeModal();
    });
  }

  updatePost() {
    this.postService.updatePost(this.post.id, this.post).subscribe(() => {
      this.postRefreshService.refreshPosts();
      this.post.content = '';
   
      this.closeModal();
    });
  }

  onModalHidden() {
    this.userService.getMyInfo().subscribe(user => {
      this.post = new Post(0, '', new Date(), user); 
      });
    const postForm = this.createModal.nativeElement.querySelector('form');
    postForm.reset();
  }
  
  closeModal() {
    this.createModal.nativeElement.dismiss();
  }

  openModal() {
    this.createModal.nativeElement.show();
  }
  

  hasSignedIn() {
    return !!this.userService.currentUser;
  }

  userName() {
    const user = this.userService.currentUser;
    return user ? user.username : '';
  }
}
