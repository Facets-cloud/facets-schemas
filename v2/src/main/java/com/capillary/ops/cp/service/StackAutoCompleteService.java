package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AutoCompleteObject;
import com.capillary.ops.cp.repository.AutoCompleteObjectRepository;
import com.capillary.ops.cp.repository.StackRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This service will be responsible to add hints, autocompletes by parsing the checked out stack
 */
@Component
@Slf4j
public class StackAutoCompleteService {

    @Autowired
    AutoCompleteObjectRepository autoCompleteObjectRepository;

    @Autowired
    StackRepository stackRepository;

    @Autowired
    StackService stackService;

    /**
     * This function will parse the checked out folder and populate the autocomplete collection
     *
     * @param stackName Stack name
     * @param location  Temporary Checked out location of the Stack
     * @return True or False
     */
    public Boolean parseStack(String stackName, String location) {
        String[] applicationTypes = getDirectoriesOrFiles(location, true);
        List<AutoCompleteObject> autoCompleteObjects = new ArrayList<>();
        for (String resourceType : applicationTypes) {
            if (resourceType.startsWith("\\.")) {
                continue;
            }
            AutoCompleteObject autoCompleteObject = new AutoCompleteObject(stackName, resourceType);
            Optional<AutoCompleteObject> existingObject = autoCompleteObjectRepository.findOneByStackNameAndResourceType(
                    stackName, resourceType);
            existingObject.ifPresent(x -> autoCompleteObject.setId(x.getId()));
            String resourceTypePath = location + "/" + resourceType + "/instances/";
            String[] instances = getDirectoriesOrFiles(resourceTypePath, false);
            if (instances != null) {
                for (String resource : instances) {
                    String resourceName = resource.split("\\.")[0];
                    autoCompleteObject.getResourceNames().add(resourceName);
                }
            }
            autoCompleteObjects.add(autoCompleteObject);
        }
        autoCompleteObjectRepository.saveAll(autoCompleteObjects);
        return true;
    }

    /**
     * Given a stackName and resourceType get all suggestions.
     *
     * @param stackName
     * @param resourceType
     * @return
     */
    public Set<String> getResourcesSuggestion(String stackName, String resourceType) {
        List<String> stacks = stackService.getSubstackNames(stackName);
        stacks.add(stackName);
        List<AutoCompleteObject> allByStackNameAndResourceType = autoCompleteObjectRepository
                .findAllByStackNameInAndResourceType(stacks, resourceType);
        Set<String> resourceNames = new HashSet<>();
        allByStackNameAndResourceType.forEach(autoCompleteObject -> resourceNames.addAll(autoCompleteObject.getResourceNames()));
        return resourceNames;
    }

    /**
     * Given a stackName  get all suggestions.
     *
     * @param stackName
     * @return
     */
    public Set<String> getResourceTypesSuggestion(String stackName) {
        List<String> stacks = stackService.getSubstackNames(stackName);
        stacks.add(stackName);
        List<AutoCompleteObject> allByStackName = autoCompleteObjectRepository.findAllByStackNameIn(stacks);
        return allByStackName.stream().map(AutoCompleteObject::getResourceType).collect(Collectors.toSet());
    }

    /**
     * Given a location getDirectories or Files
     *
     * @param location
     * @return
     */
    private String[] getDirectoriesOrFiles(String location, boolean isDirectory) {
        File file = new File(location);
        String[] applicationTypes = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                if (isDirectory) {
                    return new File(current, name).isDirectory();
                }
                return !new File(current, name).isDirectory();
            }
        });
        return applicationTypes;
    }
}
