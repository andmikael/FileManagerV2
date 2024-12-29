import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, inject, OnInit, Output } from '@angular/core';
import { getActiveConsumer } from '@angular/core/primitives/signals';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { BehaviorSubject, catchError, first, firstValueFrom, fromEvent, lastValueFrom, map, Observable, take, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ContainerService } from '../../services/container.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-containers',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './containers.component.html',
  styleUrl: './containers.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ContainersComponent {

  receivedData$: any
  selectedContainer: any;

  @Output() EmitContainerString: EventEmitter<JSON> = new EventEmitter<JSON>();


  constructor(private route: ActivatedRoute,
    private readonly containerService: ContainerService,
    private readonly userService: UserService
  ) {
    this.receivedData$ = this.containerService.containers$;
    this.populateList();
  }

  http: HttpClient = inject(HttpClient);
  router: Router = inject(Router);

  loadContainers() {
    this.containerService.getContainers()
    .pipe(
      take(1),
        tap(() => {
        }),
      )
      .subscribe();
  }

  populateList() {
    this.loadContainers();
    this.receivedData$.pipe().subscribe((item: any) => {
      if (item) {
        this.selectedContainer = item[0]
      } else {
        this.selectedContainer = "";
      }
    })
    /*this.receivedData$.pipe(first()).subscribe((item: string | any[]) => 
    {
      console.log(item)
      if (item && item.length > 0) {
        this.selectedContainer = item[0];
      } else {
        this.selectedContainer = "";
      }
    }
    )*/
  }

  selectContainer() {
    this.http.post(`${environment.apiUrl}`+"/api/containers/selectcontainer", this.selectedContainer)
    .subscribe({
      next: (response) => {
        this.navigateToIndex();
      },
      error: (error) => { 
        console.error("error while selecting container: " + error);
      }
    });
  }

  deleteContainer() {
    this.http.post(`${environment.apiUrl}`+"/api/containers/deletecontainer", this.selectedContainer, {responseType: 'text', observe: 'response'})
    .subscribe({
      next: (response) => {
        this.populateList();
      },
      error: (error) => { 
        console.error("error whilwe deleting container: " + error);
      }
    });
  }

  onCreateSubmit(containerForm: NgForm) {
    this.http.post(`${environment.apiUrl}`+"/api/containers/createcontainer", containerForm.value["container-name"], {responseType: 'text', observe: 'response'})
    .subscribe({
      next: (response) => {
        if (response.status == 201) {
          this.navigateToIndex();
        }
      },
      error: (error) => {
        console.log("error while creating container: "+error);
      }
    })
  }

  navigateToIndex() {
    this.router.navigate(['/index']);
  }
}
