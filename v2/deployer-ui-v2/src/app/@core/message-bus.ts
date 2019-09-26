import { Injectable } from "@angular/core";
import { Subject } from 'rxjs';

@Injectable()
export class MessageBus {
  deploymentTopic: Subject<any> = new Subject();
  buildTopic: Subject<any> = new Subject();
  application: Subject<any> = new Subject();
}
