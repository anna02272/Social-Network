import { Component, Input } from '@angular/core';
import { Reaction } from '../reaction/reaction';
import { User } from '../user';
import { EReactionType } from '../reaction/eReactionType';
import { ReactionService } from '../service/reaction.service';
import { UserService } from '../service';
import { CommentService } from '../service/comment.service';
import { RefreshService } from '../service/refresh.service';
import { Post } from '../post/post';
import { Comment } from '../comment/comment';

@Component({
  selector: 'app-reaction-comment',
  templateUrl: './reaction-comment.component.html',
  styleUrls: ['./reaction-comment.component.css']
})
export class ReactionCommentComponent {
    reaction!: Reaction;
    user!: User;
    post!: Post;
    selectedReaction!: EReactionType;
    like = EReactionType.LIKE;
    dislike = EReactionType.DISLIKE;
    heart = EReactionType.HEART;
    @Input() comment!: Comment;
    comments: Comment[] = [];
  
    constructor(private reactionService: ReactionService,
      public refreshService: RefreshService,
      public userService: UserService,
      public commentService: CommentService
      ) {}
  
      ngOnInit(): void {
        this.commentService.getAll().subscribe((data: Comment[]) => {
          this.comments = data;
          this.checkUserReaction();
        });
      }
  
      reactToComment(): void {
        this.reaction = new Reaction(0, this.selectedReaction, new Date(), this.user, this.post, this.comment);
        console.log(this.comment.id);
        console.log(this.selectedReaction);
        if (this.comment.id !== undefined && this.selectedReaction !== undefined) {
          this.reactionService.reactToComment(this.comment.id, this.reaction).subscribe(() => {
            this.refreshService.refresh();
          });
        }
      }
      
      checkUserReaction(): void {
        this.userService.getMyInfo().subscribe(user => {
          if (user && user.id) {
            this.user = user;
      
            for (const comment of this.comments) {
              if (comment && comment.id) {
                this.reactionService.findReactionByCommentAndUser(comment.id, user.id).subscribe(reaction => {
                  if (reaction && reaction.comment && reaction.comment.id && reaction.comment.id === comment.id) {
                    switch (reaction.type) {
                      case EReactionType.LIKE:
                        this.changeReactionColor('blue', `like-reaction-icon-${comment.id}`);
                        break;
                      case EReactionType.DISLIKE:
                        this.changeReactionColor('blue', `dislike-reaction-icon-${comment.id}`);
                        break;
                      case EReactionType.HEART:
                        this.changeReactionColor('blue', `heart-reaction-icon-${comment.id}`);
                        break;
                    }
                  }
      
                  if (comment.id) {
                    this.reactionService.countReactionsByComment(comment.id).subscribe(reactionCounts => {
                      this.updateReactionCount(reactionCounts, comment.id);
                    });
                  }
                });
              }
            }
          }
        });
      }
      
  
      updateReactionCount(reactionCounts: any, commentId: number): void {
        const likeCount = reactionCounts['LIKE'] || 0;
        const dislikeCount = reactionCounts['DISLIKE'] || 0;
        const heartCount = reactionCounts['HEART'] || 0;
        const likeCountSpan = document.getElementById(`like-reaction-count-${commentId}`);
        if (likeCountSpan) {
          likeCountSpan.innerText = likeCount.toString();
        }
      
        const dislikeCountSpan = document.getElementById(`dislike-reaction-count-${commentId}`);
        if (dislikeCountSpan) {
          dislikeCountSpan.innerText = dislikeCount.toString();
        }
      
        const heartCountSpan = document.getElementById(`heart-reaction-count-${commentId}`);
        if (heartCountSpan) {
          heartCountSpan.innerText = heartCount.toString();
        }
      }
  
      changeReactionColor(color: string, iconId: string): void {
        const reactionIcon = document.getElementById(iconId);
        if (reactionIcon) {
          reactionIcon.style.color = color;
        }
    }    
  }
