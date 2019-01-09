package com.capillary.ops.service;

import com.capillary.ops.bo.ConfigSet;
import com.capillary.ops.repository.ConfigSetRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigSetService {

  @Autowired private ConfigSetRepository configSetRepository;

  public ConfigSet addConfigSet(ConfigSet configSet) {
    return configSetRepository.save(configSet);
  }

  public List<ConfigSet> getAllConfigSets() {
    return configSetRepository.findAll();
  }

  public List<ConfigSet> getConfigSetByName(String name) {
    return configSetRepository.findByName(name);
  }
}
