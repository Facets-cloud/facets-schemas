package com.capillary.ops.cp.service.factory;

import com.capillary.ops.cp.bo.requests.Cloud;
import com.capillary.ops.cp.service.Service;

/**
 * Factory to select any Service based on cloud implementation
 *
 * @param <T>
 */
public interface ServiceFactory<T extends Service> {

    /**
     * Get cloud specific implementation
     *
     * @param cloud Cloud Enum
     * @return Service for the cloud
     */
    T getService(Cloud cloud);
}
