import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent implements OnInit {
  username: any;
  password: any;
  private nbToastrService: any;
  errorMsg: any;

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
  }

  googleLogin() {
    window.location.href = '/oauth2/authorization/google?state='+window.location.href;
  }

  formLogin() {
    let data = new FormData();
    data.append("username", this.username);
    data.append("password", this.password);
    if(!(this.username && this.password)){
      this.errorMsg = "Both username and password should be specified"
      return
    }
    this.http.post('perform_login', data, {responseType: 'text'}).subscribe(
      () => {
        window.location.href = '/capc/home'
      },
      (err) => {
        if(err.status == 404){
          window.location.href = '/capc/home'
          return;
        }
        console.log(err);
        this.errorMsg = "Invalid credentials"
      })
  }
}
