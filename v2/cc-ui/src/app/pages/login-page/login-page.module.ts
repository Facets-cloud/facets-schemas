import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginPageComponent } from './login-page.component';
import {LayoutsModule} from "../../layouts/layouts.module";
import {NbButtonModule, NbCardModule, NbIconModule, NbInputModule, NbLayoutModule} from "@nebular/theme";
import {FormsModule} from "@angular/forms";



@NgModule({
  declarations: [LoginPageComponent],
  imports: [
    CommonModule,
    LayoutsModule,
    NbLayoutModule,
    NbCardModule,
    NbIconModule,
    FormsModule,
    NbButtonModule,
    NbInputModule
  ]
})
export class LoginPageModule { }
