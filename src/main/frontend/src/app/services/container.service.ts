import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, map, take, tap } from "rxjs";
import { environment } from "../../environments/environment";
import { json } from "node:stream/consumers";

interface ContainerList {
    contNames: []
}

@Injectable({
    providedIn: 'root',
})

export class ContainerService {
    readonly containers$: BehaviorSubject<any|null> = new BehaviorSubject<any|null>([]);

    constructor(
        private http: HttpClient
    ) {}

    setList(list: any | null): void {
        let contObject: Map<String, ContainerList> = new Map(Object.entries(list))
        this.containers$.next(contObject.get("containers"));
    }

    getContainers() {
        return this.http.get(`${environment.apiUrl}`+"/api/containers/").pipe(
              take(1),
                tap((data) => {
                this.setList(data)}),
              )
    }
}