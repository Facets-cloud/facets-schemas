package com.capillary.ops.cp.service;

import mockit.Tested;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@RunWith(MockitoJUnitRunner.class)
public class GitServiceTest {

    @Tested
    private GitService gitService;

    @Test
    public void testCheckoutNoUser() throws GitAPIException, IOException {
        Path checkout = gitService.checkout("https://github.com/rr0hit/k8sdemo.git", "", "");
        File tmpDir = new File(checkout.toString());
        boolean exists = tmpDir.exists();
        assert exists;
        FileUtils.deleteDirectory(checkout.toFile());
    }
}
