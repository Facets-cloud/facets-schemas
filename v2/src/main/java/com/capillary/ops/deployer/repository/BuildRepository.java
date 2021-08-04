package com.capillary.ops.deployer.repository;

import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.PromotionIntent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

    public interface BuildRepository extends MongoRepository<Build, String> {

    List<Build> findByApplicationIdOrderByTimestampDesc(String applicationId);

    Optional<Build> findOneByApplicationIdAndId(String applicationId, String buildId);

    /**
     * Find Latest Production Build
     * <p>
     * Latest Promoted
     *
     * @param applicationId Id of Application
     * @return Build Object
     */
    Optional<Build> findFirstByApplicationIdAndPromotedIsTrueOrderByTimestampDesc(String applicationId);

    /**
     * Find Latest Staging Build
     * <p>
     * Latest Promotable
     *
     * @param applicationId Id of Application
     * @return Build Object
     */
    Optional<Build> findFirstByApplicationIdAndPromotableIsTrueOrderByTimestampDesc(String applicationId);

    /**
     * Find Latest Test Build
     * <p>
     * Non Promotable build.
     *
     * @param applicationId Id of Application
     * @return Build Object
     */
    List<Build> findFirst20ByApplicationIdAndPromotableIsFalseAndPromotedIsFalseAndTestBuildIsFalseOrderByTimestampDesc(
        String applicationId);

    Optional<Build> findFirstByApplicationIdAndPromotedIsTrueAndPromotionIntentOrderByTimestampDesc(
        String applicationId, PromotionIntent hotfix);

    List<Build> findBuildDistinctByTestBuildAndTimestampGreaterThan(Boolean testBuild, Date timestamp);

    List<String> findApplicationIdDistinctByTestBuildAndTimestampGreaterThan(Boolean testBuild, Date timestamp);

    Integer countBuildByApplicationIdAndTimestampBetween(
            String applicationId, Long periodEndDate, Long periodStartDate);

    List<Build> findFirst20ByApplicationIdAndPromotedIsTrueAndPromotionIntentOrderByTimestampDesc(String applicationId, PromotionIntent release);

    List<Build> findFirst20ByApplicationIdAndPromotedIsTrueOrderByTimestampDesc(String applicationId);

    List<Build> findFirst20ByApplicationIdAndPromotableIsTrueOrderByTimestampDesc(String applicationId);

    Optional<Build> findOneByCodeBuildId(String codeBuildId);

    List<Build> findFirst50ByApplicationIdOrderByTimestampDesc(String id);

    Integer countByApplicationIdAndTimestampGreaterThanAndTestBuildIsFalse(String applicationId, Long timestamp);

}
