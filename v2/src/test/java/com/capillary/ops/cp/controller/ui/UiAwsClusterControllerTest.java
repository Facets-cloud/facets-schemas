package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.auth.CustomClientRegistrationService;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.StackFile;
import com.capillary.ops.cp.controller.AwsClusterController;
import com.capillary.ops.cp.facade.StackFacade;
import junit.framework.TestCase;
import org.junit.Test;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UiAwsClusterControllerTest extends TestCase {

    @InjectMocks
    UiAwsClusterController uiAwsClusterController;

    @Mock
    AwsClusterController awsClusterController;

    @Mock
    private StackFacade stackFacade;

    @Test
    public void testHideSecrets() {
        Stack stack = new Stack();
        stack.setClusterVariablesMeta(new HashMap<String, com.capillary.ops.cp.bo.StackFile.VariableDetails>(){{
            put("secret1", new StackFile.VariableDetails(true,"secret1"));
            put("secret2", new StackFile.VariableDetails(true,"secret2"));
            put("secret3", new StackFile.VariableDetails(true,"secret3"));
            put("secret4", new StackFile.VariableDetails(true,"secret4"));
        }});
        AwsCluster cluster = new AwsCluster("name");
        cluster.setSecrets(new HashMap<String, String>(){{
            put("secret1","secret1");
            put("secret2","different");
            put("secret3","");
        }});

        when(awsClusterController.getCluster(any())).thenReturn(
                cluster
        );
        when(stackFacade.getStackByName(any())).thenReturn(stack);
        AwsCluster cluster1 = uiAwsClusterController.hideSecrets(cluster);
        assert cluster1.getSecrets().get("secret1").equals("Stack Default");
        assert cluster1.getSecrets().get("secret2").equals("Set");
        assert cluster1.getSecrets().get("secret3").equals("Not Set");
        assert cluster1.getSecrets().get("secret4").equals("Not Set");

        System.out.println(cluster1);
    }
}