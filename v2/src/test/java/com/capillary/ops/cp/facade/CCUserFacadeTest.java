package com.capillary.ops.cp.facade;

import com.capillary.ops.deployer.bo.User;
import com.capillary.ops.deployer.repository.UserRepository;
import junit.framework.TestCase;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CCUserFacadeTest extends TestCase {
    @Tested
    CCUserFacade userFacade;

    @Injectable
    UserRepository userRepository;

    @Test()
    public void testEmptyUsers() {
        new Expectations() {
            {
                userRepository.findAll();
                result = new ArrayList<>();
            }
        };
        List<User> allCCUsers = userFacade.getAllCCUsers();
        assert allCCUsers.size() == 0;
    }

    @Test()
    public void testExtraRolesUsers() {
        User u = new User();
        u.setUserName("testUser");
        u.setRoles(Arrays.asList("CC-ADMIN", "K8S_READER", "NOT-IDENTIFIED"));
        new Expectations() {
            {
                userRepository.findAll();
                result = Arrays.asList(u);
            }
        };
        List<User> allCCUsers = userFacade.getAllCCUsers();
        assert allCCUsers.size() == 1;
        assert allCCUsers.get(0).getRoles().size() == 2;
    }

    @Test()
    public void testNORolesUsers() {
        User u = new User();
        u.setUserName("testUser");
        u.setRoles(Arrays.asList());
        new Expectations() {
            {
                userRepository.findAll();
                result = Arrays.asList(u);
            }
        };
        List<User> allCCUsers = userFacade.getAllCCUsers();
        assert allCCUsers.size() == 1;
        assert allCCUsers.get(0).getRoles().size() == 1;
        new Verifications() {
            {
                userRepository.save(u);
                times=1;
            }
        };
    }
}