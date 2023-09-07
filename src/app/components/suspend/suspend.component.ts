import { Component, ElementRef, EventEmitter, Inject, Output, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Group } from 'src/app/models/group';
import { Report } from 'src/app/models/report';
import { GroupService, UserService } from 'src/app/services';

@Component({
  selector: 'app-suspend',
  templateUrl: './suspend.component.html',
  styleUrls: ['./suspend.component.css']
})

export class SuspendComponent {
  @ViewChild('createModal') createModal!: ElementRef;
  groups: Group[] = [];
  showSuccessMessage: boolean = false;
  suspendedReason: string = '';
  @Output() groupSuspended = new EventEmitter<number>();

  constructor(
    private userService: UserService,
    private groupService: GroupService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  suspendGroup(): void {
    this.groupService.deleteGroup(this.data.groupId, this.suspendedReason).subscribe(() => {
     this.showSuccessMessage = true;
      this.groupSuspended.emit(this.data.groupId);
    });
  }

  userName() {
    const user = this.userService.currentUser;
    return user ? user.username : '';
  }
  imagePath() {
    const user = this.userService.currentUser;
    return user ? user.image.path : '';
  }
}

