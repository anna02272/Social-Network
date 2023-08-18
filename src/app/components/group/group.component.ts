import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Group } from 'src/app/models/group';
import { GroupRequest } from 'src/app/models/groupRequest';
import { Post } from 'src/app/models/post';
import { GroupService, PostService, UserService } from 'src/app/services';
import { GroupRequestService } from 'src/app/services/groupRequest.service';


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
  

  constructor(
    private groupService: GroupService,
    private route: ActivatedRoute ,
    private datePipe: DatePipe,
    private userService: UserService,
    private groupRequestService: GroupRequestService,
  ) {}

  ngOnInit(): void {
    this.groupId = +this.route.snapshot.paramMap.get('id')!;
    this.groupService.getGroupById(this.groupId).subscribe((group: Group) => {
      this.group = group;
      this.checkGroupRequestStatus();
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
          if (groupRequest.approved) {
            this.showContent = true;
          }
        },
        (error) => {
          this.showContent = false;
        }
      );
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
