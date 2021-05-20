import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoleDirective } from './user-role.directive';



@NgModule({
    declarations: [
        UserRoleDirective
    ],
    exports: [
        UserRoleDirective
    ],
    imports: [
        CommonModule
    ]
})
export class DirectivesModule { }
