import { Component, OnInit } from '@angular/core';
import { NbDialogRef } from '@nebular/theme';

@Component({
  selector: 'app-componentupgradedialog',
  templateUrl: './componentupgradedialog.component.html',
  styleUrls: ['./componentupgradedialog.component.scss']
})
export class ComponentUpgradeDialogComponent implements OnInit {
  component: string;
  version: string;

  constructor(protected dialogRef: NbDialogRef<ComponentUpgradeDialogComponent>) { }

  ngOnInit(): void {
  }

  proceed() {
    this.dialogRef.close(true);
  }
  
  dismiss() {
    this.dialogRef.close(false);
  }

}
