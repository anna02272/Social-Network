import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  private _api_url = 'http://localhost:8080/api';
  private _user_url = this._api_url + '/users';

  private _login_url = this._user_url + '/login';

  get login_url(): string {
    return this._login_url;
  }

  private _signup_url = this._user_url + '/signup';

  get signup_url(): string {
    return this._signup_url;
  }

  get user_url(): string {
    return this._user_url;
  }

  private _whoami_url = this._user_url + '/whoami';

  get whoami_url(): string {
    return this._whoami_url;
  }

  private _group_url = this._api_url + '/groups';

  get groupRequest_url(): string {
    return this._groupRequest_url;
  }
  private _groupRequest_url = this._api_url + '/groupRequests';

  get friendRequest_url(): string {
    return this._friendRequest_url;
  }
  private _friendRequest_url = this._api_url + '/friendRequests';

  get group_url(): string {
    return this._group_url;
  }
  private _groupadmin_url = this._api_url + '/groupadmins';

  get groupadmin_url(): string {
    return this._groupadmin_url;
  }

  private _post_url = this._api_url + '/posts';

  get post_url(): string {
    return this._post_url;
  }

  private _comment_url = this._api_url + '/comments';

  get comment_url(): string {
    return this._comment_url;
  }

  private _reaction_url = this._api_url + '/reactions';

  get reaction_url(): string {
    return this._reaction_url;
  }

  private _report_url = this._api_url + '/reports';

  get report_url(): string {
    return this._report_url;
  }

  
  }


