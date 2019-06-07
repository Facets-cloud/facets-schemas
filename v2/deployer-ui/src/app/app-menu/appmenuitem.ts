import { ComponentRef } from '@ionic/core';

export interface AppMenuItem {
  name: string;
  url: string;
  icon: string;
  modal: HTMLIonModalElement;
}
