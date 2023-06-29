import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from './service/auth.guard'; 

import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import {GroupComponent } from './group/group.component';
import {PostComponent } from './post/post.component';
import {HomeComponent } from './home/home.component';
import { EditPostComponent } from './edit-post/edit-post.component';



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
    component: GroupComponent,
    canActivate: [AuthGuard] 
  },
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
  { path: 'edit-post/:id', component: EditPostComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
