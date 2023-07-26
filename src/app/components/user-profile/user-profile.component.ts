import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/services';
import { DatePipe } from '@angular/common';
import { User } from 'src/app/models/user';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
  providers: [DatePipe]
})
export class UserProfileComponent {
  currentUser!: User;
  currentPassword: string = '';
  newPassword: string = '';
  confirmPassword: string = '';
  passwordForm!: FormGroup;
  userForm!: FormGroup;
  successMessage: string = '';
  errorMessage: string = '';
  showPassword: boolean = false;

  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder,
    private datePipe: DatePipe,
  ) {}
  ngOnInit() {
    this.userService.getMyInfo().subscribe((user) => {
      this.currentUser = user;
      this.userForm.patchValue(user);
    });

    this.passwordForm = this.formBuilder.group({
      currentPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required]
    }, {
      validators: this.passwordsMatchValidator
    });
    this.userForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      profileName: [''],
      username: [{ value: '', disabled: true }],
      email: [{ value: '', disabled: true }],
      description: [''],
    });
  }


  hasSignedIn() {
    return !!this.currentUser;
  }
  update(user: User) {
    this.userService.update(user.id, user).subscribe((updatedUser) => {
      this.currentUser = updatedUser; 
      this.successMessage = 'Profile updated successfully!';
      this.errorMessage = '';
      this.userService.setCurrentUser(updatedUser);
    }, (error) => {
      this.successMessage = '';
      this.errorMessage = 'An error occurred while updating the profile. Please try again.';
    });
  }
  
  onSubmit() {
    if (this.userForm.valid) {
      const updatedUser: User = { ...this.currentUser, ...this.userForm.value };
      this.update(updatedUser);
    }
  }
  
  
  changePassword() {
    const currentPassword = this.passwordForm.get('currentPassword')?.value;
    const newPassword = this.passwordForm.get('newPassword')?.value;
    const confirmPassword = this.passwordForm.get('confirmPassword')?.value;

    this.userService.changePassword(currentPassword, newPassword, confirmPassword).subscribe(
      () => {
        this.successMessage = 'Password changed successfully!';
        this.errorMessage = ''; 
        this.passwordForm.reset(); 
      },
      (error) => {
        this.successMessage = ''; 
        this.errorMessage = 'Wrong current password. Please try again.'; 
      }
    );
  }

  passwordsMatchValidator(group: FormGroup) {
    const newPassword = group.get('newPassword')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    return newPassword === confirmPassword ? null : { passwordsNotMatch: true };
  }
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
  formatDate(date: any): string {
    const [year, month, day, hour, minute] = date;
    const formattedDate = new Date(year, month - 1, day, hour, minute);
    return this.datePipe.transform(formattedDate, 'HH:mm dd/MM/yyy ') || '';
  }

}
