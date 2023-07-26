import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ReactionService } from '../../services/reaction.service';
import { Reaction } from '../../models/reaction';
import { Post } from '../../models/post';
import { EReactionType } from '../../models/eReactionType';
import { User } from '../../models/user';
import { Comment } from '../../models/comment';
import { PostRefreshService } from '../../services/postrefresh.service';
import { Observable, Subscription, map, of, switchMap } from 'rxjs';
import { PostService, UserService } from '../../services';


@Component({
  selector: 'app-reaction',
  templateUrl: './reaction.component.html',
  styleUrls: ['./reaction.component.css']
})
export class ReactionComponent {
  reaction!: Reaction;
  user!: User;
  comment!: Comment;
  selectedReaction!: EReactionType;
  like = EReactionType.LIKE;
  dislike = EReactionType.DISLIKE;
  heart = EReactionType.HEART;
  @Input() post!: Post;
  posts: Post[] = [];

  constructor(private reactionService: ReactionService,
    public postRefreshService: PostRefreshService,
    public userService: UserService,
    public postService: PostService
    ) {}

    ngOnInit(): void {
      this.postService.getAllPosts().subscribe((data: Post[]) => {
        this.posts = data;
        this.checkUserReaction();
      });
    }

    reactToPost(): void {
      this.reaction = new Reaction(0, this.selectedReaction, new Date(), this.user, this.post, this.comment);
      console.log(this.post.id);
      console.log(this.selectedReaction);
      if (this.post.id !== undefined && this.selectedReaction !== undefined) {
        this.reactionService.reactToPost(this.post.id, this.reaction).subscribe(() => {
          this.postRefreshService.refreshPosts();
        });
      }
    }
    
    checkUserReaction(): void {
      this.userService.getMyInfo().subscribe(user => {
        if (user && user.id) {
          this.user = user;
    
          for (const post of this.posts) {
            if (post && post.id) {
              this.reactionService.findReactionByPostAndUser(post.id, user.id).subscribe(reaction => {
                if (reaction && reaction.post && reaction.post.id && reaction.post.id === post.id) {
                  switch (reaction.type) {
                    case EReactionType.LIKE:
                      this.changeReactionColor('blue', `like-reaction-icon-${post.id}`);
                      break;
                    case EReactionType.DISLIKE:
                      this.changeReactionColor('blue', `dislike-reaction-icon-${post.id}`);
                      break;
                    case EReactionType.HEART:
                      this.changeReactionColor('blue', `heart-reaction-icon-${post.id}`);
                      break;
                  }
                }
    
                if (post.id) {
                  this.reactionService.countReactionsByPost(post.id).subscribe(reactionCounts => {
                    this.updateReactionCount(reactionCounts, post.id);
                  });
                }
              });
            }
          }
        }
      });
    }
    

    updateReactionCount(reactionCounts: any, postId: number): void {
      const likeCount = reactionCounts['LIKE'] || 0;
      const dislikeCount = reactionCounts['DISLIKE'] || 0;
      const heartCount = reactionCounts['HEART'] || 0;
      const likeCountSpan = document.getElementById(`like-reaction-count-${postId}`);
      if (likeCountSpan) {
        likeCountSpan.innerText = likeCount.toString();
      }
    
      const dislikeCountSpan = document.getElementById(`dislike-reaction-count-${postId}`);
      if (dislikeCountSpan) {
        dislikeCountSpan.innerText = dislikeCount.toString();
      }
    
      const heartCountSpan = document.getElementById(`heart-reaction-count-${postId}`);
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