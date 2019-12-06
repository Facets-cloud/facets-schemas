/* tslint:disable */
import { StackTraceElement } from './stack-trace-element';
export interface Throwable {
  cause?: Throwable;
  localizedMessage?: string;
  message?: string;
  stackTrace?: Array<StackTraceElement>;
  suppressed?: Array<Throwable>;
}
