import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import { GroupRequest } from '../models/groupRequest';

@Injectable()
export class GroupRequestService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService
    
  ) {
  }

  create(groupId: number, groupRequest: GroupRequest){
    const url = `${this.config.groupRequest_url}/create/${groupId}`;
     return this.apiService.post(url, groupRequest);
   }

  approve(id: number) {
  return this.apiService.put(this.config.groupRequest_url + '/approve/' + id);
}
decline(id: number) {
    return this.apiService.put(this.config.groupRequest_url + '/decline/' + id);
  }
  
  getAll() {
    return this.apiService.get(this.config.groupRequest_url + "/all");
   }
   getByGroup(groupId: number) {
    return this.apiService.get(`${this.config.groupRequest_url}/all/${groupId}`);
  }
  
  
   getById(id : number) {
    return this.apiService.get(this.config.groupRequest_url + "/find/" + id );
   }
  
   getByUserAndGroup(userId: number, groupId: number) {
    return this.apiService.get(`${this.config.groupRequest_url}/find/${userId}/${groupId}`);
  }
  getApprovedGroupsForUser(userId: number) {
    const url = `${this.config.groupRequest_url}/approvedGroups/${userId}`;
    return this.apiService.get(url);
  }

}
