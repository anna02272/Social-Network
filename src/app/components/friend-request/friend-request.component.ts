import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FriendRequest } from 'src/app/models/friendRequest';
import { UserService } from 'src/app/services';
import { FriendRequestService } from 'src/app/services/friendRequest.service';
import { RefreshService } from 'src/app/services/refresh.service';

@Component({
  selector: 'app-friend-request',
  templateUrl: './friend-request.component.html',
  styleUrls: ['./friend-request.component.css'],
  providers: [DatePipe]
})
export class FriendRequestComponent implements OnInit {
  friendRequests: FriendRequest[] = [];
  forUserId!: number;
  
  constructor(
    private friendRequestService: FriendRequestService,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private refreshService: RefreshService,
    private userService: UserService
  ) {}
  
  ngOnInit() {
    this.route.params.subscribe(params => {
      this.forUserId = +params['id']; 
      this.load(this.forUserId);
    });
    
    
    this.subscribeToRefresh();
  }
  load(forUserId: number) {
    this.friendRequestService.getByForUser(this.userService.currentUser.id).subscribe((data: FriendRequest[]) => {
      this.friendRequests = data;
    });
  }
  approve(friendRequest: FriendRequest) {
      this.friendRequestService.approve(friendRequest.id).subscribe(() => {
        this.refreshService.refresh();
      });
    }
    decline(friendRequest: FriendRequest) {
      this.friendRequestService.decline(friendRequest.id).subscribe(() => {
        this.refreshService.refresh();
      });
    }
    private subscribeToRefresh() {
      this.refreshService.getRefreshObservable().subscribe(() => {
        this.load(this.forUserId);
      });
    }

  formatDate(date: any): string {
    const [year, month, day, hour, minute] = date;
    const formattedDate = new Date(year, month - 1, day, hour, minute);
    return this.datePipe.transform(formattedDate, 'HH:mm dd/MM/yyy ') || '';
  }
}
