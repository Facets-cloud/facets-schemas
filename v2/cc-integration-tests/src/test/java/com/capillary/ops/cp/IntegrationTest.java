package com.capillary.ops.cp;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;

//@ActiveProfiles({"dev"})
public class IntegrationTest {

    @Test
    public void testTrue(){
        assert("test").equals("test");
    }

    @Test
    public void testFalse(){
        assert("test").equals("test");
    }

}

