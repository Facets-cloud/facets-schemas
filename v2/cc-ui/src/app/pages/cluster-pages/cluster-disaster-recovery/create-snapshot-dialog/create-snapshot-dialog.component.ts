import { Component, OnInit } from '@angular/core';
import { NbDialogRef } from '@nebular/theme';

@Component({
  selector: 'app-create-snapshot-dialog',
  templateUrl: './create-snapshot-dialog.component.html',
  styleUrls: ['./create-snapshot-dialog.component.scss']
})
export class CreateSnapshotDialogComponent implements OnInit {

  constructor(protected dialogRef: NbDialogRef<CreateSnapshotDialogComponent>) { }

  ngOnInit(): void {
  }

  proceed() {
    this.dialogRef.close(true);
  }
  
  dismiss() {
    this.dialogRef.close(false);
  }
}
