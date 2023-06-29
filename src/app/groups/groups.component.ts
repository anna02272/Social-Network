import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { UserService } from '../service/user.service';
import { GroupService } from '../service/group.service';
import { Group } from '../group/group';
import { GroupRefreshService } from '../service/grouprefresh.service';
import { GroupAdminService } from '../service/groupadmin.service';
declare var $: any;

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent implements OnInit {
  groups: Group[] = [];
  group: Group = new Group(0, '', '', new Date(), false, '', '');


  @ViewChild('groupModal') groupModal!: ElementRef;

  constructor(
    private userService: UserService,
    private groupService: GroupService,
    private groupRefreshService: GroupRefreshService,
    private groupAdminService: GroupAdminService
  ) {}

  ngOnInit() {
    this.loadGroups();
    this.subscribeToRefreshGroups();
    this.fetchGroupAdmins();
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
      this.groupRefreshService.refreshGroups();
      this.group.name = '';
      this.group.description = '';

      this.closeModal();
    });
  }
  updateGroup() {
    this.groupService.updateGroup(this.group.id, this.group).subscribe(() => {
      this.groupRefreshService.refreshGroups();
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
    });
  }

  private subscribeToRefreshGroups() {
    this.groupRefreshService.getRefreshObservable().subscribe(() => {
      this.loadGroups();
    });
  }

  private fetchGroupAdmins() {
    this.groupAdminService.getAllGroupAdmins().subscribe((groupAdmins: any[]) => {
      groupAdmins.forEach((groupAdmin: any) => {
        if (groupAdmin.userId !== undefined) { // Check if userId is defined
          this.userService.getUserById(groupAdmin.userId).subscribe(user => {
            const group = this.groups.find(g => g.id === groupAdmin.groupId) as Group | undefined;
            if (group) {
              group.groupAdmin = user.username;
            }
          });
        }
      });
    });
  }

  
  closeModal() {
    this.groupModal.nativeElement.dismiss();
  
  }

  openModal() {
    this.groupModal.nativeElement.show();
  }
  
  onModalHidden() {
    this.group = new Group(0, '', '', new Date(), false, '', '');
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
