import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClusterTunnelComponent } from './cluster-tunnel.component';
import {NbCardModule, NbIconModule, NbLayoutModule, NbTabsetModule} from "@nebular/theme";
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
        NbCardModule,
        NbIconModule,
    ]
})
export class ClusterTunnelModule { }
