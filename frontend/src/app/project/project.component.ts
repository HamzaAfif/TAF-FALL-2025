import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent {

  creationForm: any = {
    projectName: null,
    responsable: null,
    startDate: null,
    endDate: null
  };  

    loading = false;
    submitted = false;

    constructor(
        private router: Router
    ) {

    }

    // convenience getter for easy access to form fields
    get f() { return this.creationForm.controls; }

    onSubmit() {
        this.submitted = true;

        // stop here if form is invalid
        if (this.creationForm.invalid) {
            return;
        }

        this.loading = true;
    }
}
