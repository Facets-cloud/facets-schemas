/* tslint:disable */
import { ProvidedAuthenticatedResource } from './provided-authenticated-resource';
import { ProvidedCloudResource } from './provided-cloud-resource';
import { ProvidedShardedService } from './provided-sharded-service';
import { ProvidedUnauthenticatedResource } from './provided-unauthenticated-resource';
export interface ProvidedResources {
  clusterId?: string;
  id?: string;
  providedAuthenticatedResources?: Array<ProvidedAuthenticatedResource>;
  providedCloudResources?: Array<ProvidedCloudResource>;
  providedShardedResources?: Array<ProvidedShardedService>;
  providedUnauthenticatedResources?: Array<ProvidedUnauthenticatedResource>;
}
