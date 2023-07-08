import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { UserService } from '../service/user.service';
import { Post } from '../post/post';
import { PostService } from '../service';
import { PostRefreshService } from '../service/postrefresh.service';
import { HttpClient } from '@angular/common/http';


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
      this.post = new Post(0, '', new Date(), user);
      
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
    // this.uploadImage();
  }
  
  uploadImage() {
    const uploadData = new FormData();
    uploadData.append('file', this.selectedFile, this.selectedFile.name);

    this.http.post<any>('/api/images/upload', uploadData).subscribe(
      (response) => {
        // Handle the response from the backend (e.g., display the uploaded image)
        const imagePath = response;
        console.log('Image path:', imagePath);
        // Perform additional actions as needed, such as updating the UI
      },
      (error) => {
        // Handle the error, if any
      }
    );
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
      this.selectedPost = new Post(0, '', new Date(), user); 
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
