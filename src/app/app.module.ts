import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { HeaderComponent } from './header/header.component';
import { HomeComponent } from './home/home.component';
import { UserMenuComponent } from './user-menu/user-menu.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {ApiService} from './service/api.service';
import {PostService} from './service/post.service';
import {AuthService} from './service/auth.service';
import {UserService} from './service/user.service';
import {ConfigService} from './service/config.service';
import { AuthGuard } from './service/auth.guard';

import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './interceptor/TokenInterceptor';
import { GroupComponent } from './group/group.component';
import { PostComponent } from './post/post.component';
import { CreatePostComponent } from './create-post/create-post.component';
import { GroupsComponent } from './groups/groups.component';
import { FirendsComponent } from './firends/firends.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { FriendRequestComponent } from './friend-request/friend-request.component';
import { GroupAdminService } from './service/groupadmin.service';
import { GroupService } from './service';
import { CommentComponent } from './comment/comment.component';
import { ReactionComponent } from './reaction/reaction.component';
import { ReportComponent } from './report/report.component';
import { CommentService } from './service/comment.service';
import { ReactionService } from './service/reaction.service';
import { ReactionCommentComponent } from './reaction-comment/reaction-comment.component';

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
    ReactionCommentComponent
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
    MatInputModule
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
    ReactionService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
