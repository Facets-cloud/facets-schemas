/* tslint:disable */
import { Throwable } from './throwable';
import { StackTraceElement } from './stack-trace-element';
export interface Exception {
  cause?: Throwable;
  localizedMessage?: string;
  message?: string;
  stackTrace?: Array<StackTraceElement>;
  suppressed?: Array<Throwable>;
}
