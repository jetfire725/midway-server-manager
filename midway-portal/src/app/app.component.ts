import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ConfigService } from './config.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'midway-portal';
  response = false;

  constructor(private configService: ConfigService){}

  ngOnInit(){
    this.setServerProperty()
  }

  public setServerProperty(): void {
    this.configService.setServerProperty("server-ip", "123").subscribe(
      (response: boolean) => {
        this.response = response;
        console.log(response)
      }, 
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    )
  }
  
}
