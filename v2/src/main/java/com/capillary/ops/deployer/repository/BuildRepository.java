package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.Build;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BuildRepository extends MongoRepository<Build, String> {

    List<Build> findByApplicationIdOrderByTimestampDesc(String applicationId);

    Optional<Build> findOneByApplicationIdAndId(String applicationId, String buildId);

    /**
     * Find Latest Production Build
     *
     * Latest Promoted
     *
     * @param applicationId Id of Application
     * @return Build Object
     */
    Optional<Build> findFirstByApplicationIdAndPromotedIsTrueOrderByTimestampDesc(String applicationId);


    /**
     * Find Latest Staging Build
     *
     * Latest Promotable
     *
     * @param applicationId Id of Application
     * @return Build Object
     */
    Optional<Build> findFirstByApplicationIdAndPromotableIsTrueOrderByTimestampDesc(String applicationId);

    /**
     * Find Latest Test Build
     *
     * Non Promotable build.
     *
     * @param applicationId Id of Application
     * @return Build Object
     */
    Optional<Build> findFirstByApplicationIdAndPromotableIsFalseAndPromotedIsFalseOrderByTimestampDesc(String applicationId);

}
