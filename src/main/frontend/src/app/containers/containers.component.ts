import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, inject, OnInit, Output } from '@angular/core';
import { getActiveConsumer } from '@angular/core/primitives/signals';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { firstValueFrom, fromEvent, Observable } from 'rxjs';

@Component({
  selector: 'app-containers',
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './containers.component.html',
  styleUrl: './containers.component.css'
})
export class ContainersComponent implements OnInit{

  receivedData: any
  selectedContainer: any;


  constructor(private route: ActivatedRoute) {
  }
  http: HttpClient = inject(HttpClient);
  router: Router = inject(Router);

  @Output() EmitContainerString: EventEmitter<JSON> = new EventEmitter<JSON>();

  ngOnInit(): void {
    this.getContainers()
    .then(data => {
      this.receivedData = data;
    }).catch(e => {
      console.log(e)
    })
  }

  async getContainers(): Promise<any> {
    try {
      return await firstValueFrom(
        this.http.get("http://localhost:8080/api/container"));
    } catch (e) {
      console.log(e);
    }
  }

  selectContainer() {  
      this.http.post("http://localhost:8080/api/selectcontainer", this.selectedContainer)
      .subscribe({
        next: (response) => {
          this.navigateToIndex();
        },
        error: (error) => { 
          console.error(error);
        }
      });
    }

  deleteContainer() {
    this.http.post("http://localhost:8080/api/deletecontainer", this.selectedContainer)
    .subscribe({
      next: (response) => {
      },
      error: (error) => { 
        console.error(error);
      }
    });
  }

  onCreateSubmit(containerForm: NgForm) {
    this.http.post("http://localhost:8080/api/createcontainer", containerForm.value["container-name"], {responseType: 'text', observe: 'response'})
    .subscribe({
      next: (response) => {
        if (response.status == 201) {
          this.navigateToIndex();
        }
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  navigateToIndex() {
    this.router.navigate(['/index']);
  }
}
