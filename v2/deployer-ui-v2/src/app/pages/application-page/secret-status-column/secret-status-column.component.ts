import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { NbDialogService, NbDialogRef } from '@nebular/theme';
import { SetCredentialValueDialogComponent } from '../set-credential-value-dialog/set-credential-value-dialog.component';

@Component({
  selector: 'secret-status-column',
  templateUrl: './secret-status-column.component.html',
  styleUrls: ['./secret-status-column.component.scss']
})
export class SecretStatusColumnComponent implements OnInit {

  @Input() value: any;

  @Input() rowData: any;

  @Output() updateResult = new EventEmitter<boolean>();

  constructor(private dialogService: NbDialogService) { }

  ngOnInit() {
  }

  setValue() {
    const dialogRef: NbDialogRef<SetCredentialValueDialogComponent> =
      this.dialogService.open(SetCredentialValueDialogComponent,
        { context: {
          application: this.rowData['application'],
          environment: this.value['environment'],
          secretName: this.rowData['secretName'],
          secretType: this.rowData['secretType'],
        } });
    dialogRef.onClose.subscribe(x => {
      this.updateResult.emit(true);
    });
  }

}
