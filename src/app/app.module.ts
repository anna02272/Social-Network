import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HeaderComponent } from './components/header/header.component';
import { HomeComponent } from './components/home/home.component';
import { UserMenuComponent } from './components/user-menu/user-menu.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {ApiService} from './services/api.service';
import {PostService} from './services/post.service';
import {AuthService} from './services/auth.service';
import {UserService} from './services/user.service';
import {ConfigService} from './services/config.service';
import { AuthGuard } from './services/auth.guard';

import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './interceptor/TokenInterceptor';
import { GroupComponent } from './components/group/group.component';
import { PostComponent } from './components/post/post.component';
import { CreatePostComponent } from './components/create-post/create-post.component';
import { GroupsComponent } from './components/groups/groups.component';
import { FirendsComponent } from './components/firends/firends.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { FriendRequestComponent } from './components/friend-request/friend-request.component';
import { GroupAdminService } from './services/groupadmin.service';
import { GroupService } from './services';
import { CommentComponent } from './components/comment/comment.component';
import { ReactionComponent } from './components/reaction/reaction.component';
import { ReportComponent } from './components/report/report.component';
import { CommentService } from './services/comment.service';
import { ReactionService } from './services/reaction.service';
import { ReactionCommentComponent } from './components/reaction-comment/reaction-comment.component';
import { GroupRequestComponent } from './components/group-request/group-request.component';
import { GroupRequestService } from './services/groupRequest.service';
import { ReportsComponent } from './components/reports/reports.component';
import { ReportService } from './services/report.service';
import { MatDialogModule } from '@angular/material/dialog';
import { RequestsComponent } from './components/requests/requests.component';
import { FriendRequestService } from './services/friendRequest.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HeaderComponent,
    HomeComponent,
    UserMenuComponent,
    GroupComponent,
    PostComponent,
    CreatePostComponent,
    GroupsComponent,
    FirendsComponent,
    UserProfileComponent,
    FriendRequestComponent,
    CommentComponent,
    ReactionComponent,
    ReportComponent,
    ReactionCommentComponent,
    GroupRequestComponent,
    ReportsComponent,
    RequestsComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatMenuModule,
    MatIconModule,
    FormsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
  ],
  providers: [ 
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
    GroupService,
    PostService,
    AuthService,
    ApiService,
    UserService,
    ConfigService,
    AuthGuard,
    CreatePostComponent,
    GroupAdminService,
    CommentService,
    ReactionService, 
    GroupRequestService,
    ReportService,
    FriendRequestService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
