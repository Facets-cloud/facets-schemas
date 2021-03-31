package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.user.Role;
import com.capillary.ops.cp.facade.CCUserFacade;
import com.capillary.ops.deployer.bo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("cc-ui/v1/users/")
public class UiUserController {

    @Autowired
    CCUserFacade userFacade;

    @GetMapping("roles")
    public Role[] getAllRoles() {
        return userFacade.getAllRoles();
    }

    @GetMapping()
    public List<User> getAllUsers() {
        return userFacade.getAllCCUsers();
    }


}
