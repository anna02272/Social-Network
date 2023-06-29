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
  private editSubject = new Subject<Post>();


  refreshPosts() {
    this.refreshSubject.next();
  }
  openModal(post?: Post) {
    this.selectedPost = post || null;
    this.openModalSubject.next();
  }
  setPost(post: Post) {
    this.selectedPost = post;
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
  startEditing(post: Post) {
    this.editSubject.next(post);
  }

  getEditObservable() {
    return this.editSubject.asObservable();
  }
}
