import { Component, OnInit, OnChanges, SimpleChanges, Input, Output, EventEmitter } from '@angular/core';
import { Application, Build } from '../../../api/models';
import { NgForm } from '@angular/forms';
import { ApplicationControllerService } from '../../../api/services';
import { NbDialogService, NbDialogRef } from '@nebular/theme';
import { Router } from '@angular/router';

@Component({
  selector: 'build-dialog',
  templateUrl: './build-dialog.component.html',
  styleUrls: ['./build-dialog.component.scss']
})
export class BuildDialogComponent implements OnInit, OnChanges {

  @Input() application: Application;

  @Output() buildTriggered: EventEmitter<boolean> = new EventEmitter<boolean>();

  build: Build = {};

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.application.currentValue) {
    }
  }

  constructor(private applicationControllerService: ApplicationControllerService,
    private router: Router, protected ref: NbDialogRef<BuildDialogComponent>) { }

  ngOnInit() {
  }

  onSubmit() {
    this.build.applicationId = this.application.id;

    this.applicationControllerService.buildUsingPOST({
      build: this.build,
      applicationFamily: this.application.applicationFamily,
      applicationId: this.application.id,
    }).subscribe(
      x => {
        this.buildTriggered.emit(true);
        this.ref.close();
      },
    );
  }
}
