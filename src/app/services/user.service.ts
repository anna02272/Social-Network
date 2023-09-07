import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import {map} from 'rxjs/operators';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  currentUser: any;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();
  

  constructor(
    private apiService: ApiService,
    private config: ConfigService,
    private http: HttpClient
  ) {
  }

  getMyInfo() {
    return this.apiService.get(this.config.whoami_url)
      .pipe(map(user => {
        this.currentUser = user;
        return user;
      }));
  }
  getUserById(id : number) {
    return this.apiService.get(this.config.user_url + "/find/" + id );
   }
   
   changePassword(currentPassword: string, newPassword: string, confirmPassword: string): Observable<void> {
    const changePasswordRequest = {
      currentPassword: currentPassword,
      newPassword: newPassword,
      confirmPassword: confirmPassword
    };

    return this.apiService.post(this.config.user_url + "/changePassword", changePasswordRequest);
  }
  update(userId: number, user: User) {
    const url = `${this.config.user_url}/update/${userId}`;
    return this.apiService.put(url, user);
  }
  updateProfilePicture(userId: number, postData: FormData) {
    const url = `${this.config.user_url}/updateProfilePicture/${userId}`;
    return this.http.put(url, postData);
  }
  setCurrentUser(user: User | null) {
    this.currentUserSubject.next(user);
  }
  deleteProfilePicture(userId: number) {
    return this.apiService.delete(this.config.user_url + '/deleteProfilePicture/' + userId);
  }
}
