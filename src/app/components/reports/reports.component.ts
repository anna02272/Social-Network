import { Component, OnInit } from '@angular/core';
import { Report } from 'src/app/models/report';
import { ReportService } from 'src/app/services/report.service';
import { DatePipe } from '@angular/common';
import { UserService } from 'src/app/services';
import { RefreshService } from 'src/app/services/refresh.service';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css'],
  providers: [DatePipe]
})

export class ReportsComponent implements OnInit {
  reportedPosts: Report[] = [];
  reportedComments: Report[] = [];
  reportedUsers: Report[] = [];

  constructor(
    private reportService: ReportService,
    private datePipe: DatePipe,
    private userService: UserService,
    private refreshService : RefreshService,

  ) { 
    this.refreshService.getRefreshObservable().subscribe(() => {
    this.refreshContent();
  });}


  ngOnInit() {
    this.loadReportsForPost();
    this.loadReportsForComment();
    this.loadReportsForUser();
    
  }
  refreshContent() {
    this.loadReportsForPost();
    this.loadReportsForComment();
    this.loadReportsForUser();
  }
  
   loadReportsForPost() {
    this.reportService.getReportedPosts().subscribe((data: Report[]) => {
      this.reportedPosts = data;
        });
  }
  loadReportsForComment() {
    this.reportService.getReportedComments().subscribe((data: Report[]) => {
      this.reportedComments = data;
        });
  }
  loadReportsForUser() {
    this.reportService.getReportedUsers().subscribe((data: Report[]) => {
      this.reportedUsers = data;
        });
  }
  approve(report: Report) {
    this.reportService.approve(report.id).subscribe(() => {
      this.refreshService.refresh();
    });
  }
  decline(report: Report) {
    this.reportService.decline(report.id).subscribe(() => {
      this.refreshService.refresh();
    });
  }
  formatDate(date: any): string {
    const [year, month, day] = date;
    const formattedDate = new Date(year, month - 1, day);
    return this.datePipe.transform(formattedDate, 'dd/MM/yyyy') || '';
  }
  userType() {
    const user = this.userService.currentUser;
    return user ? user.type : '';
  }
}