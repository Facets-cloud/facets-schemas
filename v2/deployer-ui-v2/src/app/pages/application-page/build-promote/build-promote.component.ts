import { Component, OnInit, OnChanges, SimpleChanges, Input, Output, EventEmitter, ViewChild, TemplateRef } from '@angular/core';
import { Application, Build } from '../../../api/models';
import { NgForm } from '@angular/forms';
import { ApplicationControllerService } from '../../../api/services';
import { NbDialogService, NbDialogRef, NbPopoverDirective, NbTrigger, NbPosition, NbMenuItem, NbMenuService } from '@nebular/theme';
import { Router } from '@angular/router';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'build-promote',
  templateUrl: './build-promote.component.html',
  styleUrls: ['./build-promote.component.scss']
})
export class BuildPromoteComponent implements OnInit, OnChanges {

  @Input() application: Application;

  @ViewChild(NbPopoverDirective, { static: false }) popover: NbPopoverDirective;

  @Input() build: Build = {};
  trigger = NbTrigger.NOOP;
  position = NbPosition.BOTTOM;
  adjustment = 'noop';
  promotionTypes = ['RELEASE','HOTFIX'];
  promotionIntent = "RELEASE";
  ngOnChanges(changes: SimpleChanges): void {
  }

  constructor(private applicationControllerService: ApplicationControllerService,
    private router: Router, protected ref: NbDialogRef<BuildPromoteComponent>,
    protected menu: NbMenuService) { }

  async ngOnInit() {
  }


  onSubmit() {
     this.applicationControllerService.updateBuildUsingPUT({
          applicationFamily: this.build.applicationFamily,
          applicationId: this.build.applicationId,
          build: { promoted: true,
           promotionIntent: this.promotionIntent
           },
          buildId: this.build.id,
        }).subscribe(
          x => {
            this.build.promoted = true;
            this.ref.close();
          },
        );
  }

}
