package richmond.swe.dotsalary.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import richmond.swe.dotsalary.data.entity.UserEntity;
import richmond.swe.dotsalary.data.repository.UserRepository;
import richmond.swe.dotsalary.service.bean.UserBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit test UserService.
 * @author richmondchng
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    // test instance
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @AfterEach
    void tearDown() {
        userService = null;
    }

    /**
     * Test getUsers.
     *
     * Test mandatory parameter, missing min parameter.
     */
    @Test
    void givenMinParamNull_whenGetUsers_throwException() {
        try {
            userService.getUsers(null, BigDecimal.valueOf(4000), 0, null, "name");
            fail("Expect exception to be thrown");
        } catch(IllegalArgumentException e) {
            verifyNoInteractions(userRepository);
            assertEquals("Missing mandatory parameter min", e.getMessage());
        }
    }

    /**
     * Test getUsers.
     *
     * Test mandatory parameter, missing max parameter.
     */
    @Test
    void givenMaxParamNull_whenGetUsers_throwException() {
        try {
            userService.getUsers(BigDecimal.ZERO, null, 0, null, "name");
            fail("Expect exception to be thrown");
        } catch(IllegalArgumentException e) {
            verifyNoInteractions(userRepository);
            assertEquals("Missing mandatory parameter max", e.getMessage());
        }
    }

    /**
     * Test getUsers.
     *
     * Test mandatory parameter, missing offset parameter.
     */
    @Test
    void givenOffsetParamNull_whenGetUsers_throwException() {
        try {
            userService.getUsers(BigDecimal.ZERO, BigDecimal.valueOf(4000), null, null, "name");
            fail("Expect exception to be thrown");
        } catch(IllegalArgumentException e) {
            verifyNoInteractions(userRepository);
            assertEquals("Missing mandatory parameter offset", e.getMessage());
        }
    }

    /**
     * Test getUsers.
     *
     * Test invalid sort parameter value.
     */
    @Test
    void givenInvalidSortValue_whenGetUsers_throwException() {
        try {
            userService.getUsers(BigDecimal.ZERO, BigDecimal.valueOf(4000), 0, null, "gender");
            fail("Expect exception to be thrown");
        } catch(IllegalArgumentException e) {
            verifyNoInteractions(userRepository);
            assertEquals("Invalid sort parameter", e.getMessage());
        }
    }

    /**
     * Test getUsers.
     *
     * Test no records returned from repository.
     */
    @Test
    void givenNoRecordsFromRepository_whenGetUsers_returnEmptyList() {
        // return empty list
        when(userRepository.findBySalary(any(), any(), any())).thenReturn(Collections.emptyList());

        final Collection<UserBean> results = userService.getUsers(BigDecimal.valueOf(0), BigDecimal.valueOf(4000),
                0, null, "name");

        verify(userRepository, times(1)).findBySalary(eq(BigDecimal.ZERO),
                eq(BigDecimal.valueOf(4000)), any());

        assertEquals(0, results.size());
    }

    /**
     * Test getUsers.
     *
     * Test records returned.
     */
    @Test
    void givenValidParameters_whenGetUsers_returnRecords() {
        // return
        final UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setName("John");
        user1.setSalary(BigDecimal.valueOf(3010));

        final UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setName("Ryan");
        user2.setSalary(BigDecimal.valueOf(3020));

        when(userRepository.findBySalary(any(), any(), any())).thenReturn(Arrays.asList(user1, user2));

        final Collection<UserBean> results = userService.getUsers(BigDecimal.valueOf(0), BigDecimal.valueOf(4000),
                0, null, "name");

        verify(userRepository, times(1)).findBySalary(eq(BigDecimal.ZERO),
                eq(BigDecimal.valueOf(4000)), any());

        assertEquals(2, results.size());

        // get record as returned
        final Iterator<UserBean> iterator = results.iterator();

        final UserBean result1 = iterator.next();
        assertEquals(1, result1.getId());
        assertEquals("John", result1.getName());
        assertTrue(BigDecimal.valueOf(3010).compareTo(result1.getSalary()) == 0);

        final UserBean result2 = iterator.next();
        assertEquals(2, result2.getId());
        assertEquals("Ryan", result2.getName());
        assertTrue(BigDecimal.valueOf(3020).compareTo(result2.getSalary()) == 0);
    }
}