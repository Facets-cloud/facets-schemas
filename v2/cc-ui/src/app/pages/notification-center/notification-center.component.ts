import {Component, OnInit} from '@angular/core';
import {UiNotificationControllerService} from "../../cc-api/services/ui-notification-controller.service";
import {NotificationChannel} from "../../cc-api/models/notification-channel";
import {Subscription} from "../../cc-api/models/subscription";
import {NbDialogService} from "@nebular/theme";
import {UiStackControllerService} from "../../cc-api/services/ui-stack-controller.service";

@Component({
  selector: 'app-notification-center',
  templateUrl: './notification-center.component.html',
  styleUrls: ['./notification-center.component.scss']
})
export class NotificationCenterComponent implements OnInit {
  channels: Array<NotificationChannel>;
  subscriptions: Array<Subscription>;

  subscriptionSettings: any = {
    columns: {
      stackName: {
        title: 'Stack',
        filter: false,
      },
      notificationType: {
        title: 'notification Type',
        filter: true,
      },
      channelName: {
        title: 'Channel Name',
        valuePrepareFunction: (value, row, cell) => {
          debugger
          if (!value) {
            return "Legacy Integration"
          }
          return value;
        }
      }
    },
    actions: {
      edit: false,
      delete: false,
      add: false,
      position: 'right',
      custom: [
        {name: 'Edit', title: '<i class="eva-edit-2-outline eva"></i>', type: 'html'},
        {name: 'Delete', title: '<i class="eva-trash-2-outline eva"></i>', type: 'html'}]
    },
  };

  channelsSettings: any = {
    columns: {
      channelType: {
        title: 'Type',
        filter: false,
      },
      name: {
        title: 'Channel Name',
        filter: true,
      },
      channelAddress: {
        title: 'URL',
        filter: false,

      }
    },
    actions: {
      edit: false,
      delete: false,
      add: false,
      position: 'right',
      custom: [
        {name: 'Edit', title: '<i class="eva-edit-2-outline eva"></i>', type: 'html'}]
    },
  };
  nc: NotificationChannel = {};
  sub: Subscription = {};
  notificationError: any = "";
  channelMap = {};
  channelOptions = []
  stackOptions = []
  notificationTypeOptions = []
  testingResult: string;

  constructor(private uiNotificationControllerService: UiNotificationControllerService,
              private dialogService: NbDialogService, private uiStackControllerService: UiStackControllerService) {
  }

  ngOnInit(): void {
    this.uiStackControllerService.getStacksUsingGET1().subscribe(
      res => {
        res.forEach(
          stack => {
            var option = {}
            option["value"] = stack.name
            option["label"] = stack.name
            this.stackOptions.push(option)
          }
        )
      }
    )
    this.uiNotificationControllerService.getAllNotificationTypesUsingGET().subscribe(
      res => {
        res.forEach(
          type => {
            var option = {}
            option["value"] = type
            option["label"] = type.replace("_", " ")
            this.notificationTypeOptions.push(option)
          }
        )
      }
    )
    this.uiNotificationControllerService.getAllChannelsUsingGET().subscribe(
      res => {
        this.populateChannels(res);
        this.uiNotificationControllerService.getAllSubscriptionsUsingGET().subscribe(
          res => {
            this.populateSubscriptions(res);
          }
        )
      }
    )

  }


  private populateSubscriptions(res: Array<Subscription>) {
    this.subscriptions = res
    this.subscriptions.forEach(s => {
        if (s.channelId) {
          s["channelName"] = this.channelMap[s.channelId]
        }
      }
    );
  }

  private populateChannels(res: Array<NotificationChannel>) {
    this.channelOptions = []
    this.channels = res
    this.channels.forEach(
      channel => {
        this.channelMap[channel.id] = channel.name
        var option = {}
        option["value"] = channel.id
        option["label"] = channel.name + " - " + channel.channelType
        this.channelOptions.push(option)
      }
    );
  }

  addNotificationChannel(ref: any, edit: any) {
    if (edit) {
      this.uiNotificationControllerService.editNotificationChannelUsingPUT({
        nc: this.nc,
        channelId: this.nc.id
      }).subscribe(
        response => {
          this.populateChannels(response);
          ref.close();
        },
        error => {
          this.notificationError = error.error.message;
        }
      )
    } else {
      this.uiNotificationControllerService.createNotificationChannelUsingPOST(this.nc).subscribe(
        response => {
          this.populateChannels(response);
          ref.close();
        },
        error => {
          this.notificationError = error.error.message;
        }
      )
    }
  }

  openPopup(addChannel) {
    this.notificationError = ""
    this.testingResult = ""
    this.sub = {}
    this.nc = {}
    this.dialogService.open(addChannel, {context: {edit: false}})
  }

  addSubscriptionCall(ref: any, edit) {
    if (edit) {
      this.uiNotificationControllerService.editSubscriptionUsingPUT({
        subscription: this.sub,
        subscriptionId: this.sub.id
      }).subscribe(
        response => {
          this.populateSubscriptions(response)
          ref.close();
        },
        error => {
          this.notificationError = error.error.message;
        }
      )

    } else {
      this.uiNotificationControllerService.createSubscriptionUsingPOST(this.sub).subscribe(
        response => {
          this.populateSubscriptions(response)
          ref.close();
        },
        error => {
          this.notificationError = error.error.message;
        }
      )
    }
  }


  testNotification() {
    this.uiNotificationControllerService.testNotificationChannelUsingPOSTResponse(
      this.nc
    ).subscribe(
      response => {
        if (response) {
          this.testingResult = "Test Notification succeeded"
        }
      },
      error => {
        this.notificationError = "Test Notification failed"
      }
    )
  }

  openEditPopup(dialog, $event) {
    this.notificationError = ""
    this.testingResult = ""
    this.nc = $event.data
    this.dialogService.open(dialog, {context: {edit: true}})
  }

  openEditPopupSubs(dialog, $event) {
    if($event.action == 'Edit') {
      this.notificationError = ""
      this.testingResult = ""
      this.sub = $event.data
      this.dialogService.open(dialog, {context: {edit: true}})
    }
    if($event.action == 'Delete') {
      this.uiNotificationControllerService.deleteSubscriptionUsingDELETE($event.data.id).subscribe(
        response => {
          this.populateSubscriptions(response)
        },
        error => {
          console.log(error)
        }
      )
    }
  }
}
