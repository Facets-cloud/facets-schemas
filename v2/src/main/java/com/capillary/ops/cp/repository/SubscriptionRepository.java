package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.Artifact;
import com.capillary.ops.cp.bo.BuildStrategy;
import com.capillary.ops.cp.bo.notifications.NotificationType;
import com.capillary.ops.cp.bo.notifications.Subscription;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    List<Subscription> findByStackNameAndNotificationTypeAndNotificationSubject(String stackId, NotificationType notificationType, String notificationSubject);
    List<Subscription> findByStackName(String stackName);
}