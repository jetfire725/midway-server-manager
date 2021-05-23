import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({providedIn: 'root'})
export class ConfigService {

    private serverAddress=environment.serverAddress

    constructor(private http: HttpClient) { }

    public setServerProperty(property: string, value: string): Observable<boolean> {
        return this.http.post<boolean>(`${this.serverAddress}/setProperty`, {"property": property, "value": value})
    }
}


