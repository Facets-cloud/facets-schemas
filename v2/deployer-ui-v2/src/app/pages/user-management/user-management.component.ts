import { Component, OnInit } from '@angular/core';
import { ApplicationControllerService } from '../../api/services';
import { User } from '../../api/models';

@Component({
  selector: 'user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {
  users: User[] = [];
  allUsers: User[] = [];

  settings = {
    columns: {
      userName: {
        title: 'User Name',
        filter: false,
        width: '45%',
      },
      rolesString: {
        title: 'Roles',
        filter: false,
        width: '45%',
      },
    },
    noDataMessage: '',
    add: {
      addButtonContent: '<i class="nb-plus"></i>',
      createButtonContent: '<i class="nb-checkmark"></i>',
      cancelButtonContent: '<i class="nb-close"></i>',
      confirmCreate: true,
    },
    edit: {
      editButtonContent: '<i class="nb-edit"></i>',
      saveButtonContent: '<i class="nb-checkmark"></i>',
      cancelButtonContent: '<i class="nb-close"></i>',
      confirmSave: true,
    },
    pager: {
      display: true,
      perPage: 10,
    },
    actions: {
      position: 'right',
      delete: false,
    },
  };

  constructor(private applicationControllerService: ApplicationControllerService) { }

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.applicationControllerService.getUsersUsingGET().subscribe(
      users => {
        users.forEach(u => u['rolesString'] = u.roles.join(','));
        this.users = users;
        this.allUsers = users;
      },
    );
  }

  onDeleteConfirm(event) {

  }

  onCreateConfirm(event) {
    this.applicationControllerService.createUserUsingPOST(
      {
        userName: (<string> event.newData['userName']).trim(),
        roles: (<string> event.newData['rolesString']).trim().split(','),
      },
    ).subscribe(
      user => {
        event.confirm.resolve(event.newData);
        this.loadUsers();
      },
    );
  }

  onEditConfirm(event) {
    this.applicationControllerService.createUserUsingPOST(
      {
        userName: (<string> event.newData['userName']).trim(),
        roles: (<string> event.newData['rolesString']).trim().split(','),
        id: event.newData['id'],
      },
    ).subscribe(
      user => {
        event.confirm.resolve(event.newData);
        this.loadUsers();
      },
    );
  }

  search(input) {
    this.users = this.allUsers.filter(x => x.userName.startsWith(input));
  }
}
