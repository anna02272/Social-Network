import { Component, Input } from '@angular/core';
import { Comment } from 'src/app/models/comment';
import { Post } from 'src/app/models/post';
import { CommentService } from 'src/app/services/comment.service';
import { RefreshService } from 'src/app/services/refresh.service';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent {
  @Input() post!: Post;
  sortingOrder: string = 'ascending';
  comments: Comment[] = [];
  
  
  constructor(
    private commentService: CommentService,
    private refreshService: RefreshService
  ) {  }

  ngOnInit() {
    this.load();
    this.subscribeToRefresh();
  }
  load() {
    if (this.post && this.post.id) {
      if (this.sortingOrder === 'ascending') {
        this.commentService.getAllAscending(this.post.id).subscribe((data: Comment[]) => {
          this.comments = data;
        });
      } else if (this.sortingOrder === 'descending') {
        this.commentService.getAllDescending(this.post.id).subscribe((data: Comment[]) => {
          this.comments = data;
        });
      } else if (this.sortingOrder === 'likesAscending') {
        this.commentService.getAllByAscendingLikes(this.post.id).subscribe((data: Comment[]) => {
          this.comments = data;
        });
      } else if (this.sortingOrder === 'likesDescending') {
        this.commentService.getAllByDescendingLikes(this.post.id).subscribe((data: Comment[]) => {
          this.comments = data;
        });
      } else if (this.sortingOrder === 'dislikesAscending') {
        this.commentService.getAllByAscendingDislikes(this.post.id).subscribe((data: Comment[]) => {
          this.comments = data;
        });
      } else if (this.sortingOrder === 'dislikesDescending') {
        this.commentService.getAllByDescendingDislikes(this.post.id).subscribe((data: Comment[]) => {
          this.comments = data;
        });
      } else if (this.sortingOrder === 'heartsAscending') {
        this.commentService.getAllByAscendingHearts(this.post.id).subscribe((data: Comment[]) => {
          this.comments = data;
        });
      } else if (this.sortingOrder === 'heartsDescending') {
        this.commentService.getAllByDescendingHearts(this.post.id).subscribe((data: Comment[]) => {
          this.comments = data;
        });
      }
    } else {
      this.comments = [];
    }
  }
  toggleSortingOrder(sortOption: string) {
    if (sortOption === 'date') {
      this.sortingOrder = this.sortingOrder === 'ascending' ? 'descending' : 'ascending';
    } else if (sortOption === 'likes') {
      this.sortingOrder = this.sortingOrder === 'likesAscending' ? 'likesDescending' : 'likesAscending';
    } else if (sortOption === 'dislikes') {
      this.sortingOrder = this.sortingOrder === 'dislikesAscending' ? 'dislikesDescending' : 'dislikesAscending';
    } else if (sortOption === 'hearts') {
      this.sortingOrder = this.sortingOrder === 'heartsAscending' ? 'heartsDescending' : 'heartsAscending';
    }
    this.load();
  }
  getRootComments(): Comment[] {
    return this.comments.filter((comment) => comment.parentComment === null);
  }
  // getReplies(commentId: number): Comment[] {
  //   return this.comments
  //     .filter((comment) => comment.parentComment?.id === commentId)
  //     .sort(
  //       (a, b) =>
  //         new Date(a.timeStamp).getTime() - new Date(b.timeStamp).getTime()
  //     );
  // }
  getReplies(commentId: number): Comment[] {
    const replies = this.comments
      .filter((comment) => comment.parentComment?.id === commentId)
      .sort(
        (a, b) =>
          new Date(a.timeStamp).getTime() - new Date(b.timeStamp).getTime()
      );
  
    for (const reply of replies) {
      reply.replies = this.getReplies(reply.id);
    }
  
    return replies;
  }
  

    
  private subscribeToRefresh() {
    this.refreshService.getRefreshObservable().subscribe(() => {
      this.load();
    });
  }
}
