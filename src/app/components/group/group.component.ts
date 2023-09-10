import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Banned } from 'src/app/models/banned';
import { Group } from 'src/app/models/group';
import { GroupAdmin } from 'src/app/models/groupAdmin';
import { GroupRequest } from 'src/app/models/groupRequest';
import { Post } from 'src/app/models/post';
import { User } from 'src/app/models/user';
import { GroupService, PostService, UserService } from 'src/app/services';
import { BannedService } from 'src/app/services/banned.service';
import { GroupRequestService } from 'src/app/services/groupRequest.service';
import { RefreshService } from 'src/app/services/refresh.service';


@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css'],
  providers: [DatePipe]
})
export class GroupComponent implements OnInit {
  groupId!: number; 
  group!: Group;
  groupRequest : GroupRequest =  new GroupRequest(0, false, new Date(),null, this.userService.currentUser, this.group )
  showSuccessMessage: boolean = false;
  showErrorMessage: boolean = false;
  showContent: boolean = false;
  approvedUsers: User[] = [];
  banned: Banned = new Banned(0, new Date(), false, this.userService.currentUser, null, null, null)
  blockedGroupUsers: Banned[] = [];
  constructor(
    private groupService: GroupService,
    private route: ActivatedRoute ,
    private datePipe: DatePipe,
    private userService: UserService,
    private groupRequestService: GroupRequestService,
    private bannedService: BannedService,
    private refreshService : RefreshService,
  ) { this.refreshService.getRefreshObservable().subscribe(() => {
    this.loadBlockedGroupUsers(this.group);  });}

  ngOnInit(): void {
    this.groupId = +this.route.snapshot.paramMap.get('id')!;
    this.groupService.getGroupById(this.groupId).subscribe((group: Group) => {
      this.group = group;
      this.checkGroupRequestStatus();
      
      this.groupRequestService
      .getApprovedUsersForGroup(this.group.id)
      .subscribe((approvedUsers: any[]) => {
        this.approvedUsers = approvedUsers;
      });
      
    this.loadBlockedGroupUsers(group);
    });
    
  }
  createGroupRequest(group: Group) {
    this.groupRequest.group = group; 
    this.groupRequestService.create(group.id, this.groupRequest).subscribe(
      () => {
        this.showSuccessMessage = true;
      },
      (error) => {
        if (error.status === 400) {
          this.showErrorMessage = true;
        }
      }
    );
    this.showSuccessMessage = false;
  }
  checkGroupRequestStatus() {
    this.groupRequestService
      .getByUserAndGroup(this.userService.currentUser.id, this.group.id)
      .subscribe(
        (groupRequest) => {
          if (groupRequest.approved ) {
            if (!this.isCurrentUserBlocked()) {
            this.showContent = true;
            }
          }
        },
        (error) => {
          this.showContent = false;
        }
      );
  }  
  isCurrentUserBlocked(): boolean {
    if (!this.blockedGroupUsers) {
      return false; 
    }
    return this.blockedGroupUsers.some(
      (blockedGroupUser) => blockedGroupUser.bannedUser?.id === this.userService.currentUser.id 
    );
  }
    block(user : User, group: Group) {
    this.bannedService.blockGroupUser(user.id, group.id, this.banned).subscribe(() => {
      this.refreshService.refresh();
    });
  }
  unblock(banned: Banned) {
    this.bannedService.unblockUser(banned.id).subscribe(() => {
      this.refreshService.refresh();
    });
  }
  loadBlockedGroupUsers(group: Group) {
    this.bannedService.getAllBlockedGroupUsers(group.id).subscribe((data: Banned[]) => {
      this.blockedGroupUsers = data;
        });
  }
  removeGroupAdmin(group: Group) {
    this.groupService.removeGroupAdmin(group.id).subscribe(() => {
        
      });
      }

  formatDate(date: any): string {
    const [year, month, day, hour, minute] = date;
    const formattedDate = new Date(year, month - 1, day, hour, minute);
    return this.datePipe.transform(formattedDate, 'HH:mm dd/MM/yyy ') || '';
  }
  userName() {
    const user = this.userService.currentUser;
    return user ? user.username : '';
    
  }
  userType() {
    const user = this.userService.currentUser;
    return user ? user.type : '';
  }
}
