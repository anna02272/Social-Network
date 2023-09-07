import { Component, OnInit, ElementRef, ViewChild, Input } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Post } from '../../models/post';
import { PostService } from '../../services';
import { PostRefreshService } from '../../services/postrefresh.service';
import { Image } from 'src/app/models/image';
import { Group } from 'src/app/models/group';
import { RefreshService } from 'src/app/services/refresh.service';


@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit {
  post!: Post;
  selectedPost!: Post;
  image! : Image[];
  @Input() group!: Group;
  selectedImages: File[] = [];
  @ViewChild('createModal') createModal!: ElementRef;
  @ViewChild('fileInput') fileInput: any;

  constructor(
    private userService: UserService,
    private postService: PostService,
    public postRefreshService: PostRefreshService
  ) {}


  onFileSelected(event: any) {
    const files: FileList = event.target.files;
    for (let i = 0; i < files.length; i++) {
      this.selectedImages.push(files[i]);
    }
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
    this.fileInput.nativeElement.value = '';
    
  }
  combinedMethod() {
    this.onSubmit();
  }

  createPost() {
    if (this.group === undefined) {
      const formData = new FormData();
      formData.append('content', this.post.content);
      for (let i = 0; i < this.selectedImages.length; i++) {
        formData.append('images', this.selectedImages[i]);
      }
      this.postService.createPost(formData).subscribe(() => {
        this.postRefreshService.refreshPosts();
        this.post.content = '';
        this.selectedImages = []; 
    });
  }else {
    const formData = new FormData();
    formData.append('content', this.post.content);
    for (let i = 0; i < this.selectedImages.length; i++) {
      formData.append('images', this.selectedImages[i]);
    }
    this.postService.createGroupPost(this.group.id, formData).subscribe(() => {
      this.postRefreshService.refreshPosts();
      this.post.content = '';
      this.selectedImages = []; 
      this.closeModal();
    });
  }
}

  updatePost() {
    const formData = new FormData();
    formData.append('content', this.selectedPost.content);
  
    for (let i = 0; i < this.selectedImages.length; i++) {
      formData.append('images', this.selectedImages[i]);
    }

    this.postService.updatePost(this.selectedPost.id, formData).subscribe(() => {
      this.postRefreshService.refreshPosts();
      this.selectedPost.content = '';
      this.selectedImages = []; 
     
    });
  }
  deleteImage(image: Image): void {
    this.postService.deleteImage(image.id).subscribe(() => {
      const index = this.selectedPost.images.indexOf(image);
      if (index !== -1) {
        this.selectedPost.images.splice(index, 1);}
      this.postRefreshService.refreshPosts();
    });
  }
  
  
  onModalHidden() {
    this.userService.getMyInfo().subscribe(user => {
      this.selectedPost = new Post(0, '', new Date(), user, null, this.image); 
      });
      
    this.fileInput.nativeElement.value = '';
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
