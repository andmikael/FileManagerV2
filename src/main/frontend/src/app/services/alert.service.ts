import { Injectable, signal } from "@angular/core";
import { Observable, Subject } from "rxjs";
import { AlertInterface, ApiError } from "../models/api.model";

@Injectable({
    providedIn: 'root'
})

export class AlertService {
    private errorSig = signal<null | ApiError>(null);

    setAlert(alert: ApiError) {
        this.errorSig.set(alert);
    }

    getAlert() {
        return this.errorSig;
    }
}