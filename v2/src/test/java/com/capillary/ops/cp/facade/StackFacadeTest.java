package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.repository.AuditLogRepository;
import com.capillary.ops.cp.repository.ClusterTaskRepository;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.cp.repository.SubstackRepository;
import com.capillary.ops.cp.service.GitService;
import com.capillary.ops.cp.service.StackAutoCompleteService;
import com.capillary.ops.cp.service.StackService;
import com.capillary.ops.cp.service.notification.FlockNotifier;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class StackFacadeTest {

    @Tested
    StackFacade stackFacade;

    @Injectable
    StackRepository stackRepository;

    @Injectable
    SubstackRepository substackRepository;

    @Injectable
    GitService gitService;

    @Injectable
    StackAutoCompleteService stackAutoCompleteService;

    @Injectable
    private AuditLogRepository auditLogRepository;

    @Injectable
    private FlockNotifier flockNotifier;

    @Injectable
    private StackService stackService;

    @Injectable
    private ArtifactFacade artifactFacade;

    @Injectable
    private ClusterFacade clusterFacade;

    @Injectable
    private MetaFacade metaFacade;

    @Injectable
    private ClusterTaskRepository clusterTaskRepository;

   // @Test(expected = IllegalStateException.class)
    public void createStackNonUnique() {
        Stack s = new Stack();
        s.setName("NonUnique");
        new Expectations() {

            {
                stackRepository.findById(anyString);
                result = Optional.of(s);
            }
        };
        stackFacade.createStack(s);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createStackWrongGithub() throws GitAPIException {
        Stack s = new Stack();
        s.setName("Unique");
        new Expectations() {

            {
                stackRepository.findById(anyString);
                result = Optional.empty();
            }

            {
                gitService.checkout(anyString, anyString, anyString);
                result = new JGitInternalException("");
            }
        };
        stackFacade.createStack(s);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createStackNoStack() throws GitAPIException, IOException {
        Stack s = new Stack();
        s.setName("Unique");

        File file = new File("/tmp/repos/aa");
        FileUtils.deleteDirectory(file);
        file.mkdirs();
        new Expectations() {

            {
                stackRepository.findById(anyString);
                result = Optional.empty();
            }

            {
                gitService.checkout(anyString, anyString, anyString);
                result = file.toPath();
            }
        };
        stackFacade.createStack(s);
    }

    @Test
    public void createStackWithStack() throws GitAPIException, IOException {
        Stack s = new Stack();
        s.setName("Unique");
        File stackPath = new File("/tmp/repos/aa/");
        FileUtils.deleteDirectory(stackPath);
        stackPath.mkdirs();
        File file = new File("/tmp/repos/aa/stack.json");
        String data = "{\"stackVariables\":{\"var1\":\"val1\"}}";
        Files.write(file.toPath(), data.getBytes());
        new Expectations() {

            {
                stackRepository.findById(anyString);
                result = Optional.empty();
            }

            {
                gitService.checkout(anyString, anyString, anyString);
                result = stackPath.toPath();
            }

            {
                stackRepository.save(s);
                result = s;
            }

        };
        Stack stack = stackFacade.createStack(s);
        assert stack.getName().equals(s.getName());
        System.out.println(stack.getStackVars());
        assert stack.getStackVars().size() == 1;
    }

    @Test(expected = IllegalArgumentException.class)
    public void createStackWithStackInvalidJson() throws GitAPIException, IOException {
        Stack s = new Stack();
        s.setName("Unique");
        File stackPath = new File("/tmp/repos/aa/");
        FileUtils.deleteDirectory(stackPath);
        stackPath.mkdirs();
        File file = new File("/tmp/repos/aa/stack.json");
        String data = "{\"var1\":\"val1\"";
        Files.write(file.toPath(), data.getBytes());
        new Expectations() {

            {
                stackRepository.findById(anyString);
                result = Optional.empty();
            }

            {
                gitService.checkout(anyString, anyString, anyString);
                result = stackPath.toPath();
            }
        };
        Stack stack = stackFacade.createStack(s);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createStackWithStackWrongJson() throws GitAPIException, IOException {
        Stack s = new Stack();
        s.setName("Unique");
        File stackPath = new File("/tmp/repos/aa/");
        FileUtils.deleteDirectory(stackPath);
        stackPath.mkdirs();
        File file = new File("/tmp/repos/aa/stack.json");
        String data = "{\"stackVariables\":\"val1\"}";
        Files.write(file.toPath(), data.getBytes());
        new Expectations() {

            {
                stackRepository.findById(anyString);
                result = Optional.empty();
            }

            {
                gitService.checkout(anyString, anyString, anyString);
                result = stackPath.toPath();
            }
        };
        try {
            Stack stack = stackFacade.createStack(s);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }
}