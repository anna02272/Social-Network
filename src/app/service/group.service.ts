import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import { Group } from '../group/group';


@Injectable()
export class GroupService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) {
  }

    createGroup(group: Group){
      return this.apiService.post(this.config.group_url + "/create", group);
    }
  
    deleteGroup(id: number) {
    return this.apiService.delete(this.config.group_url + '/delete/' + id);
  }
    
  updateGroup(groupId: number, group: Group) {
    const url = `${this.config.group_url}/update/${groupId}`;
    return this.apiService.put(url, group);
  }
  
    getAllGroups() {
      return this.apiService.get(this.config.group_url + "/all");
     }
     getGroupById(id : number) {
      return this.apiService.get(this.config.group_url + "/find/" + id );
     }
    
  
  
  }

