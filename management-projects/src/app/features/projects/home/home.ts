import { Component } from '@angular/core';
import { ProjectList } from "../project-list/project-list";

@Component({
  selector: 'app-home',
  imports: [ProjectList],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {

}
