package com.capillary.ops.cp.repository;

import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.notifications.NotificationChannel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationChannelRepository extends MongoRepository<NotificationChannel, String> {


    Optional<NotificationChannel> findByName(String name);
}
