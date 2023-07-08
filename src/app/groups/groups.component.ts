import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { UserService } from '../service/user.service';
import { GroupService } from '../service/group.service';
import { Group } from '../group/group';
import { GroupAdminService } from '../service/groupadmin.service';
import { RefreshService } from '../service/refresh.service';
import { User } from '../user';
declare var $: any;

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent implements OnInit {
  groups: Group[] = [];
  group: Group = new Group(0, '', '', new Date(), false, '', this.userService.currentUser)


  @ViewChild('groupModal') groupModal!: ElementRef;

  constructor(
    private userService: UserService,
    private groupService: GroupService,
    private refreshService: RefreshService,
    private groupAdminService: GroupAdminService
  ) {}

  ngOnInit() {
    this.loadGroups();
    this.subscribeToRefreshGroups();
  }

  onSubmit() {
    if (this.group.id) {
      this.updateGroup();
    } else {
      this.createGroup();
    }
  }
  
  createGroup() {
    this.groupService.createGroup(this.group).subscribe(() => {
      this.refreshService.refresh();
      this.group.name = '';
      this.group.description = '';

      this.closeModal();
    });
  }
  updateGroup() {
    this.groupService.updateGroup(this.group.id, this.group).subscribe(() => {
      this.refreshService.refresh();
      this.group.name = '';
      this.group.description = '';
  
      this.closeModal();
    });
  }
  
  editGroup(group: Group): void {
    this.group = { ...group }; 
    this.openModal();
  }
  

  deleteGroup(group: Group): void {
    this.groupService.deleteGroup(group.id).subscribe(() => {
      this.groups = this.groups.filter(p => p.id !== group.id);
    });
  }

  loadGroups() {
    this.groupService.getAllGroups().subscribe((data: Group[]) => {
      this.groups = data;
      this.fetchGroupAdmins();
    });
  }
  private fetchGroupAdmins() {
    this.groups.forEach(group => {
      if (group.groupAdmin && group.groupAdmin.user) {
        const userId = group.groupAdmin.user.id;
        this.userService.getUserById(userId).subscribe((user: User) => {
          group.groupAdmin.user = user;
        });
      }
    });
  }
  
  
  private subscribeToRefreshGroups() {
    this.refreshService.getRefreshObservable().subscribe(() => {
      this.loadGroups();
    });
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

  hasSignedIn() {
    return !!this.userService.currentUser;
  }

  userName() {
    const user = this.userService.currentUser;
    return user ? user.username : '';
  }
}
