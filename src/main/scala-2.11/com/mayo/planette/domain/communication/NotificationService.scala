package com.mayo.planette.domain.communication

import scala.concurrent.Future

/**
 * @author yoav @since 5/8/16.
 */
trait NotificationService {

  type Notification
  type ClientId


  def sendNotificationToClient(clientId: ClientId, notification: Notification): Future[Unit]
}
