import { Component, OnInit } from '@angular/core';
import { NbDialogRef } from '@nebular/theme';

@Component({
  selector: 'app-pause-release-dialog',
  templateUrl: './pause-release-dialog.component.html',
  styleUrls: ['./pause-release-dialog.component.scss']
})
export class PauseReleaseDialogComponent implements OnInit {
  status: string;

  constructor(protected dialogRef: NbDialogRef<PauseReleaseDialogComponent>) { }

  ngOnInit(): void {
    console.log("status is " + this.status);
  }

  proceed() {
    this.dialogRef.close(true);
  }
  
  dismiss() {
    this.dialogRef.close(false);
  }

}
