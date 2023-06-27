import { User } from './user';

describe('User', () => {
  it('should create an instance', () => {
    const id = 1;
    const userName = 'userName';
    expect(new User(id, userName)).toBeTruthy();
  });
});
