import { ErrorHandler, Injectable, Injector, Inject} from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Router, ɵROUTER_PROVIDERS } from '@angular/router';
import { DOCUMENT } from '@angular/common';
@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  constructor(@Inject(DOCUMENT) private document: any) { }
  handleError(error) {
    if (error) {
      if (error instanceof HttpErrorResponse) {
        if ((<HttpErrorResponse> error).status === 401) {
          this.document.location.href = '/oauth2/authorization/google';
        }
      }
      console.log(error);
      throw error;
    }
  }

}
