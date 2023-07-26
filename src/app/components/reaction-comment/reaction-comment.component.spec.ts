import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReactionCommentComponent } from './reaction-comment.component';

describe('ReactionCommentComponent', () => {
  let component: ReactionCommentComponent;
  let fixture: ComponentFixture<ReactionCommentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReactionCommentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReactionCommentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
