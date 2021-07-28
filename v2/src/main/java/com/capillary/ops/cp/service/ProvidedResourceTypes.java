package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.providedresources.AbstractProvidedResource;
import com.capillary.ops.cp.bo.providedresources.ProvidedResources;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ProvidedResourceTypes {
  UNAUTHENICATED_SERVICE (ProvidedResources::getProvidedUnauthenticatedResources),
  AUTHENTICATED_SERVICE (ProvidedResources::getProvidedAuthenticatedResources),
  CLOUD_RESOURCE (ProvidedResources::getProvidedCloudResources);

  ProvidedResourceTypes(Function<ProvidedResources, List<? extends AbstractProvidedResource>> fetcherFn) {
    this.fetcherFn = fetcherFn;
  }

  @JsonIgnore
  private Function<ProvidedResources, List<? extends AbstractProvidedResource>> fetcherFn;

  public Function<ProvidedResources, List<? extends AbstractProvidedResource>> getFetcherFn() {
    return fetcherFn;
  }

  public void setFetcherFn(Function<ProvidedResources, List<? extends AbstractProvidedResource>> fetcherFn) {
    this.fetcherFn = fetcherFn;
  }
}
