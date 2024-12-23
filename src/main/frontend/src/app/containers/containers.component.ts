import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component, EventEmitter, inject, OnInit, Output } from '@angular/core';
import { getActiveConsumer } from '@angular/core/primitives/signals';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { firstValueFrom, fromEvent, lastValueFrom, Observable } from 'rxjs';

@Component({
  selector: 'app-containers',
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './containers.component.html',
  styleUrl: './containers.component.css'
})
export class ContainersComponent implements OnInit{

  receivedData: String[] = [];
  selectedContainer: any;


  constructor(private route: ActivatedRoute) {
  }
  http: HttpClient = inject(HttpClient);
  router: Router = inject(Router);

  @Output() EmitContainerString: EventEmitter<JSON> = new EventEmitter<JSON>();

  ngOnInit(): void {
    if (this.receivedData.length == 0) {
      this.populateContainerList();
    } else {
      this.selectedContainer = this.receivedData[0];
    }
  }

  async getContainers(): Promise<any> {
    try {
      return lastValueFrom(
        this.http.get("http://localhost:8080/api/container"));
    } catch (e) {
      console.log("error while trying to get containers: " + e);
    }
  }

  async populateContainerList() {
    await this.getContainers()
    .then(data => {
      console.log(data);
      this.receivedData = data;
      if (this.receivedData.length > 0) {
        this.selectedContainer = this.receivedData[0];
      } else {
        this.selectedContainer = "";
      }
    }).catch(e => {
      console.log("error while trying to populate dropdownlist: " + e)
    })
  }

  selectContainer() {  
      this.http.post("http://localhost:8080/api/selectcontainer", this.selectedContainer)
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
    this.receivedData = this.receivedData.filter(element => element !== this.selectedContainer)
    this.http.post("http://localhost:8080/api/deletecontainer", this.selectedContainer, {responseType: 'text', observe: 'response'})
    .subscribe({
      next: (response) => {
        console.log(response);
      },
      error: (error) => { 
        console.error("error whilwe deleting container: " + error);
      }
    });
    if (this.receivedData.length > 0) {
      this.selectedContainer = this.receivedData[0];
    } else {
      this.selectedContainer = "";
    }
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
        console.log("error while creating container: "+error);
      }
    })
  }

  navigateToIndex() {
    this.router.navigate(['/index']);
  }
}
