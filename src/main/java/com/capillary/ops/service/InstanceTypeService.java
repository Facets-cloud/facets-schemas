package com.capillary.ops.service;

import com.capillary.ops.bo.InstanceType;
import com.capillary.ops.bo.exceptions.ResourceAlreadyExists;
import com.capillary.ops.repository.InstanceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InstanceTypeService {

    @Autowired
    private InstanceTypeRepository instanceTypeRepository;

    public InstanceType save(InstanceType instanceType) {
        InstanceType existingInstanceType = this.findByName(instanceType.getName());
        if (existingInstanceType != null) {
            throw new ResourceAlreadyExists("Instance type with this name already exist");
        }

        return instanceTypeRepository.save(instanceType);
    }

    public InstanceType findByName(String instanceTypeName) {
        List<InstanceType> instanceTypeList = instanceTypeRepository.findByName(instanceTypeName);
        if (instanceTypeList.size() > 1) {
            System.out.println("Error in data, more than one instance types found with the same name");
        }

        return instanceTypeList.isEmpty() ? null : instanceTypeList.get(0);
    }

    public List<InstanceType> findAll() {
        return instanceTypeRepository.findAll();
    }
}
