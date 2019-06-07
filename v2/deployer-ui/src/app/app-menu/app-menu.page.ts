import { Component, OnInit, Input } from '@angular/core';
import { AppMenuItem } from './appmenuitem';
import { NavController, PopoverController } from '@ionic/angular';

@Component({
  selector: 'app-app-menu',
  templateUrl: './app-menu.page.html',
  styleUrls: ['./app-menu.page.scss'],
})
export class AppMenuPage implements OnInit {

  @Input() menuItems: AppMenuItem[];

  constructor(private navController: NavController, private popoverController: PopoverController) { }

  ngOnInit() {
  }

  navigate(item: AppMenuItem) {
    this.popoverController.dismiss();
    if (item.modal) {
      item.modal.present();
    } else {
      this.navController.navigateForward(item.url);
    }
  }

}
