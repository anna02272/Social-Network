import { Component, OnInit } from '@angular/core';
import {UserService} from '../../services/user.service';
import { FriendRequestService } from 'src/app/services/friendRequest.service';
import { User } from 'src/app/models/user';
import { FriendRequest } from 'src/app/models/friendRequest';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  profileName: string | null = null;
  searchKeyword: string = '';
  searchResults: any[] = [];
  forUser: User = new User(0,"", "", "", "", new Date(), "", "")
  friendRequest : FriendRequest =  new FriendRequest(0, false, new Date(),null, this.userService.currentUser, this.forUser )
  showSuccessMessage: boolean = false;
  showErrorMessage: boolean = false;

  constructor( private userService: UserService,
    private friendRequestService: FriendRequestService) { }

    ngOnInit() {
      this.userService.currentUser$.subscribe((user) => {
        if (user) {
          this.profileName = user.profileName;
        }
      });
    }
    clearSearch() {
      this.searchKeyword = '';
    }
    onSearch() {
      if (this.searchKeyword.trim() === '') {
        this.searchResults = [];
        return;
      }
  
      this.friendRequestService.searchUsers(this.searchKeyword).subscribe(
        (response: any) => {
          this.searchResults = response; 
          this.showSuccessMessage = false;
          this.showErrorMessage = false;
        },
        (error) => {
          console.error('Error searching users:', error);
        }
      );
    }
    createFriendRequest(forUser: User) {
      this.friendRequest.forUser = forUser; 
      this.friendRequestService.create(forUser.id, this.friendRequest).subscribe(
        () => {
          this.showSuccessMessage = true;
        },
        (error) => {
          if (error.status === 400) {
            this.showErrorMessage = true;
          }
        }
      );
      this.showSuccessMessage = false;
      this.showErrorMessage = false;
    }
  hasSignedIn() {
    return !!this.userService.currentUser;
  }

  username() {
    const user = this.userService.currentUser;
    return user.username;
  }



}
