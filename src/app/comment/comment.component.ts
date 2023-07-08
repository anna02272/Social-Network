import { Component, Input } from '@angular/core';
import { UserService } from '../service';
import { CommentService } from '../service/comment.service';
import { Comment } from './comment';
import {RefreshService } from '../service/refresh.service';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent {
  
  @Input() postId!: number;
  comments: Comment[] = [];
  comment: Comment = new Comment(0,'',new Date(),false, this.userService.currentUser,[],[],[],[]
  );

  constructor(
    private userService: UserService,
    private commentService: CommentService,
    private refreshService: RefreshService,
  ) {}

  ngOnInit() {
    this.load();
    this.subscribeToRefresh();
  }

  onSubmit() {
    if (this.comment.id) {
      this.update();
    } else {
      this.create();
    }
  }
  create() {
    this.commentService.create(this.comment).subscribe(() => {
      this.refreshService.refresh();
      this.comment.text = '';
    });
  }
  update() {
    this.commentService.update(this.comment.id, this.comment).subscribe(() => {
      this.refreshService.refresh();
      this.comment.text = '';
    
    });
  }

    edit(comment: Comment): void {
      this.comment = { ...comment }; 
    }

    delete(comment: Comment): void {
      this.commentService.delete(comment.id).subscribe(() => {
        this.comments = this.comments.filter(p => p.id !== comment.id);
      });
    }
  
    load() {
      this.commentService.getAll().subscribe((data: Comment[]) => { // Fetch comments by post ID
        this.comments = data;
      });
    }
  
    private subscribeToRefresh() {
      this.refreshService.getRefreshObservable().subscribe(() => {
        this.load();
      });
    }

    hasSignedIn() {
      return !!this.userService.currentUser;
    }
  
    userName() {
      const user = this.userService.currentUser;
      return user ? user.username : '';
    }
  }
