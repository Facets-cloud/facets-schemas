import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.page.html',
  styleUrls: ['./confirmation-dialog.page.scss'],
})
export class ConfirmationDialogPage implements OnInit {

  @Input() infoText: string;
  @Input() title: string;
  @Input() callback: () => void;

  constructor() { }

  ngOnInit() {
  }

}
