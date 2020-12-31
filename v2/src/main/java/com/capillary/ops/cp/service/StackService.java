package com.capillary.ops.cp.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.StackFile;
import com.capillary.ops.cp.bo.Substack;
import com.capillary.ops.cp.bo.stack.Composition;
import com.capillary.ops.cp.bo.stack.Plugin;
import com.capillary.ops.cp.repository.StackRepository;
import com.capillary.ops.cp.repository.SubstackRepository;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StackService {

    @Autowired
    private StackRepository stackRepository;

    @Autowired
    private SubstackRepository substackRepository;

    @Autowired
    GitService gitService;

    @Value("${aws.s3bucket.ccSubstackBucket.name}")
    private String substackS3Bucket;

    @Value("${aws.s3bucket.ccSubstackBucket.region}")
    private String substackS3BucketRegion;

    //@Cacheable(value = "substacks", unless="#result == null")
    public List<Substack> getSubstacks(String stackName) {
        Optional<Stack> optionalStack = stackRepository.findById(stackName);
        if (!optionalStack.isPresent()) {
            throw new NotFoundException("stack with name " + stackName + "not found");
        }

        Stack stack = optionalStack.get();
        Iterable<Substack> substacks = substackRepository.findAllById(stack.getChildStacks());
        return StreamSupport.stream(substacks.spliterator(), true)
                .collect(Collectors.toList());
    }

    //@Cacheable(value = "substackNames", unless="#result == null")
    public List<String> getSubstackNames(String stackName) {
        List<Substack> substacks = getSubstacks(stackName);
        return substacks.stream().map(Stack::getName).collect(Collectors.toList());
    }

    public Substack getSubstack(String substackName) {
        Optional<Substack> optionalStack = substackRepository.findById(substackName);
        if (!optionalStack.isPresent()) {
            throw new NotFoundException("did not find substack with name: " + substackName);
        }

        return optionalStack.get();
    }

    public Stack syncWithStackFile(Stack stack, Path location) {
        String relativePath = (StringUtils.isEmpty(stack.getRelativePath())) ? "" : stack.getRelativePath();
        File stackFile = loadStackFile(stack, location, relativePath);
        try {
            Gson gson = new Gson();
            StackFile file = gson.fromJson(new FileReader(stackFile), StackFile.class);
            stack.setStackVars(file.getStackVariables());
            stack.setClusterVariablesMeta(file.getClusterVariablesMeta());
            Composition stackComposition = file.getComposition();
            if (stackComposition != null) {
                List<Plugin> enabledPlugins = stackComposition.getPlugins().stream()
                        .filter(plugin -> !plugin.getDisabled())
                        .collect(Collectors.toList());
                enabledPlugins.forEach(plugin -> {
                    Substack substack = substackRepository.findById(plugin.getName()).get();
                    stack.getStackVars().putAll(substack.getStackVars());
                    stack.getClusterVariablesMeta().putAll(substack.getClusterVariablesMeta());
                });
                stack.setChildStacks(enabledPlugins.stream().map(Plugin::getName).collect(Collectors.toList()));
            }
        } catch (Throwable e) {
            throw new IllegalArgumentException(
                    "Invalid Stack Definition in given Directory " + stack.getVcsUrl() + "" + stack.getRelativePath(), e);
        }

        return stack;
    }

    private File loadStackFile(Stack stack, Path location, String relativePath) {
        File stackFile = new File(location.toString() + "/" + relativePath + "/stack.json");
        if (!stackFile.isFile()) {
            throw new IllegalArgumentException(
                    "No Stack Definition in given Directory " + stack.getVcsUrl() + "" + stack.getRelativePath());
        }
        return stackFile;
    }

    public String uploadSubstack(Substack substack, Path location) throws IOException {
        String archivePath = substack.getName() + ".zip";
        File repositoryArchive = new File(archivePath);
        String relativePath = (StringUtils.isEmpty(substack.getRelativePath())) ? "" : substack.getRelativePath();
        ZipUtil.pack(Paths.get(location.toString() + "/" + relativePath + "/").toFile(), repositoryArchive);
        AmazonS3 amazonS3 =
                AmazonS3ClientBuilder.standard().withRegion(Regions.valueOf(substackS3BucketRegion)).build();
        amazonS3.putObject(substackS3Bucket, archivePath, repositoryArchive);
        repositoryArchive.delete();
        FileUtils.deleteDirectory(repositoryArchive);

        return archivePath;
    }
}
