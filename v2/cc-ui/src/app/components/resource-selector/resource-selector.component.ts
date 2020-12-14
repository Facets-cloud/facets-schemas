import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {Observable, of} from "rxjs";
import {UiStackControllerService} from "../../cc-api/services/ui-stack-controller.service";
import {map} from "rxjs/operators";
import {NbAutocompleteComponent} from "@nebular/theme/components/autocomplete/autocomplete.component";

@Component({
  selector: 'app-resource-selector',
  templateUrl: './resource-selector.component.html',
  styleUrls: ['./resource-selector.component.scss']
})
export class ResourceSelectorComponent implements OnInit {

  resourceName: string;
  filteredOptions$: Observable<string[]>;
  resources: string[];

  @ViewChild('resourceInput') input;

  @Input()
  stackName: string;

  @Input()
  resourceTypeSelected: string;

  @Output()
  onResourceSelect: EventEmitter<string> = new EventEmitter<string>();

  constructor(private u: UiStackControllerService) {
  }

  ngOnInit(): void {
    this.initializeResourceComponent(this.resourceTypeSelected);
  }

  public initializeResourceComponent(resourceTypeSelected: string) {
    this.resourceName = "";
    this.u.getResourcesByTypesUsingGET({stackName: this.stackName, resourceType: resourceTypeSelected}).subscribe(
      (x: Array<string>) => {
        this.resources = x;
        this.filteredOptions$ = of(this.resources);
      }
    )
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

  onChange() {
    this.filteredOptions$ = this.getFilteredOptions(this.input.nativeElement.value, this.resources);
  }

  onSelectionChange($event) {
    this.filteredOptions$ = this.getFilteredOptions($event, this.resources);
    this.onResourceSelect.emit(this.resourceName);

  }
}
