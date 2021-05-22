import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({providedIn: 'root'})
export class ConfigService {

    private serverAddress=environment.serverAddress

    constructor(private http: HttpClient) { }

    public getResponse(): Observable<boolean> {
        return this.http.get<boolean>(`${this.serverAddress}/start`)
    }
}


