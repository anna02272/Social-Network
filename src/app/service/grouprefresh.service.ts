import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GroupRefreshService {
  private refreshSubject = new Subject<void>();
 
  refreshGroups() {
    this.refreshSubject.next();
  }
  getRefreshObservable() {
    return this.refreshSubject.asObservable();
  }
  

}
