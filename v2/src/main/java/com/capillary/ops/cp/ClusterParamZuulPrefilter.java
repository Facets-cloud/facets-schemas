package com.capillary.ops.cp;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.repository.CpClusterRepository;
import com.capillary.ops.cp.service.ClusterHelper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;

@Service
@Slf4j
public class ClusterParamZuulPrefilter extends ZuulFilter {

    private static final String HTTPS_WETTY = "https://wetty.";
    private static final String HTTPS_GRAFANA = "https://grafana.";

    private HashMap<String, String> urlMap = new HashMap<>();
    @Autowired
    private CpClusterRepository clusterRepository;

    @Autowired
    private ClusterHelper clusterHelper;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String clusterId = ((String) ctx.get("requestURI")).split("/")[1];
        String resourceType = ((String) ctx.get("requestURI")).split("/")[2];
        String key = clusterId + "-" + resourceType;
        if (!urlMap.containsKey(key)) {
            String routeHost = "/notfound";
            Optional<AbstractCluster> clusterO = clusterRepository.findById(clusterId);
            if (!clusterO.isPresent()) {
                return null;
            }
            AbstractCluster cluster = clusterO.get();
            String toolsPws = clusterHelper.getToolsPws(cluster);
            String baseUrl = clusterHelper.getToolsURL(cluster);


            routeHost = "https://" + resourceType + "." + baseUrl+"/tunnel/";
            urlMap.put(key, routeHost);
            urlMap.put(key + "-auth", toolsPws);
        }
        String routeHost = urlMap.get(key);
        String auth = urlMap.get(key + "-auth");
        try {
            ctx.addZuulRequestHeader("Authorization", "Basic " + auth);
            ctx.put("routeHost", new URL(routeHost));
        } catch (MalformedURLException e) {
            log.warn("Unable to route to {} as it is invalid", routeHost);
        }
        return null;
    }
}
