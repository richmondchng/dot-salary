package richmond.swe.dotsalary.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import richmond.swe.dotsalary.service.UserService;
import richmond.swe.dotsalary.service.bean.UserBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test /users.
 */
@WebMvcTest(controllers = { UsersController.class })
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Test GET "/users".
     *
     * Return empty collection.
     */
    @Test
    void givenNoUsers_whenGetUsers_returnEmptyData() throws Exception {

        when(userService.getUsers()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect((jsonPath("$.results", hasSize(0))));

        verify(userService, times(1)).getUsers();
    }

    /**
     * Test GET "/users".
     *
     * Return users filtered by default options.
     */
    @Test
    void givenHasUsers_whenGetUsers_returnUsersDefaultOptions() throws Exception {

        final Collection<UserBean> results = Arrays.asList(
                UserBean.builder().name("John").salary(BigDecimal.valueOf(3000)).build(),
                UserBean.builder().name("Ryan").salary(BigDecimal.valueOf(3500)).build());
        when(userService.getUsers()).thenReturn(results);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect((jsonPath("$.results", hasSize(2))));

        verify(userService, times(1)).getUsers();
    }
}