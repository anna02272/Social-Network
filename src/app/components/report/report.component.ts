import { Component, OnInit, ElementRef, ViewChild, Inject } from '@angular/core';
import { UserService } from '../../services/user.service';
import { PostRefreshService } from '../../services/postrefresh.service';
import { ReportService } from 'src/app/services/report.service';
import { Report } from 'src/app/models/report';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { EReportReason } from 'src/app/models/eReportReason';
import { User } from 'src/app/models/user';
import { RefreshService } from 'src/app/services/refresh.service';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {
  report!: Report;
  selectedReport!: Report;
  selectedReportReason!: EReportReason;
  reportReasons: EReportReason[] = Object.values(EReportReason);
  @ViewChild('createModal') createModal!: ElementRef;
  showSuccessMessage: boolean = false;

  constructor(
    private userService: UserService,
    private reportService: ReportService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}


  ngOnInit() {
  }
  
  createReport() {
    if (this.data) {
      const report: Report = { reason: this.selectedReportReason, id: 0, timestamp: new Date(),accepted: false, isDeleted : false,
        user: undefined, post: undefined, comment: undefined,reportedUser: undefined
      };
      if (this.data.postId) {
        this.reportService.reportPost(this.data.postId, report).subscribe(() => {
          this.showSuccessMessage = true;
        });
      } else if (this.data.commentId) {
        this.reportService.reportComment(this.data.commentId, report).subscribe(() => {
          this.showSuccessMessage = true;
        });
      } else if (this.data.reportedUserId) {
        this.reportService.reportUser(this.data.reportedUserId, report).subscribe(() => {
          this.showSuccessMessage = true;
        });
      }
    }
  }

  hasSignedIn() {
    return !!this.userService.currentUser;
  }

  userName() {
    const user = this.userService.currentUser;
    return user ? user.username : '';
  }
}
