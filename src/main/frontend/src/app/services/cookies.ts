import { DOCUMENT, } from '@angular/common';
import {
  Inject,
  Injectable,
  Optional,
} from '@angular/core';

@Injectable({
  providedIn: 'root',
})

// Cookie service used for retrieving cookie from the document
// could be changed to get the cookie from Set-Cookoie header from the response after login
export class CookieService {
  constructor(
    @Optional() @Inject(DOCUMENT) private doc: Document
  ) {}

  get(): string {
    return this.doc.cookie;
  }
}