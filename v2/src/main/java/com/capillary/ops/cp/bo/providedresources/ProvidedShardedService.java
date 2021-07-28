package com.capillary.ops.cp.bo.providedresources;

import java.util.List;

public class ProvidedShardedService {
  private List<ProvidedAuthenticatedResource> shards;

  public List<ProvidedAuthenticatedResource> getShards() {
    return shards;
  }

  public void setShards(List<ProvidedAuthenticatedResource> shards) {
    this.shards = shards;
  }
}
