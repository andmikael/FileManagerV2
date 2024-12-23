import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { getActiveConsumer } from '@angular/core/primitives/signals';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { firstValueFrom, Observable } from 'rxjs';

@Component({
  selector: 'app-containers',
  imports: [RouterModule, CommonModule],
  templateUrl: './containers.component.html',
  styleUrl: './containers.component.css'
})
export class ContainersComponent implements OnInit{

  receivedData: any

  constructor(private route: ActivatedRoute) {}
  http: HttpClient = inject(HttpClient);

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
}
