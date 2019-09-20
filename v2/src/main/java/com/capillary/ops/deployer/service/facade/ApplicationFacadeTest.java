package com.capillary.ops.deployer.service.facade;

import static org.junit.Assert.*;

import org.junit.Test;


public class ApplicationFacadeTest {

    @Test
    public void testEquals() {
        assertEquals(4, 4);
    }

    @Test
    private void assertEqualsString() {
        assertEquals("hello", "hello");
    }
}
