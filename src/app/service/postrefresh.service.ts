import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Post } from '../post/post';

@Injectable({
  providedIn: 'root'
})
export class PostRefreshService {
  private refreshSubject = new Subject<void>();
  private openModalSubject = new Subject<void>();
  private selectedPost: Post | null = null;


  refreshPosts() {
    this.refreshSubject.next();
  }
  openModal(post?: Post) {
    this.selectedPost = post || null;
    this.openModalSubject.next();
  }

  getRefreshObservable() {
    return this.refreshSubject.asObservable();
  }
  getOpenModalObservable() {
    return this.openModalSubject.asObservable();
  }
  getSelectedPost(): Post | null {
    return this.selectedPost;
  }
}
