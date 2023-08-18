import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from './services/auth.guard'; 

import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import {GroupComponent } from './components/group/group.component';
import {PostComponent } from './components/post/post.component';
import {HomeComponent } from './components/home/home.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { GroupsComponent } from './components/groups/groups.component';
import { GroupRequestComponent } from './components/group-request/group-request.component';
import { ReportComponent } from './components/report/report.component';
import { RequestsComponent } from './components/requests/requests.component';
import { ProfileComponent } from './components/profile/profile.component';



const routes: Routes = [
  {
      path: 'login',
      component: LoginComponent,
    },
    {
      path: '',
      redirectTo: '/login',
      pathMatch: 'full',
    },
    {
      path: 'home',
      component: HomeComponent,
      canActivate: [AuthGuard] 
    },
  {
    path: 'signup',
    component: RegisterComponent,
  },
  {
    path: 'groups',
    component: GroupsComponent,
    canActivate: [AuthGuard] 
  },
  {
    path: 'groupRequests',
    component: GroupRequestComponent,
    canActivate: [AuthGuard] 
  },
  { path: 'groupRequests/:id',
  component: GroupRequestComponent,
  canActivate: [AuthGuard]  },
  {
    path: 'requests',
    component: RequestsComponent,
    canActivate: [AuthGuard] 
  },
  {
    path: 'group',
    component: GroupComponent,
    canActivate: [AuthGuard] 
  },
  { path: 'group/:id',
   component: GroupComponent,
   canActivate: [AuthGuard]  },
  {
    path: 'posts',
    component: PostComponent,
    canActivate: [AuthGuard] 
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [AuthGuard] 
  },
  {
    path: 'profile',
    component: UserProfileComponent,
    canActivate: [AuthGuard] 
  },
  { path: 'profile/:id',
   component: ProfileComponent,
   canActivate: [AuthGuard]  },
  {
    path: 'report',
    component: ReportComponent,
    canActivate: [AuthGuard] 
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
