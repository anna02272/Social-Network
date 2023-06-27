import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FirendsComponent } from './firends.component';

describe('FirendsComponent', () => {
  let component: FirendsComponent;
  let fixture: ComponentFixture<FirendsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FirendsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FirendsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
