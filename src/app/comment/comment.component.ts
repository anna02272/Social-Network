import { Component, Input } from '@angular/core';
import { UserService } from '../service';
import { CommentService } from '../service/comment.service';
import { Comment } from './comment';
import {RefreshService } from '../service/refresh.service';
import { PostRefreshService } from '../service/postrefresh.service';
import { Post } from '../post/post';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent {
  @Input() postId!: number;
  comments: Comment[] = [];
  comment = new Comment(0,'',new Date(),false, this.userService.currentUser,[],[],[],[] );
  @Input() post!: Post;
  editingCommentId: number | null = null;
  newCommentText: string = '';
  editingComment!: Comment;

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
      this.create();
  }

  onSubmitUpdate(comment: Comment) {
    this.update(comment);
  }
  

  create() {
    if (this.post.id !== undefined) {
      const newComment = new Comment(
        0,
        this.newCommentText,
        new Date(),
        false,
        this.userService.currentUser,
        [],
        [],
        [],
        []
      );
  
      this.commentService.create(this.post.id, newComment).subscribe(() => {
        this.refreshService.refresh();
        this.newCommentText = '';
      });
    }
  }
  
  update(comment: Comment) {
    this.commentService.update(comment.id, comment).subscribe(() => {
      this.refreshService.refresh();
    });
  }

    edit(comment: Comment): void {
      this.comment = { ...comment }; 
      this.editingCommentId = comment.id;
    }

    delete(comment: Comment): void {
      this.commentService.delete(comment.id).subscribe(() => {
        this.comments = this.comments.filter(p => p.id !== comment.id);
      });
    }
  
    load() {
      if (this.post && this.post.id) {
        this.commentService.getCommentsByPostId(this.post.id).subscribe((data: Comment[]) => {
          this.comments = data;
        });
      } else {
        this.comments = [];
      }
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