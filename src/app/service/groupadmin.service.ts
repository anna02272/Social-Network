import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';

@Injectable()
export class GroupAdminService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) {
  }

     getAllGroupAdmins() {
        return this.apiService.get(this.config.groupadmin_url + "/all");
       }
  
  
  }

