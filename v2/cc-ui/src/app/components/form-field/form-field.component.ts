import {Component, Input, OnInit} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from "@angular/forms";

const noop = () => {
};

@Component({
  selector: 'app-form-field',
  templateUrl: './form-field.component.html',
  styleUrls: ['./form-field.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: FormFieldComponent,
    multi: true
  }]
})
export class FormFieldComponent implements OnInit, ControlValueAccessor {
  @Input("name")
  fieldId: string;
  innerValue: string;
  @Input("patternMatch")
  patternMatch: string;
  @Input("pattern")
  pattern: string;
  @Input("missingMsg")
  missingMsg: string;
  @Input("invalidMsg")
  invalidMsg: string;
  @Input("placeholder")
  placeholder: string;
  @Input("label")
  label: string;
  @Input("tooltipText")
  tooltipText: string;

  @Input("required")
  required: boolean;
  @Input("disabled")
  disabled: boolean;
  @Input("minlength")
  minlength: number
  @Input("maxlength")
  maxlength: number
  @Input("select")
  select: boolean = false;
  @Input("selectValues")
  selectValues: { value: string; label: string }[];

  @Input("multiple")
  multiple: boolean = false;
  @Input("optional")
  optional: boolean;
  @Input("type")
  type: string = "text";

  @Input("full")
  full: boolean;

  offset = 2;
  inputSize = 5;
  labelSize = 3;

  constructor() {
  }

  ngOnInit(): void {
    this.invalidMsg = this.invalidMsg || this.label + " is invalid!"
    this.missingMsg = this.missingMsg || this.label + " is required!";
    this.placeholder = this.placeholder || this.label;
    if (this.full) {
      this.offset = 0;
      this.inputSize = 7;
      this.labelSize = 5;
    }
  }

  /**
   * Invoked when the model has been changed
   */
  onChange: (_: any) => void = noop;

  /**
   * Invoked when the model has been touched
   */
  onTouched: () => void = noop;

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  writeValue(value: any): void {
    if (value != this.innerValue) {
      this.innerValue = value
      this.onChange(this.multiple && !this.select ? this.value.split(",") : this.value);
    }
  }

  //get accessor
  get value(): any {
    return this.innerValue;
  };

  //set accessor including call the onchange callback
  set value(v: any) {
    if (v !== this.innerValue) {
      this.innerValue = v;
      this.onChange(v);
    }
  }


  setDisabledState(disabled: boolean) {
    this.disabled = disabled;
  }
}
