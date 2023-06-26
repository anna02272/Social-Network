import { Component, OnInit } from '@angular/core';
import {UserService} from '../service/user.service';
import { Post } from '../post/post';
import { ConfigService, PostService } from '../service';
import { ActivatedRoute, Router } from '@angular/router';



@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit{
  post : Post;
 
  constructor(
    private userService: UserService,
    private postService : PostService,
    private route: ActivatedRoute, 
    private router: Router, 
    private config: ConfigService
    ) {
      this.post = new Post('', new Date());
    }

  ngOnInit() {
  }
  onSubmit() {
    this.postService.createPost(this.post).subscribe(result =>this.goToHome());
  }

  goToHome() {
    this.router.navigate(['/home']);
  }
  hasSignedIn() {
    return !!this.userService.currentUser;
  }
  userName() {
    const user = this.userService.currentUser;
    return user.username;
  }

}

