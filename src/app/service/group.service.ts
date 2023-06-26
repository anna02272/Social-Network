import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';

@Injectable()
export class GroupService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) {
  }

  getGroups() {
    return this.apiService.get(this.config.group_url);
  }

}
