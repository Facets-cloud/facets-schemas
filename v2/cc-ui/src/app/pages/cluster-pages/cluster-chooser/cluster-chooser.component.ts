import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-cluster-chooser',
  templateUrl: './cluster-chooser.component.html',
  styleUrls: ['./cluster-chooser.component.scss']
})
export class ClusterChooserComponent implements OnInit {
  stackName: any;

  constructor(private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(p => {
        this.stackName = p.stackName;
      }
    );
  }

}
