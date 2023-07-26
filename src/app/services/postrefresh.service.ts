import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Post } from '../models/post';

@Injectable({
  providedIn: 'root'
})
export class PostRefreshService {
  private refreshSubject = new Subject<void>();
  private openModalSubject = new Subject<void>();
  private post$ = new BehaviorSubject<any>({});
  selectedPost$ = this.post$.asObservable();


  setPost(post: any) {
    this.post$.next(post);
  }
  
  refreshPosts() {
    this.refreshSubject.next();
  }
  openModal() {
    this.openModalSubject.next();
  }
 
  getRefreshObservable() {
    return this.refreshSubject.asObservable();
  }
  getOpenModalObservable() {
    return this.openModalSubject.asObservable();
  }
 
}
