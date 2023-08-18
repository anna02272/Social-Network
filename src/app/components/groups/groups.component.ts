import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { UserService } from '../../services/user.service';
import { GroupService } from '../../services/group.service';
import { Group } from 'src/app/models/group';
import { RefreshService } from '../../services/refresh.service';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { GroupRequestService } from 'src/app/services/groupRequest.service';
import { GroupRequest } from 'src/app/models/groupRequest';
import { SuspendComponent } from '../suspend/suspend.component';
import { MatDialog } from '@angular/material/dialog';

declare var $: any;
interface DisplayMessage {
  msgType: string;
  msgBody: string;
}
@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css'],
  providers: [DatePipe]
})
export class GroupsComponent implements OnInit {
  groups: Group[] = [];
  group: Group = new Group(0, '', '', new Date(), false, '', [])
  groupRequest : GroupRequest =  new GroupRequest(0, false, new Date(),null, this.userService.currentUser, this.group )
  notification: DisplayMessage = {} as DisplayMessage;
  submitted = false;
  @ViewChild('groupModal') groupModal!: ElementRef;
  showSuccessMessage: boolean = false;
  showErrorMessage: boolean = false;

  constructor(
    private userService: UserService,
    private groupService: GroupService,
    private groupRequestService: GroupRequestService,
    private refreshService: RefreshService,
    private datePipe: DatePipe,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.loadGroups();
    this.subscribeToRefreshGroups();
  }

  onSubmit() {
    this.notification = { msgType: '', msgBody: '' };
    this.submitted = true;

    if (this.group.id) {
      this.updateGroup();
    } else {
      this.createGroup();
    }
    
  }
  
  createGroup() {
    this.groupService.createGroup(this.group).subscribe(
      () => {
        this.refreshService.refresh();
        this.group.name = '';
        this.group.description = '';
        this.closeModal();
      },
      (error) => {
        this.submitted = false;
        if (error.status === 409) {
          this.notification = { msgType: 'error', msgBody: 'Group with that name already exists.' };
        } else {
          this.notification = { msgType: 'error', msgBody: error['error'].message };
         }
      }
    );
  }
  
  updateGroup() {
    this.groupService.updateGroup(this.group.id, this.group).subscribe(
      () => {
        this.refreshService.refresh();
        this.group.name = '';
        this.group.description = '';
        this.closeModal();
      },
      (error) => {
        this.submitted = false;
        if (error.status === 409) {
          this.notification = { msgType: 'error', msgBody: 'Group name already exists.' };
        } else {
          this.notification = { msgType: 'error', msgBody: error['error'].message };
         }
      }
    );
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
  
  
  editGroup(group: Group): void {
    this.group = { ...group }; 
    this.openModal();
  }

  loadGroups() {
    this.groupService.getAllGroups().subscribe((data: Group[]) => {
      this.groups = data;
    });
  }
  
  private subscribeToRefreshGroups() {
    this.refreshService.getRefreshObservable().subscribe(() => {
      this.loadGroups();
    });
  }
  viewGroup(group: Group): void {
    this.router.navigate(['/group', group.id]);
  }

  
  closeModal() {
    this.groupModal.nativeElement.dismiss();
  
  }

  openModal() {
    this.groupModal.nativeElement.show();
  }
  
  onModalHidden() {
    this.group = new Group(0, '', '', new Date(), false, '', this.userService.currentUser );
    const groupForm = this.groupModal.nativeElement.querySelector('form');
    groupForm.reset();
  }
  openSuspendModal(group: Group) {
    const dialogRef = this.dialog.open(SuspendComponent, {
      width: '500px', 
      data: { groupId: group.id },
    });
  
    dialogRef.componentInstance.groupSuspended.subscribe((deletedGroupId: number) => {
      this.groups = this.groups.filter(group => group.id !== deletedGroupId);
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
    const [year, month, day, hour, minute] = date;
    const formattedDate = new Date(year, month - 1, day, hour, minute);
    return this.datePipe.transform(formattedDate, 'HH:mm dd/MM/yyy ') || '';
  }
}
