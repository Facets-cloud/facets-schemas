package com.capillary.ops.deployer.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//@Profile("!apidev")
//@Controller
public class ForwardingController {
    @RequestMapping("/ui/{path:[^\\.]+}/**")
    public String forward() {
        return "forward:/";
    }
}
