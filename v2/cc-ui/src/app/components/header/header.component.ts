import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  isClusterContext = true;

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      if (!p.clusterId) {
        console.log(p);
        this.isClusterContext = false;
      }
    });
  }

}
