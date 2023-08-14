import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Post } from '../../models/post';
import { PostService } from '../../services';
import { PostRefreshService } from '../../services/postrefresh.service';
import { HttpClient } from '@angular/common/http';
import { Image } from 'src/app/models/image';


@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit {
  post!: Post;
  selectedPost!: Post;
  selectedFile!: File;
  selectedFileName!: string;
  image! : Image[];


  @ViewChild('createModal') createModal!: ElementRef;

  constructor(
    private userService: UserService,
    private postService: PostService,
    public postRefreshService: PostRefreshService,
    private http: HttpClient
  ) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
    this.selectedFileName = this.selectedFile ? this.selectedFile.name : '';
  }


  ngOnInit() {
   this.userService.getMyInfo().subscribe(user => {
      this.post = new Post(0, '', new Date(), user, null, this.image);
      
    });
    this.postRefreshService.selectedPost$.subscribe((value) => {
      this.selectedPost = value;
    });

  }
  
  onSubmit() {
    if (this.selectedPost.id) {
      this.updatePost();
    } else {
      this.createPost();
    }
    
  }
  combinedMethod() {
    this.onSubmit();
  }
  


  createPost() {
    this.postService.createPost(this.post).subscribe(() => {
      this.postRefreshService.refreshPosts();
      this.post.content = '';
      this.closeModal();
    });
  }

  updatePost() {
    this.postService.updatePost(this.selectedPost.id, this.selectedPost).subscribe(() => {
      this.postRefreshService.refreshPosts();
      this.post.content = '';
    });
  }
  

  onModalHidden() {
    this.userService.getMyInfo().subscribe(user => {
      this.selectedPost = new Post(0, '', new Date(), user, null, this.image); 
      });
  }
  
  closeModal() {
    this.createModal.nativeElement.hide();
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
