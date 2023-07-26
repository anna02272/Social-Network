import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-user-menu',
  templateUrl: './user-menu.component.html',
  styleUrls: ['./user-menu.component.css']
})
export class UserMenuComponent implements OnInit {

  user: any;

  constructor(
    private authService: AuthService,
    private userService: UserService) { }

  ngOnInit() {
    this.user = this.userService.currentUser;
  }

  logout() {
    this.authService.logout();
  }

}
