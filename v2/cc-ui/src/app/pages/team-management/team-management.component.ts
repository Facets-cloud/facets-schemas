import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NbDialogService } from '@nebular/theme';
import { Stack, Team, TeamMembership } from 'src/app/cc-api/models';
import { UiStackControllerService, UiTeamControllerService } from 'src/app/cc-api/services';

@Component({
  selector: 'app-team-management',
  templateUrl: './team-management.component.html',
  styleUrls: ['./team-management.component.scss']
})
export class TeamManagementComponent implements OnInit {

  team: Team;
  members: Array<TeamMembership>;
  stackNames: Array<string>;
  selectedStackName: string;
  selectedResourceType: string;
  selectedResourceName: string;

  tableSettings = {
    columns: {
      userName: {
        title: 'User Name',
        filter: false,
        width: '98%',
      },
    },
    noDataMessage: '',
    add: {
      addButtonContent: '<i class="eva-plus-outline eva"></i>',
      createButtonContent: '<i class="eva-checkmark-outline eva"></i>',
      cancelButtonContent: '<i class="eva-close-outline eva"></i>',
      confirmCreate: true,
    },
    pager: {
      display: false,
      perPage: 15,
    },
    actions: {
      position: 'right',
      delete: false,
    },
  };

  resourcesTableSettings = {
    columns: {
      stackName: {
        title: 'Stack',
        filter: false,
        width: '33%',
        editor: {
          type: 'list'
        }
      },
      resourceType: {
        title: 'Resource Type',
        filter: false,
        width: '33%',
      },
      resourceName: {
        title: 'Resource Name',
        filter: false,
        width: '33%',
      },
    },
    noDataMessage: '',
    pager: {
      display: false,
      perPage: 15,
    },
    actions: false,
  };

  constructor(private activatedRoute: ActivatedRoute,
    private teamsController: UiTeamControllerService,
    private stackController: UiStackControllerService,
    private dialogService: NbDialogService) { }

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe(
      x => this.teamsController.getTeamUsingGET(x.get('teamId')).subscribe(
        t => this.team = t));
    this.activatedRoute.paramMap.subscribe(
      x => this.teamsController.getTeamMembersUsingGET(x.get('teamId')).subscribe(
        t => this.members = t));
    this.stackController.getStacksUsingGET1().subscribe(
      s => this.stackNames = s.map(x=>x.name),
    )
  }

  onCreateConfirm(event) {
    this.teamsController.addTeamMembersUsingPOST(
      {
        userNames: [(<string> event.newData['userName']).trim()],
        teamId: this.team.id
      },
    ).subscribe(
      user => {
        event.confirm.resolve(event.newData);
        this.ngOnInit();
      },
    );
  }

  appSelected($event: string) {
    this.selectedResourceName = $event;
  }

  rtSelected($event: string) {
    this.selectedResourceType = $event;
  }

  showDialog(dialog) {
    this.dialogService.open(dialog, {}).onClose.subscribe(
      _ => {
        this.team.resources.push({
        resourceType: this.selectedResourceType,
        resourceName: this.selectedResourceName,
        stackName: this.selectedStackName
      });
      this.teamsController.upsertTeamUsingPOST(this.team).subscribe(
        x => this.ngOnInit(),
      )
    },
    )
  }

  addResourceItem(dialog) {
    dialog.close();
  }

  stackSelected(e, rt) {
    this.selectedStackName = e;
    rt.stackName = this.selectedStackName;
    rt.ngOnInit();
  }
}
