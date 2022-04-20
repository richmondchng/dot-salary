package richmond.swe.dotsalary.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit test UserService.
 * @author richmondchng
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
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
            assertEquals("Invalid sort parameter", e.getMessage());
        }
    }
}