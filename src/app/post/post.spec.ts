import { Post } from './post';

describe('Post', () => {
  it('should create an instance', () => {
    const content = 'Sample content'; 
    const creationDate = new Date();
   // const user = new User();
    expect(new Post(content, creationDate)).toBeTruthy();
    
  });
});
