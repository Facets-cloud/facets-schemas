package com.capillary.ops.deployer.service.capillaryCloud;

import com.capillary.ops.deployer.bo.capillaryCloud.Cluster;
import com.capillary.ops.deployer.bo.capillaryCloud.ProcessExecutionResult;
import com.capillary.ops.deployer.service.KubernetesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TerraformEnvironment implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(TerraformEnvironment.class);

    private static String OS = System.getProperty("os.name").toLowerCase();

    private final Cluster cluster;
    private String tempPath;

    public TerraformEnvironment(Cluster cluster) {
        this.cluster = cluster;
        tempPath = getTempPath();
        createWorkingDirectory();
        generateTFVars();
        runCommand("terraform version");
        runCommand("terraform init");
        runCommand("terraform workspace new " + cluster.getName());
        runCommand("terraform workspace select " + cluster.getName());
        runCommand("terraform workspace show");
    }

    private void generateTFVars() {
        try {
            new ObjectMapper().writeValue(new File(tempPath + "/tf/terraform.tfvars.json"), cluster);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ProcessExecutionResult plan() {
        return runCommand("terraform plan -input=false -no-color");
    }

    public ProcessExecutionResult apply() {
        return runCommand("terraform apply -input=false -no-color -auto-approve");
    }

    public ProcessExecutionResult destroy() {
        return runCommand("terraform destroy -no-color -auto-approve");
    }

    public ProcessExecutionResult runCommand(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(new String[]{"/bin/bash", "-c", command});
            processBuilder.directory(new File(tempPath + "/tf"));
            String newPath = tempPath + "/bin/" + getPlatform() + "/:" + processBuilder.environment().get("PATH");
            processBuilder.environment().put("PATH", newPath);
            Process process = processBuilder.start();
            process.waitFor();
            String stdOut = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
            String errOut = IOUtils.toString(process.getErrorStream(), StandardCharsets.UTF_8);
            logger.info(stdOut);
            logger.error(errOut);
            String stdOutBase64 = new BASE64Encoder().encode(stdOut.getBytes());
            String errBase64 = new BASE64Encoder().encode(errOut.getBytes());
            return new ProcessExecutionResult(process.exitValue(), stdOutBase64, errBase64);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public void close() {
        if (new File(tempPath).exists()) {
            new File(tempPath).delete();
        }
    }

    private void createWorkingDirectory() {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:/bin/" + getPlatform() + "/**");
            for (Resource resource : resources) {
                if (resource.exists() & resource.isReadable() && resource.contentLength() > 0) {
                    String url = resource.getURL().getPath();
                    String substring = url.substring(url.indexOf("/bin/"));
                    File destination = new File(tempPath + "/" + substring);
                    FileUtils.copyURLToFile(resource.getURL(), destination);
                    destination.setExecutable(true);
                } else {
                }
            }

            resources = resolver.getResources("classpath*:/tf/**");

            for (Resource resource : resources) {
                if (resource.exists() & resource.isReadable() && resource.contentLength() > 0) {
                    String url = resource.getURL().getPath();
                    String substring = url.substring(url.indexOf("/tf/"));
                    File destination = new File(tempPath + "/" + substring);
                    FileUtils.copyURLToFile(resource.getURL(), destination);
                } else {
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private String getTempPath() {
        return System.getProperty("java.io.tmpdir")
                + "/" + cluster.getName()
                + System.currentTimeMillis();
    }

    private String getPlatform() {
        String platform = "osx";
        if (OS.toLowerCase().contains("linux") || OS.toLowerCase().contains("nix")) {
            platform = "linux";
        }
        return platform;
    }
}
