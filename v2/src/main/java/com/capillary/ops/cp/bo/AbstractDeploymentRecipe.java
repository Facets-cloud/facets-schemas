package com.capillary.ops.cp.bo;

import com.capillary.ops.cp.bo.recipes.AuroraDRDeploymentRecipe;
import com.capillary.ops.cp.bo.recipes.MongoDRDeploymentRecipe;
import com.capillary.ops.cp.bo.recipes.MongoVolumeResizeDeploymentRecipe;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuroraDRDeploymentRecipe.class, name = "AURORA_DR"),
        @JsonSubTypes.Type(value = MongoDRDeploymentRecipe.class, name = "MONGO_DR"),
        @JsonSubTypes.Type(value = MongoVolumeResizeDeploymentRecipe.class, name = "MONGO_VOLUME_RESIZE")
})
public abstract class AbstractDeploymentRecipe {
}
