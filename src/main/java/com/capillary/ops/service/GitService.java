package com.capillary.ops.service;

import com.capillary.ops.bo.Application;
import com.capillary.ops.bo.Deployment;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

@Service
public class GitService {

    @Autowired
    private DeploymentMongoService deploymentMongoService;

    @Async
    public void pushToDeis(Application application, Deployment deployment) {
        try {
            File workingDir = Files.createTempDirectory(String.format("%s_%s_%s", application.getName(),
                    deployment.getEnvironment().name(), deployment.getTag())).toFile();
            TransportConfigCallback transportConfigCallback = new SshTransportConfigCallback(application);
            Git git = Git.cloneRepository()
                    .setDirectory(workingDir)
                    .setTransportConfigCallback(transportConfigCallback)
                    .setURI(application.getRepoURL())
                    .call();
            git.checkout().setName(deployment.getTag()).call();
            git.branchCreate().setName(deployment.getId()).call();
            git.checkout().setName(deployment.getId()).call();
            git.remoteAdd().setName("deis").setUri(new URIish(
                    String.format("%s/%s.git",
                            deployment.getEnvironment().getDeisGitUri(),
                            deployment.getEnvironment().generateAppName(application.getName())))
                ).call();
            Iterable<PushResult> pushResults = git.push().setTimeout(3600).setTransportConfigCallback(transportConfigCallback)
                    .setForce(true).setRemote("deis").call();
            deployment.setPushResult(pushResults.iterator().next().getMessages());
            deployment.setStatus(Deployment.Status.FINISHED);
            deploymentMongoService.update(deployment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static class SshTransportConfigCallback implements TransportConfigCallback {
        private final SshSessionFactory sshSessionFactory;

        public SshTransportConfigCallback(Application application) {
            sshSessionFactory = new JschConfigSessionFactory() {
                @Override
                protected void configure(OpenSshConfig.Host hc, Session session) {
                    session.setConfig("StrictHostKeyChecking", "no");
                    try {
                        session.setTimeout(3600);
                    } catch (JSchException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                protected JSch createDefaultJSch(FS fs) throws JSchException {
                    JSch jSch = super.createDefaultJSch(fs);
                    jSch.addIdentity(application.getName(),
                            application.getPrivateKey().getBytes(),
                            application.getPublicKey().getBytes(), "".getBytes());
                    return jSch;
                }
            };
        }

        @Override
        public void configure(Transport transport) {
            SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(sshSessionFactory);
        }
    }
}
