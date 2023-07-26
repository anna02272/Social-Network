import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { UserService } from '../../services/user.service';
import { GroupService } from '../../services/group.service';
import { Group } from 'src/app/models/group';
import { RefreshService } from '../../services/refresh.service';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
declare var $: any;

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css'],
  providers: [DatePipe]
})
export class GroupsComponent implements OnInit {
  groups: Group[] = [];
  group: Group = new Group(0, '', '', new Date(), false, '', this.userService.currentUser)

  @ViewChild('groupModal') groupModal!: ElementRef;

  constructor(
    private userService: UserService,
    private groupService: GroupService,
    private refreshService: RefreshService,
    private datePipe: DatePipe,
    private router: Router
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
  isGroupNameTaken(): boolean {
    return this.groups.some(group => group.name === this.group.name);
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
