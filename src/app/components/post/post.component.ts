import { Component,    OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Post } from '../../models/post';
import { PostService } from '../../services';
import { PostRefreshService } from '../../services/postrefresh.service';
import { ReactionService } from '../../services/reaction.service';
import { Reaction } from '../../models/reaction';
import { DatePipe } from '@angular/common';
import { ReportComponent } from '../report/report.component';
import { MatDialog } from '@angular/material/dialog';
import { User } from 'src/app/models/user';
import { FriendRequestService } from 'src/app/services/friendRequest.service';
import { GroupRequestService } from 'src/app/services/groupRequest.service';
import { Group } from 'src/app/models/group';
import { Banned } from 'src/app/models/banned';
import { BannedService } from 'src/app/services/banned.service';

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
  friends: User[] = [];
  currentUser!: User;
  approvedGroups: Group[] = [];
  blockedGroupUsers: Banned[] = [];

  constructor(
    private userService: UserService,
    private postService: PostService,
    private postRefreshService: PostRefreshService,
    private friendRequestService: FriendRequestService,
    private reactionService: ReactionService,
    private datePipe: DatePipe,
    private dialog: MatDialog,
    private groupRequestService: GroupRequestService,
    private bannedService: BannedService
  ) {}

  ngOnInit() {
    this.loadPosts();
    this.subscribeToRefreshPosts();
    this.subscribeToOpenModal();
    this.loadApprovedFriends();
   
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
        for (const post of this.posts) {
          if (post.group != null) {
            this.loadBlockedGroupUsers(post);
          }
        }
      });
    } else {
      this.postService.getAllDescending().subscribe((data: Post[]) => {
        this.posts = data;
        for (const post of this.posts) {
          if (post.group != null) {
            this.loadBlockedGroupUsers(post);
          }
        }
      });
    }
  }

  loadApprovedFriends() {
      this.userService.getMyInfo().subscribe((user) => {
        this.currentUser = user;
  
        this.friendRequestService
        .getApprovedFriendsForUser(this.currentUser.id)
        .subscribe((friends: User[]) => {
          this.friends = friends;
        });
        this.groupRequestService
        .getApprovedGroupsForUser(this.currentUser.id)
        .subscribe((approvedGroups: Group[]) => {
          this.approvedGroups = approvedGroups;
        });
      });
  }

  shouldDisplayPost(post: Post): boolean {
    if (post.group) {
      const isUserMemberOfGroup = this.isUserMemberOfGroup(post.group.id);
      if (isUserMemberOfGroup) {
        if (!this.isCurrentUserBlocked()) {
        return true;
        }
      } else {
        return false; 
      }
    }
    
    return this.friends.some(friend => friend.id === post.user.id);
  }
  
  isCurrentUserBlocked(): boolean {
    if (!this.blockedGroupUsers) {
      return false; 
    }
    return this.blockedGroupUsers.some(
      (blockedGroupUser) => blockedGroupUser.bannedUser?.id === this.userService.currentUser.id 
    );
  }
  loadBlockedGroupUsers(post: Post) {
    if(post.group != null) {
    this.bannedService.getAllBlockedGroupUsers(post.group.id).subscribe((data: Banned[]) => {
      this.blockedGroupUsers = data;
        });
      }
  }
  private isUserMemberOfGroup(groupId: number): boolean {
    return this.approvedGroups.some(group => group.id === groupId);
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
 
  openReportModal(post: Post) {
    const dialogRef = this.dialog.open(ReportComponent, {
      width: '500px', 
      data: { postId: post.id },
    });

    dialogRef.afterClosed().subscribe(result => {
    });
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
