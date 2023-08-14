import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { GroupRequest } from '../../models/groupRequest';
import { GroupRequestService } from '../../services/groupRequest.service';
import { ActivatedRoute } from '@angular/router';
import { RefreshService } from '../../services/refresh.service';

@Component({
  selector: 'app-group-request',
  templateUrl: './group-request.component.html',
  styleUrls: ['./group-request.component.css'],
  providers: [DatePipe]
})
export class GroupRequestComponent implements OnInit {
  groupRequests: GroupRequest[] = [];
  groupId!: number;
  
  constructor(
    private groupRequestService: GroupRequestService,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private refreshService: RefreshService,
  ) {}
  
  ngOnInit() {
    this.route.params.subscribe(params => {
      this.groupId = +params['id']; 
      this.load(this.groupId);
    });
    
    this.subscribeToRefresh();
  }
  load(groupId: number) {
    this.groupRequestService.getByGroup(groupId).subscribe((data: GroupRequest[]) => {
      this.groupRequests = data;
    });
  }
  approve(groupRequest: GroupRequest) {
      this.groupRequestService.approve(groupRequest.id).subscribe(() => {
        this.refreshService.refresh();
      });
    }
    decline(groupRequest: GroupRequest) {
      this.groupRequestService.decline(groupRequest.id).subscribe(() => {
        this.refreshService.refresh();
      });
    }
    private subscribeToRefresh() {
      this.refreshService.getRefreshObservable().subscribe(() => {
        this.load(this.groupId);
      });
    }

  formatDate(date: any): string {
    const [year, month, day, hour, minute] = date;
    const formattedDate = new Date(year, month - 1, day, hour, minute);
    return this.datePipe.transform(formattedDate, 'HH:mm dd/MM/yyy ') || '';
  }
}
