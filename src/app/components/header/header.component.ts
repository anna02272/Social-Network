import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  profileName: string | null = null;
  constructor( private userService: UserService) { }
    ngOnInit() {
      this.userService.currentUser$.subscribe((user) => {
        if (user) {
          this.profileName = user.profileName;
        }
      });
    }

  hasSignedIn() {
    return !!this.userService.currentUser;
  }

  username() {
    const user = this.userService.currentUser;
    return user.username;
  }



}
