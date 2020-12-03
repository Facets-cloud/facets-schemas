import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {Observable, of} from "rxjs";
import {map} from "rxjs/operators";
import {ResourceSelectorComponent} from "../resource-selector/resource-selector.component";
import {UiStackControllerService} from "../../cc-api/services/ui-stack-controller.service";

@Component({
  selector: 'app-resource-type-selector',
  templateUrl: './resource-type-selector.component.html',
  styleUrls: ['./resource-type-selector.component.scss']
})
export class ResourceTypeSelectorComponent implements OnInit {
  filteredOptionsRT$: Observable<string[]>;
  resourceType: string;
  resourceTypes: string[];
  @ViewChild('resourceTypeInput') inputRT;
  @Input()
  stackName: string;
  @Output()
  onResourceTypeSelect: EventEmitter<string> = new EventEmitter<string>();

  @Input()
  resourceSelectorComponent: ResourceSelectorComponent;

  constructor(private u: UiStackControllerService) {
  }

  ngOnInit(): void {
    this.u.getResourceTypesUsingGET(this.stackName).subscribe(
      (x: Array<string>) => {
        this.resourceTypes = x;
        this.filteredOptionsRT$ = of(this.resourceTypes);
      }
    )
  }

  onChangeRT() {
    this.filteredOptionsRT$ = this.getFilteredOptions(this.inputRT.nativeElement.value, this.resourceTypes);
  }

  onSelectionChangeRT($event) {
    this.filteredOptionsRT$ = this.getFilteredOptions($event, this.resourceTypes);
    this.resourceSelectorComponent.initializeResourceComponent(this.resourceType);
    this.onResourceTypeSelect.emit(this.resourceType);
  }

  private filter(value: string, list: string[]): string[] {
    const filterValue = value.toLowerCase();

    return list.filter(optionValue => optionValue.toLowerCase().includes(filterValue));
  }

  getFilteredOptions(value: string, list: string[]): Observable<string[]> {
    if (!list || list.length == 0) {
      return;
    }
    return of(value).pipe(
      map(filterString => this.filter(filterString, list)),
    );
  }
}
