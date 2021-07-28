import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProvidedResourcesComponent } from './provided-resources.component';
import { NbAccordionModule, NbButtonModule, NbCardModule, NbInputModule } from '@nebular/theme';
import { LayoutsModule } from 'src/app/layouts/layouts.module';
import { FormsModule } from '@angular/forms';
import { ComponentsModule } from 'src/app/components/components.module';



@NgModule({
  declarations: [ProvidedResourcesComponent],
  imports: [
    CommonModule,
    NbCardModule,
    LayoutsModule,
    NbInputModule,
    FormsModule,
    NbAccordionModule,
    ComponentsModule,
    NbButtonModule,
  ]
})
export class ProvidedResourcesModule { }
