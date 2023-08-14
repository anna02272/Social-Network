import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import { Post } from '../models/post';
import { Report } from '../models/report';
import { Comment } from '../models/comment';
import { User } from '../models/user';

@Injectable()
export class ReportService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService
    
  ) {
  }
  

  reportPost(postId: number, report: Report) {
    return this.apiService.post(this.config.report_url + "/reportPost/" + postId, report);
  }
  reportComment(commentId: number, report: Report) {
    return this.apiService.post(this.config.report_url + "/reportComment/" + commentId, report);
  }
  reportUser(reportedUserId: number, report: Report) {
    return this.apiService.post(this.config.report_url + "/reportUser/" + reportedUserId, report);
  }
      
  getReportedPosts() {
    return this.apiService.get(this.config.report_url + "/allPosts");
   }
   getReportedComments() {
    return this.apiService.get(this.config.report_url + "/allComments");
   }
   getReportedUsers() {
    return this.apiService.get(this.config.report_url + "/allUsers");
   }
   approve(id: number) {
    return this.apiService.put(this.config.report_url + '/approve/' + id);
  }
  decline(id: number) {
      return this.apiService.put(this.config.report_url + '/decline/' + id);
    }
  


}
