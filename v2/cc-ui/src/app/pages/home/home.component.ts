import {Component, OnInit} from '@angular/core';
import {UiStackControllerService} from '../../cc-api/services/ui-stack-controller.service';
import {Stack} from '../../cc-api/models/stack';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  stacks: Array<Stack>;

  constructor(private uiStackControllerService: UiStackControllerService) {
  }

  ngOnInit(): void {
    this.uiStackControllerService.getStacksUsingGET1().subscribe(
      s => {
        return this.stacks = s;
      }
    );
  }

}
