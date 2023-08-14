import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { AuthService, UserService } from '../../services';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

interface DisplayMessage {
  msgType: string;
  msgBody: string;
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  title = 'Smiley';
  register = 'Registration';
  form: FormGroup = new FormGroup({});

  submitted = false;

  notification: DisplayMessage = {} as DisplayMessage;
  returnUrl = '';
  private ngUnsubscribe: Subject<void> = new Subject<void>();

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder
  ) {

  }

  ngOnInit() {
    this.route.params
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((params: Params) => {
        this.notification = params as DisplayMessage || { msgType: '', msgBody: '' };
      });
  
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    this.form = this.formBuilder.group({
      username: ['', Validators.compose([Validators.required, Validators.minLength(4), Validators.maxLength(64)])],
      password: ['', Validators.compose([Validators.required, Validators.minLength(8), Validators.maxLength(32)])],
      email: ['', Validators.compose([Validators.required, Validators.email, Validators.minLength(8), Validators.maxLength(64)])],
      firstName: ['', Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(64)])],
      lastName: ['', Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(64)])]
    });
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }

  onSubmit() {
    this.notification = { msgType: '', msgBody: '' };
    this.submitted = true;

    this.authService.signup(this.form.value).subscribe(
      (data) => {
        this.authService.login(this.form.value).subscribe(() => {
          this.userService.getMyInfo().subscribe();
        });
        this.router.navigate(['/home']);
      },
      (error) => {
        this.submitted = false;
        if (error.status === 409) {
          this.notification = { msgType: 'error', msgBody: 'Username or email already exists.' };
        } else {
          this.notification = { msgType: 'error', msgBody: error['error'].message };
        }
      }
    );
  }

}
