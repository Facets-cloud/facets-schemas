import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Team, TeamResource } from 'src/app/cc-api/models';
import { UiTeamControllerService } from 'src/app/cc-api/services';

@Component({
  selector: 'app-teams-overview',
  templateUrl: './teams-overview.component.html',
  styleUrls: ['./teams-overview.component.scss']
})
export class TeamsOverviewComponent implements OnInit {

  teams: Team[];
  tableSettings = {
    columns: {
      name: {
        title: 'Team Name',
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
      custom: [{name: 'View', title: '<i class="eva-edit-2-outline eva"></i>', type: 'html'}]
    },
  };

  constructor(private teamsController: UiTeamControllerService, private router: Router) { }

  ngOnInit(): void {
    this.teamsController.getTeamsUsingGET().subscribe(
      x => this.teams = x,
    );
  }

  gotoPage(x): void {
    if (x.action === 'View') {
      const teamName = x.data.id;
      this.router.navigate(['/capc', 'teams', teamName]);
    }
  }

  getResourceCount(cell: Array<TeamResource>, row) {
    return cell.length
  }

  getMemberCount(cell: Array<TeamResource>, row) {
    return cell.length
  }

  onCreateConfirm(event) {
    this.teamsController.upsertTeamUsingPOST(
      {
        name: (<string> event.newData['name']).trim(),
      },
    ).subscribe(
      user => {
        event.confirm.resolve(event.newData);
        this.ngOnInit();
      },
    );
  }

}
