import { Component, OnInit, OnChanges, SimpleChanges, Input, Output, EventEmitter, ViewChild, TemplateRef } from '@angular/core';
import { Application, Build } from '../../../api/models';
import { NgForm } from '@angular/forms';
import { ApplicationControllerService } from '../../../api/services';
import { NbDialogService, NbDialogRef, NbPopoverDirective, NbTrigger, NbPosition, NbMenuItem, NbMenuService } from '@nebular/theme';
import { Router } from '@angular/router';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'build-dialog',
  templateUrl: './build-dialog.component.html',
  styleUrls: ['./build-dialog.component.scss']
})
export class BuildDialogComponent implements OnInit, OnChanges {

  @Input() application: Application;

  @Output() buildTriggered: EventEmitter<boolean> = new EventEmitter<boolean>();

  @ViewChild(NbPopoverDirective, { static: false }) popover: NbPopoverDirective;
  @ViewChild('searchResults', { read: TemplateRef, static: false }) templateList: TemplateRef<any>;

  build: Build = {};
  branches: string[];
  tags: string[];
  filteredBranches: NbMenuItem[] = [];
  trigger = NbTrigger.NOOP;
  position = NbPosition.BOTTOM;
  adjustment = 'noop';
  branchloadingFailed = false;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.application.currentValue) {
    }
  }

  constructor(private applicationControllerService: ApplicationControllerService,
    private router: Router, protected ref: NbDialogRef<BuildDialogComponent>,
    protected menu: NbMenuService) { }

  async ngOnInit() {
    try {
      this.branches = await this.applicationControllerService.getApplicationBranchesUsingGET({
        applicationFamily: this.application.applicationFamily,
        applicationId: this.application.id,
      }).toPromise();
      this.tags = await this.applicationControllerService.getApplicationTagsUsingGET({
        applicationFamily: this.application.applicationFamily,
        applicationId: this.application.id,
      }).toPromise();
      this.searchBranch('');
      this.popover.show();
    } catch (e) {
      this.branchloadingFailed = true;
    }
    this.menu.onItemClick().subscribe(
      x => {
        this.build.tag = x.item.title;
        this.build.promotable = (x.item.parent.title == "tags")
        this.popover.hide();
      },
    );
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

  searchBranch(query: any) {
    const filteredBranches: NbMenuItem[] = this.branches.filter(x => x.startsWith(query)).map(
      x => {
        return {
          title: x,
        };
      },
    );

    const filteredTags: NbMenuItem[] = this.tags.filter(x => x.startsWith(query)).map(
      x => {
        return {
          title: x,
        };
      },
    );

    this.filteredBranches = [
      {
        title: 'branches',
        group: false,
        children: filteredBranches,
        expanded: true,
      },
      {
        title: 'tags',
        group: false,
        children: filteredTags,
        expanded: true,
      }];

    this.popover.show();
  }
}
