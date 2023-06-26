import { Component, OnInit } from '@angular/core';
import {UserService} from '../service/user.service';
import { Post } from './post';
import { PostService } from '../service';


@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit{
  posts : Post[] = [];

  constructor( private userService: UserService, 
    private postService: PostService) { }

  ngOnInit() {
    this.postService.getAllPosts().subscribe(data => {
      this.posts = data;
    });
  }

  hasSignedIn() {
    return !!this.userService.currentUser;
  }

  userName() {
    const user = this.userService.currentUser;
    return user.username;
  }

}
