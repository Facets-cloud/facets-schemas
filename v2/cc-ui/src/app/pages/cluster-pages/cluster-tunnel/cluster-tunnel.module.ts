import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterTunnelComponent } from './cluster-tunnel.component';
import {NbLayoutModule, NbTabsetModule} from "@nebular/theme";
import {ComponentsModule} from "../../../components/components.module";
import {LayoutsModule} from "../../../layouts/layouts.module";



@NgModule({
  declarations: [ClusterTunnelComponent],
  imports: [
    CommonModule,
    NbLayoutModule,
    ComponentsModule,
    NbTabsetModule,
    LayoutsModule,
  ]
})
export class ClusterTunnelModule { }
