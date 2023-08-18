
import { Component,    Input,    OnInit } from '@angular/core';
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

declare var $: any;
@Component({
  selector: 'app-group-post',
  templateUrl: './group-post.component.html',
  styleUrls: ['./group-post.component.css'],
  providers: [DatePipe]
})
export class GroupPostComponent {

  posts: Post[] = [];
  comments: Comment[] =[];
  sortingOrder: string = 'ascending';
  friends: User[] = [];
  currentUser!: User;
  approvedGroups: Group[] = [];
  @Input() group!: Group;

  constructor(
    private userService: UserService,
    private postService: PostService,
    private postRefreshService: PostRefreshService,
    private friendRequestService: FriendRequestService,
    private reactionService: ReactionService,
    private datePipe: DatePipe,
    private dialog: MatDialog,
    private groupRequestService: GroupRequestService,
  ) {}

  ngOnInit() {
    this.loadPosts();
    this.subscribeToRefreshPosts();
    this.subscribeToOpenModal();
  }


  loadPosts() {
    if (this.sortingOrder === 'ascending') {
      this.postService.getByGroupAsc(this.group.id).subscribe((data: Post[]) => {
        this.posts = data;
      });
    } else {
      this.postService.getByGroupDesc(this.group.id).subscribe((data: Post[]) => {
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
 
  openReportModal(post: Post) {
    const dialogRef = this.dialog.open(ReportComponent, {
      width: '500px', 
      data: { postId: post.id },
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }
  openUserReportModal(user: User) {
    const dialogRef = this.dialog.open(ReportComponent, {
      width: '500px', 
      data: { reportedUserId: user.id },
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

