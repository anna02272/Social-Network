import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Group } from 'src/app/models/group';
import { GroupService } from 'src/app/services';

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css'],
  providers: [DatePipe]
})
export class GroupComponent implements OnInit {
  groupId!: number; 
  group!: Group;

  constructor(
    private groupService: GroupService,
    private route: ActivatedRoute ,
    private datePipe: DatePipe,
  ) {}

  ngOnInit(): void {
    this.groupId = +this.route.snapshot.paramMap.get('id')!;
    this.groupService.getGroupById(this.groupId).subscribe((group: Group) => {
      this.group = group;
    });
  }
  formatDate(date: any): string {
    const [year, month, day, hour, minute] = date;
    const formattedDate = new Date(year, month - 1, day, hour, minute);
    return this.datePipe.transform(formattedDate, 'HH:mm dd/MM/yyy ') || '';
  }
}
