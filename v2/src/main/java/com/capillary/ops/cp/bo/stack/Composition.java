package com.capillary.ops.cp.bo.stack;

import java.util.ArrayList;
import java.util.List;

public class Composition {

    private List<Plugin> plugins = new ArrayList<>();

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<Plugin> plugins) {
        this.plugins = plugins;
    }
}
