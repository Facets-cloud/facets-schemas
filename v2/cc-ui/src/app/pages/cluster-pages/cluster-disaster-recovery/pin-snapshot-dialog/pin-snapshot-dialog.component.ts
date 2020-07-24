import { Component, OnInit } from '@angular/core';
import { NbDialogRef } from '@nebular/theme';

@Component({
  selector: 'app-pin-snapshot-dialog',
  templateUrl: './pin-snapshot-dialog.component.html',
  styleUrls: ['./pin-snapshot-dialog.component.scss']
})
export class PinSnapshotDialogComponent implements OnInit {

  constructor(protected dialogRef: NbDialogRef<PinSnapshotDialogComponent>) { }

  ngOnInit(): void {
  }

  proceed() {
    this.dialogRef.close(true);
  }

  dismiss() {
    this.dialogRef.close(false);
  }
}
