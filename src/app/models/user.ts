export class User {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  lastLogin: Date;
  description: string;
  profileName: string;
  

  constructor(id: number, username: string, firstName: string, lastName: string, email: string, lastLogin: Date, description: string, profileName: string) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.lastLogin = lastLogin;
    this.description = description;
    this.profileName = profileName;
  }
}
