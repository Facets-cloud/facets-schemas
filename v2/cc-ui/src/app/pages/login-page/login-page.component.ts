import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PublicApIsService} from "../../cc-api/services/public-ap-is.service";
import {CustomOAuth2ClientRegistration} from "../../cc-api/models/custom-oauth-2client-registration";

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
  logo: any;
  loginOptions: Array<CustomOAuth2ClientRegistration>;

  constructor(private http: HttpClient, private publicApIsService: PublicApIsService) {
  }

  ngOnInit(): void {
    this.http.get("public/v1/logo", {responseType: 'text'}).subscribe(
      x => {
        this.logo = x
      }
    )
    this.publicApIsService.getLoginOptionsUsingGET().subscribe(response => {
      this.loginOptions = response;
    })
  }

  googleLogin(registrationId: string) {
    window.location.href = '/oauth2/authorization/' + registrationId + '?state=' + window.location.href;
  }

  formLogin() {
    let data = new FormData();
    data.append("username", this.username);
    data.append("password", this.password);
    if (!(this.username && this.password)) {
      this.errorMsg = "Both username and password should be specified"
      return
    }
    this.http.post('perform_login', data, {responseType: 'text'}).subscribe(
      () => {
        window.location.href = '/capc/home'
      },
      (err) => {
        if (err.status == 404) {
          window.location.href = '/capc/home'
          return;
        }
        console.log(err);
        this.errorMsg = "Invalid credentials"
      })
  }
}
