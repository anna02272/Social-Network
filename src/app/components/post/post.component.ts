import { Component,    OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Post } from '../../models/post';
import { PostService } from '../../services';
import { PostRefreshService } from '../../services/postrefresh.service';
import { ReactionService } from '../../services/reaction.service';
import { Reaction } from '../../models/reaction';
import { DatePipe } from '@angular/common';


declare var $: any;
@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
  providers: [DatePipe]
})
export class PostComponent implements OnInit {
  posts: Post[] = [];
  comments: Comment[] =[];
  sortingOrder: string = 'ascending';

  constructor(
    private userService: UserService,
    private postService: PostService,
    private postRefreshService: PostRefreshService,
    private reactionService: ReactionService,
    private datePipe: DatePipe
  ) {}

  ngOnInit() {
    this.loadPosts();
    this.subscribeToRefreshPosts();
    this.subscribeToOpenModal();
    
  }

  // loadPosts() {
  //   this.postService.getAllPosts().subscribe((data: Post[]) => {
  //     this.posts = data;
  //       });
  // }
  loadPosts() {
    if (this.sortingOrder === 'ascending') {
      this.postService.getAllAscending().subscribe((data: Post[]) => {
        this.posts = data;
      });
    } else {
      this.postService.getAllDescending().subscribe((data: Post[]) => {
        this.posts = data;
      });
    }
  }

  onSelectedPost(post: Post) {
    this.postRefreshService.setPost(post);
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

  toggleSortingOrder() {
    this.sortingOrder = this.sortingOrder === 'ascending' ? 'descending' : 'ascending';
    this.loadPosts();
  }

  sortAscending() {
    this.sortingOrder = 'ascending';
    this.loadPosts();
  }
  
  sortDescending() {
    this.sortingOrder = 'descending';
    this.loadPosts();
  }
     
  openModal() {
    $('#createModal').modal('show');
  }

  userName() {
    const user = this.userService.currentUser;
    return user ? user.username : '';
    
  }
  userType() {
    const user = this.userService.currentUser;
    return user ? user.type : '';
  }
  reactToPost(post: Post, reaction: Reaction): void {
    this.reactionService.reactToPost(post.id, reaction).subscribe(() => {
      this.loadPosts();
    });
  }
  formatDate(date: any): string {
    const [year, month, day, hour, minute] = date;
    const formattedDate = new Date(year, month - 1, day, hour, minute);
    return this.datePipe.transform(formattedDate, 'HH:mm dd/MM/yyy ') || '';
  }
  
}
