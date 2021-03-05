package com.capillary.ops.deployer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@ControllerAdvice
public class LoginErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(LoginErrorController.class);

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public ModelAndView goToSigninPage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("error happened while returning response: {}", response);
        return new ModelAndView(new RedirectView("/pages/signin", true));
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
