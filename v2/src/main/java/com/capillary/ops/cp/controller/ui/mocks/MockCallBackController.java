package com.capillary.ops.cp.controller.ui.mocks;

import com.capillary.ops.cp.bo.CodeBuildStatusCallback;
import com.capillary.ops.cp.facade.mocks.MockDeploymentFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cc-ui/v1/callbacks")
@Profile("dev")
public class MockCallBackController {
    @Autowired
    MockDeploymentFacade mockDeploymentFacade;

    @PostMapping("/mockcallback")
    public Boolean mockCallBack(@RequestBody CodeBuildStatusCallback callback) {
        mockDeploymentFacade.mockHandleCodeBuildCallback(callback);
        return true;
    }
}
