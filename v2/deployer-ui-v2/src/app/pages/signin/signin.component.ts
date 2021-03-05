import { Component, Inject, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { NbToastrService } from '@nebular/theme';

@Component({
  selector: 'signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  username: string;
  password: string;

  constructor(@Inject(DOCUMENT) private document: any, private http: HttpClient, private router: Router,
  private nbToastrService: NbToastrService) { }

  ngOnInit() {
  }

  googleLogin() {
    this.document.location.href = '/oauth2/authorization/google';
  }

  formLogin() {
    let data = new FormData();
    data.append("username", this.username);
    data.append("password", this.password);
    this.http.post('perform_login', data, { responseType: 'text' }).subscribe(
      () => {
        this.document.location.href = '/pages/dashboard'
      },
      (err) => {
        console.log(err);
        this.nbToastrService.danger('Invalid Credentials', 'Error');
      })
  }

}
