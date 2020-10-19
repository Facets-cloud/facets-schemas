import { Component, OnInit } from '@angular/core';
import { NbDialogRef } from '@nebular/theme';

@Component({
  selector: 'app-delete-override-dialog',
  templateUrl: './delete-override-dialog.component.html',
  styleUrls: ['./delete-override-dialog.component.scss']
})
export class DeleteOverrideDialogComponent implements OnInit {

  constructor(protected dialogRef: NbDialogRef<DeleteOverrideDialogComponent>) { }

  ngOnInit(): void {
  }

  proceed() {
    this.dialogRef.close(true);
  }
  
  dismiss() {
    this.dialogRef.close(false);
  }

}
