// comment-create.component.ts
import { Component, Input } from '@angular/core';
import { UserService } from '../../services';
import { CommentService } from '../../services/comment.service';
import { Comment } from '../../models/comment';
import { RefreshService } from '../../services/refresh.service';
import { Post } from '../../models/post';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-create-comment',
  templateUrl: './create-comment.component.html',
  styleUrls: ['./create-comment.component.css'],
})
export class CreateCommentComponent {
  @Input() post!: Post;
  newCommentText: string = '';

  constructor(
    private userService: UserService,
    private commentService: CommentService,
    private refreshService: RefreshService,
    private datePipe: DatePipe
  ) {}

  onSubmit() {
    this.create();
  }

  create() {
    if (this.post.id !== undefined) {
      let newComment: Comment;
      newComment = new Comment(
        0,
        this.newCommentText,
        new Date(),
        false,
        this.userService.currentUser
      );

      this.commentService.create(this.post.id, newComment).subscribe(() => {
        this.refreshService.refresh();
        this.newCommentText = '';
      });
    }
  }
  profileImage() {
    const user = this.userService.currentUser;
    return user.image?.path;
  }

}
