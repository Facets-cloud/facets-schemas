import {Component, OnInit} from '@angular/core';
import {UiUserControllerService} from "../../cc-api/services/ui-user-controller.service";
import {User} from "../../cc-api/models/user";
import {Role} from "../../cc-api/models/role";
import {UserRendererComponent} from "./user-renderer.component";
import {NbDialogService} from "@nebular/theme";
import {PopupAppOverrideComponent} from "../cluster-pages/cluster-overrides/popup-app-override/popup-app-override.component";
import {ApplicationControllerService} from "../../cc-api/services/application-controller.service";

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {
  users: Array<User>;
  roles: Array<Role>;
  baseRoles: Array<Role>;
  additionalRoles: Array<Role>;
  tableSettings: any = {
    columns: {
      userName: {
        title: 'User',
        filter: true,
        type: "custom",
        renderComponent: UserRendererComponent,
      },
      roles: {
        title: 'Roles',
        filter: false,
        type: 'List',
      },
      password: {
        title: 'User Type',
        filter: false,
        valuePrepareFunction: (cell) => {
          if (cell) {
            return 'Password Based'
          } else {
            return 'OAuth/ Google'
          }
        }

      }
    },
    noDataMessage: 'No Users created. Who are you?',
    add: {
      addButtonContent: '<i class="eva-plus-outline eva"></i>',
    },
    pager: {
      display: false,
      perPage: 15,
    },
    mode: "external",
    actions: {
      add: false,
      position: 'right',
      delete: false,
      custom: [{name: 'Edit', title: '<i class="eva-edit-2-outline eva"></i>', type: 'html'}]
    },
  };
  errorMsg: string;

  constructor(private usersController: UiUserControllerService, private dialogService: NbDialogService,
              private applicationController: ApplicationControllerService) {
  }

  ngOnInit(): void {
    this.usersController.getAllRolesUsingGET().subscribe(
      r => {
        this.roles = r
        this.baseRoles = r.filter(r => r.baseRole);
        this.additionalRoles = r.filter(r => !r.baseRole);
        this.usersController.getAllUsersUsingGET().subscribe(
          x => this.users = x,
        );
      }
    )
  }

  userEdited = new class implements User {
    id: string;
    password: string;
    picture: string;
    baseRole: string;
    additionalRoles: Array<string>;
    teams: Array<string>;
    userName: string;
  };

  openCreatePopup(dialogRef) {
    this.errorMsg = "";
    this.userEdited = new class implements User {
      id: string;
      password: string;
      picture: string;
      baseRole: string;
      additionalRoles: Array<string>;
      teams: Array<string>;
      userName: string;
    };
    this.dialogService.open(dialogRef, {context: {edit: false}});
  }

  openEditPopup(dialogRef, event) {
    this.errorMsg = "";
    this.userEdited.id = event.data.id;
    this.userEdited.password = event.data.password;
    this.userEdited.picture = event.data.picture;
    this.userEdited.baseRole = event.data.roles.filter(r => this.baseRoles.map(r2 => r2.id).indexOf(r) != -1)[0]
    this.userEdited.additionalRoles = event.data.roles.filter(r => this.additionalRoles.map(r2 => r2.id).indexOf(r) != -1)
    this.userEdited.teams = event.data.teams
    this.userEdited.userName = event.data.userName
    this.dialogService.open(dialogRef, {context: {edit: true, user: this.userEdited}});
  }

  submitForm(edit: any, dialogRef) {
    if (!this.userEdited.userName || !this.userEdited.baseRole) {
      this.errorMsg = "Please fill all required attributes"
      return;
    }
    if (!edit && !this.userEdited.password) {
      this.errorMsg = "Password is mantatory for new user creation"
      return;
    }
    console.log(this.userEdited);
    let user: User = new class implements User {
      id: string;
      password: string;
      picture: string;
      roles: Array<string>;
      teams: Array<string>;
      userName: string;
    }
    user.id = this.userEdited.id;
    user.password = this.userEdited.password;
    user.userName = this.userEdited.userName;
    user.picture = this.userEdited.picture;
    user.teams = this.userEdited.teams;
    user.roles = [];
    if (this.userEdited.additionalRoles) {
      user.roles.push(...this.userEdited.additionalRoles)
    }
    user.roles.push(this.userEdited.baseRole)

    function success() {
      return value => {
        dialogRef.close(true);
        this.usersController.getAllUsersUsingGET().subscribe(
          x => this.users = x,
        );
      };
    }

    if (edit) {
      this.applicationController.updateUserUsingPUT({user: user, userId: user.id}).subscribe(
        success.call(this),
        error => {
          this.errorMsg = "Something went wrong: " + error.error.message
        },
      )
    } else {
      this.applicationController.createUserUsingPOSTResponse(user).subscribe(
        success.call(this),
        error => {
          this.errorMsg = "Something went wrong: " + error.error.message
        },
      )
    }
  }
}
