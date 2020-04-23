package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.BuildStrategy;
import com.capillary.ops.cp.facade.ClusterFacade;
import com.capillary.ops.deployer.bo.*;
import io.fabric8.kubernetes.api.model.EnvVar;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CCAdapterService {

    @Autowired
    private ClusterFacade clusterFacade;

    public List<EnvironmentMetaData> getCCEnvironmentMetaData(ApplicationFamily applicationFamily) {
        List<AbstractCluster> clustersByStackName =
            clusterFacade.getClustersByStackName(applicationFamily.name().toLowerCase());

        return clustersByStackName.stream().map(c -> {
            EnvironmentMetaData environmentMetaData = new EnvironmentMetaData();
            environmentMetaData.setCapillaryCloudClusterName(c.getId());
            environmentMetaData.setName(c.getName());
            environmentMetaData.setApplicationFamily(applicationFamily);
            environmentMetaData.setCapCloud(true);
            environmentMetaData.setEnvironmentType(
                c.getReleaseStream().equals(BuildStrategy.QA) ? EnvironmentType.QA : EnvironmentType.PRODUCTION);
            return environmentMetaData;
        }).collect(Collectors.toList());
    }

    public Deployment getCCDeployment(ApplicationFamily applicationFamily, String applicationId, String environment) {
        io.fabric8.kubernetes.api.model.apps.Deployment ccDeployment =
            clusterFacade.getApplicationData(environment, "deployerid", applicationId);
        if (ccDeployment != null) {
            Deployment d = new Deployment();
            d.setApplicationFamily(applicationFamily);
            d.setApplicationId(applicationId);
            d.setBuildId(ccDeployment.getSpec().getTemplate().getSpec().getContainers().get(0).getImage());
            d.setEnvironment(environment);
            d.setDeployedBy("Cap Cloud");
            d.setImage(ccDeployment.getSpec().getTemplate().getSpec().getContainers().get(0).getImage());
//            d.setHorizontalPodAutoscaler(ccDeployment.);
            List<EnvVar> env = ccDeployment.getSpec().getTemplate().getSpec().getContainers().get(0).getEnv();
            List<EnvironmentVariable> envs =
                env.stream().map(e -> new EnvironmentVariable(e.getName(), e.getValue())).collect(Collectors.toList());
            d.setConfigurations(envs);
            HPA hpa = new HPA(-1,-1,-1);
            d.setHorizontalPodAutoscaler(hpa);
            SimpleDateFormat sdfmt= new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            try {
                Date parse = sdfmt.parse(ccDeployment.getMetadata().getCreationTimestamp());
                d.setTimestamp(parse);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return d;
        }
        return null;
    }

//    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat sdfmt= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        Date parse = sdfmt.parse("2020-12-31T23:55:55Z");
//        System.out.println(parse);
//    }

}
