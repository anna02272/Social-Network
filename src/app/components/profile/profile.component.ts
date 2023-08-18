import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GroupService, UserService } from 'src/app/services';
import { DatePipe } from '@angular/common';
import { User } from 'src/app/models/user';
import { GroupRequestService } from 'src/app/services/groupRequest.service';
import { FriendRequestService } from 'src/app/services/friendRequest.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Group } from 'src/app/models/group';
import { MatDialog } from '@angular/material/dialog';
import { ReportComponent } from '../report/report.component';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
  providers: [DatePipe]
})
export class ProfileComponent {
  profileForm!: FormGroup;
  approvedGroups: Group[] = [];
  friends: User[] = [];
  userId!: number; 
  user!: User;

  constructor(
    private userService: UserService,
    private groupRequestService: GroupRequestService,
    private friendRequestService: FriendRequestService,
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private dialog: MatDialog,
  ) {}


  
  ngOnInit(): void{
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.userId = +params.get('id')!;
      this.loadUserData();
    });

    this.profileForm = this.formBuilder.group({
      firstName: [{ value: '', disabled: true }],
      lastName: [{ value: '', disabled: true }],
      profileName: [{ value: '', disabled: true }],
      username: [{ value: '', disabled: true }],
      email: [{ value: '', disabled: true }],
      description: [{ value: '', disabled: true }],
    });
   
  }
  loadUserData() {
    this.userService.getUserById(this.userId).subscribe((user: User) => {
      this.user = user;
      this.profileForm.patchValue(user);

      this.groupRequestService
        .getApprovedGroupsForUser(this.user.id)
        .subscribe((approvedGroups: Group[]) => {
          this.approvedGroups = approvedGroups;
        });
      this.friendRequestService
        .getApprovedFriendsForUser(this.user.id)
        .subscribe((friends: User[]) => {
          this.friends = friends;
        });
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
  formatDate(date: any): string {
    if (date) {
      const [year, month, day, hour, minute] = date;
      const formattedDate = new Date(year, month - 1, day, hour, minute);
      return this.datePipe.transform(formattedDate, 'HH:mm dd/MM/yyyy ') || '';
    } else {
      return 'The user hasn\'t logged in yet'; 
    }
  }
  

}

