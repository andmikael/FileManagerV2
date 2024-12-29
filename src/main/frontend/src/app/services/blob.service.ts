import { Injectable } from "@angular/core";
import { BehaviorSubject, tap } from "rxjs";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { BlobsModel } from "../models/api.model";

@Injectable({
    providedIn: 'root',
})

/*interface BlobList {
    blobNames: []
}*/

export class BlobService {

    readonly blobs$: BehaviorSubject<BlobsModel | null> =
        new BehaviorSubject<BlobsModel | null>(null);
    constructor(
        private http: HttpClient,
    ) {}

    setBlobs(blobs: any | null): void {
        // kind of a workaround
        let blobsObject: Map<String, any> = new Map(Object.entries(blobs))
        this.blobs$.next(blobsObject.get("blobs"));
        //this.blobs$.next(blobs);
    }

    getBlobs() {
        return this.http.get<BlobsModel>(`${environment.apiUrl}`+"/api/index/").pipe(
            tap((blobs) => this.setBlobs(blobs)));
    }
}