package com.capillary.ops.service;

import com.capillary.ops.bo.Application;
import com.capillary.ops.bo.Deployment;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;
import org.eclipse.jgit.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class GitService {

  @Autowired private DeploymentMongoService deploymentMongoService;

  @Async
  public void pushToDeis(Application application, Deployment deployment) {
    try {
      File workingDir =
          Files.createTempDirectory(
                  String.format("%s_%s", application.getName(), deployment.getId()))
              .toFile();
      TransportConfigCallback transportConfigCallback = new SshTransportConfigCallback(application);
      Git git =
          Git.cloneRepository()
              .setDirectory(workingDir)
              .setTransportConfigCallback(transportConfigCallback)
              .setURI(application.getRepoURL())
              .call();
      git.checkout().setName(deployment.getTag()).call();
      git.fetch().setTransportConfigCallback(transportConfigCallback).call();
      git.branchCreate().setName(deployment.getId()).call();
      git.checkout().setName(deployment.getId()).call();

      if (!StringUtils.isEmptyOrNull(application.getProjectFolder())) {
        workingDir = new File(workingDir.getAbsolutePath() + "/" + application.getProjectFolder());
        git = Git.init().setDirectory(workingDir).call();
        git.add().addFilepattern(".").call();
        git.commit()
            .setCommitter("admin", "admin@deployer")
            .setAuthor("admin", "admin@deployer")
            .setMessage("adding subdir as a repo")
            .call();
      }
      git.remoteAdd()
          .setName("deis")
          .setUri(
              new URIish(
                  String.format(
                      "%s/%s.git",
                      deployment.getEnvironment().getDeisGitUri(),
                      deployment.getEnvironment().generateAppName(application.getName()))))
          .call();
      Iterable<PushResult> pushResults =
          git.push()
              .setTimeout(3600)
              .setTransportConfigCallback(transportConfigCallback)
              .setForce(true)
              .setRemote("deis")
              .call();
      String responseMessage = pushResults.iterator().next().getMessages();
      if (responseMessage != null && responseMessage.contains("deployed to Workflow")) {
        deployment.setPushResult(responseMessage);
        deployment.setStatus(Deployment.Status.FINISHED);
      } else {
        deployment.setPushResult(responseMessage);
        deployment.setStatus(Deployment.Status.FAILED);
      }
      deploymentMongoService.update(deployment);
    } catch (Exception e) {
      e.printStackTrace();
      deployment.setStatus(Deployment.Status.FAILED);
      deployment.setPushResult(e.getMessage() + " " + e.getStackTrace());
      deploymentMongoService.update(deployment);
    }
  }

  public List<String> listBranches(Application application) {
    return lsRemote(application)
        .stream()
        .filter(x -> x.contains("refs/heads"))
        .map(x -> x.replace("refs/heads", "origin"))
        .collect(Collectors.toList());
  }

  public List<String> listTags(Application application) {
    return lsRemote(application)
        .stream()
        .filter(x -> x.contains("refs/tags"))
        .map(x -> x.replace("refs/tags", "origin"))
        .collect(Collectors.toList());
  }

  private List<String> lsRemote(Application application) {
    try {
      TransportConfigCallback transportConfigCallback = new SshTransportConfigCallback(application);
      return Git.lsRemoteRepository()
          .setTransportConfigCallback(transportConfigCallback)
          .setRemote(application.getRepoURL())
          .call()
          .stream()
          .map(x -> x.getName())
          .collect(Collectors.toList());
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private static class SshTransportConfigCallback implements TransportConfigCallback {

    private final SshSessionFactory sshSessionFactory;

    public SshTransportConfigCallback(Application application) {
      sshSessionFactory =
          new JschConfigSessionFactory() {

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
              jSch.addIdentity(
                  application.getName(),
                  application.getPrivateKey().getBytes(),
                  application.getPublicKey().getBytes(),
                  "".getBytes());
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
