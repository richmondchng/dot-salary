package richmond.swe.dotsalary.controller;

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
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test "/users".
 * @author richmondchng
 */
@WebMvcTest(controllers = { UsersController.class })
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    /**
     * Test GET "/users".
     *
     * Return empty collection.
     */
    @Test
    void givenNoUsers_whenGetUsers_returnEmptyData() throws Exception {

        when(userService.getUsers(any(), any(), anyInt(), isNull(), isNull())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect((jsonPath("$.results", hasSize(0))));

        verify(userService, times(1)).getUsers(eq(BigDecimal.ZERO), eq(BigDecimal.valueOf(4000)),
                eq(0), isNull(), isNull());
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
        when(userService.getUsers(any(), any(), anyInt(), isNull(), isNull())).thenReturn(results);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect((jsonPath("$.results", hasSize(2))))
                // same order as service results
                .andExpect(jsonPath("$.results[0].name", is("John")))
                .andExpect(jsonPath("$.results[0].salary", is(3000)))
                .andExpect(jsonPath("$.results[1].name", is("Ryan")))
                .andExpect(jsonPath("$.results[1].salary", is(3500))) ;

        verify(userService, times(1)).getUsers(eq(BigDecimal.ZERO), eq(BigDecimal.valueOf(4000)),
                eq(0), isNull(), isNull());
    }

    /**
     * Test GET "/users".
     *
     * Return users filtered by options.
     */
    @Test
    void givenHasRequestParams_whenGetUsers_returnUsersByParams() throws Exception {

        final Collection<UserBean> results = Arrays.asList(
                UserBean.builder().name("John").salary(BigDecimal.valueOf(3000)).build(),
                UserBean.builder().name("Ryan").salary(BigDecimal.valueOf(3500)).build());
        when(userService.getUsers(any(), any(), anyInt(), anyInt(), anyString())).thenReturn(results);

        mockMvc.perform(get("/users?min=100&max=5000&offset=10&limit=99&sort=name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect((jsonPath("$.results", hasSize(2))))
                // same order as service results
                .andExpect(jsonPath("$.results[0].name", is("John")))
                .andExpect(jsonPath("$.results[0].salary", is(3000)))
                .andExpect(jsonPath("$.results[1].name", is("Ryan")))
                .andExpect(jsonPath("$.results[1].salary", is(3500))) ;

        verify(userService, times(1)).getUsers(eq(BigDecimal.valueOf(100)),
                eq(BigDecimal.valueOf(5000)), eq(10), eq(99), eq("name"));
    }

    /**
     * Test GET "/users".
     *
     * Invalid request param, throws exception.
     */
    @Test
    void givenInvalidRequestParams_whenGetUsers_throwException() throws Exception {

        when(userService.getUsers(any(), any(), anyInt(), isNull(), anyString())).thenThrow(new RuntimeException("Invalid parameter"));

        mockMvc.perform(get("/users?sort=gender"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error", is("Invalid parameter")));

        verify(userService, times(1)).getUsers(eq(BigDecimal.ZERO), eq(BigDecimal.valueOf(4000)),
                eq(0), isNull(), eq("gender"));
    }
}