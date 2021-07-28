package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.providedresources.AbstractProvidedResource;
import com.capillary.ops.cp.bo.providedresources.ProvidedAuthenticatedResource;
import com.capillary.ops.cp.bo.providedresources.ProvidedCloudResource;
import com.capillary.ops.cp.bo.providedresources.ProvidedResources;
import com.google.common.collect.ImmutableMap;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProvidedResourcesService {

  private static final Map<String, ProvidedResourceTypes> resourceTypesMeta = ImmutableMap.of(
    "s3", ProvidedResourceTypes.CLOUD_RESOURCE,
    "mongo", ProvidedResourceTypes.AUTHENTICATED_SERVICE
  );

  public void validateProvidedResources(Stack stack, ProvidedResources providedResources) {
    stack.getProvidedResources().stream()
      .forEach(x -> {
        boolean present = resourceTypesMeta.get(x.getResourceType()).getFetcherFn().apply(providedResources).stream()
          .anyMatch(y -> y.getResourceType().equalsIgnoreCase(x.getResourceType()) && y.getResourceName().equalsIgnoreCase(x.getResourceName()));
        if(!present) {
          throw new RuntimeException("Required resource details missing for " + x.getResourceType() + " " + x.getResourceName());
        }
      });
  }
}
