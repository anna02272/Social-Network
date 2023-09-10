import { Component, Input } from '@angular/core';
import { UserService } from '../../services';
import { CommentService } from '../../services/comment.service';
import { Comment } from '../../models/comment';
import {RefreshService } from '../../services/refresh.service';
import { Post } from '../../models/post';
import { DatePipe } from '@angular/common';
import { ReportComponent } from '../report/report.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-coment',
  templateUrl: './coment.component.html',
  styleUrls: ['./coment.component.css'],
    providers: [DatePipe]
})

export class ComentComponent {
  @Input() postId!: number;
  parentComment = new Comment(0, '', new Date(), false, this.userService.currentUser);
//   comment = new Comment(0,'',new Date(),false, this.userService.currentUser, this.parentComment);
  editingCommentId: number | null = null;
  replyingCommentId: number | null = null;
  newCommentText: string = '';
  replyCommentText: string = '';
  showReplyForm!: boolean;
    @Input() post!: Post;
  @Input() sortingOrder: string = 'ascending';
  @Input() comments: Comment[] = [];
  @Input() comment!: Comment;
  @Input() replies!: Comment[];
  @Input() parentId!: number | undefined;


  constructor(
    private userService: UserService,
    private commentService: CommentService,
    private refreshService: RefreshService,
    private datePipe: DatePipe,
    private dialog: MatDialog
  ) {  this.showReplyForm = false;}

  ngOnInit() {
  }

  onSubmit() {
      this.create();
  }

  onSubmitUpdate(comment: Comment) {
    this.update(comment);
  }
 
  create() {
    if (this.post.id !== undefined) {
      let newComment: Comment;
      if (this.parentComment.id) {
        newComment = new Comment(  0, this.replyCommentText, new Date(),false, this.userService.currentUser, this.parentComment  );
      } else {
        newComment = new Comment( 0, this.newCommentText,  new Date(),  false, this.userService.currentUser );
      }
  
      this.commentService.create(this.post.id, newComment).subscribe(() => {
        this.refreshService.refresh();
        if (this.parentComment.id) { this.replyCommentText = '';  this.showReplyForm = false;
         this.parentComment = new Comment( 0, '', new Date(), false, this.userService.currentUser );
        } else {
          this.newCommentText = '';

        }
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
        this.refreshService.refresh();
      });
    }
    reply(comment: Comment) {
      if (this.replyingCommentId === comment.id) {
        this.showReplyForm = !this.showReplyForm;
      } else {
        this.parentComment = comment;
        this.replyingCommentId = comment.id;
        this.showReplyForm = true;
      }
    }

      

  openReportModal(comment: Comment) {
    const dialogRef = this.dialog.open(ReportComponent, {
      width: '500px', 
      data: { commentId: comment.id },
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }
  

    hasSignedIn() {
      return !!this.userService.currentUser;
    }
  
    userName() {
      const user = this.userService.currentUser;
      return user ? user.username : '';
    }
    userType() {
      const user = this.userService.currentUser;
      return user ? user.type : '';
    }
    formatDate(date: any): string {
    const [year, month, day] = date;
    const formattedDate = new Date(year, month - 1, day);
    return this.datePipe.transform(formattedDate, 'dd/MM/yyyy') || '';
  }
  }