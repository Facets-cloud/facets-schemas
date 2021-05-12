package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.DeploymentContext;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.service.GitService;
import com.capillary.ops.deployer.bo.EcrTokenMap;
import com.capillary.ops.deployer.service.facade.ApplicationFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class VagrantFacade {


    @Autowired
    private ApplicationFacade applicationFacade;

    @Autowired
    private StackFacade stackFacade;

    @Autowired
    ClusterFacade clusterFacade;

    @Autowired
    private GitService gitService;

    public void getFiles(String clusterId, ZipOutputStream zipOutputStream) throws IOException, GitAPIException {
        AbstractCluster cluster = clusterFacade.getCluster(clusterId);
        String stackName = cluster.getStackName();
        Stack stack = stackFacade.getStackByName(stackName);
        //    EcrTokenMap ecrTokenMapping = applicationFacade.getEcrTokenMapping();
        EcrTokenMap ecrTokenMapping = new EcrTokenMap();
        ecrTokenMapping.setAwsAccountId("accId");
        ecrTokenMapping.setEcrToken("token");
        //TODO: get the ecr url as well
        String dockerLogin = "docker login -u AWS -p " + ecrTokenMapping.getEcrToken() +
                " https://" + ecrTokenMapping.getAwsAccountId() + ".dkr.ecr.us-west-1.amazonaws.com";
        this.writeToZip(new ByteArrayInputStream(dockerLogin.getBytes()), zipOutputStream, "dockerLogin.sh");
        DeploymentContext localDeploymentContext = stackFacade.getLocalDeploymentContext(stackName);
        ObjectMapper objectMapper = new ObjectMapper();
        this.writeToZip(new ByteArrayInputStream(objectMapper.writeValueAsBytes(localDeploymentContext)), zipOutputStream, "deploymentcontext.json");
        File file = ResourceUtils.getFile("classpath:VagrantFile");
        this.writeToZip(new FileInputStream(file), zipOutputStream, "VagrantFile");
        Path checkout = gitService.checkout(stack.getVcsUrl(), stack.getUser(), stack.getAppPassword());
        writeToZip(checkout.toFile(), zipOutputStream, stackName, true);
    }

    private void writeToZip(InputStream value, ZipOutputStream zipOutputStream, String fileName) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipEntry.setTime(System.currentTimeMillis());
        zipOutputStream.putNextEntry(zipEntry);
        StreamUtils.copy(value, zipOutputStream);
        value.close();
        zipOutputStream.closeEntry();
    }

    private void writeToZip(File file, ZipOutputStream zipOutputStream, String basePath, boolean isRoot) throws IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (isRoot) {
                    writeToZip(f, zipOutputStream, basePath, false);
                } else {
                    writeToZip(f, zipOutputStream, basePath + "/" + file.getName(), false);
                }
            }
            return;
        }
        ZipEntry zipEntry = new ZipEntry(basePath + "/" + file.getName());
        zipEntry.setTime(System.currentTimeMillis());
        zipOutputStream.putNextEntry(zipEntry);
        FileInputStream fileInputStream = new FileInputStream(file);
        StreamUtils.copy(fileInputStream, zipOutputStream);
        fileInputStream.close();
        zipOutputStream.closeEntry();
    }
}
