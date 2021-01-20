import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";

@Component({
  selector: 'app-cluster-tunnel',
  templateUrl: './cluster-tunnel.component.html',
  styleUrls: ['./cluster-tunnel.component.scss']
})
export class ClusterTunnelComponent implements OnInit {
  wettyTunnel: String;
  grafanaTunnel: string = "http://localhost:4300";
  private clusterId: String;
  grafanaTunnelSafe: SafeUrl = this.sanitizer.bypassSecurityTrustUrl(this.grafanaTunnel.toString());

  constructor(private route: ActivatedRoute, private sanitizer: DomSanitizer
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
        if (p.clusterId) {
          this.clusterId = p.clusterId;
          this.grafanaTunnel = window.location.origin + "/tunnel/"+this.clusterId+"/grafana";
          this.grafanaTunnelSafe = this.makeSafe(this.grafanaTunnel);
        }
      }
    )
  }

  makeSafe(url:string){
    return this.sanitizer.bypassSecurityTrustResourceUrl(url)
  }
}
