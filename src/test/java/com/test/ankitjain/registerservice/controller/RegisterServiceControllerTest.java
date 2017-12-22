package com.test.ankitjain.registerservice.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.registerservice.config.WebConfig;
import com.test.registerservice.controller.RegisterServiceController;
import com.test.registerservice.handler.RestExceptionHandler;
import com.test.registerservice.model.User;
import com.test.registerservice.service.ExclusionService;
import com.test.registerservice.service.UserServiceImpl;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
public class RegisterServiceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private ExclusionService exclusionService;

    @Autowired
    RegisterServiceController registerServiceController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registerServiceController).setControllerAdvice(new RestExceptionHandler()).build();
        registerServiceController.setUserService(userService);
        registerServiceController.setExclusionService(exclusionService);
    }

    @Test
    public void registerNullUserDetails() throws Exception {

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isNoContent());

        verifyNoMoreInteractions(userService);
    }

    @Test
    public void registerValidUser() throws Exception {

        final String username = "ValidUser";
        final String password = "ValidP0ssword";
        final String dob = "11-11-2001";
        final String ssn = "SSN101";

        final User user = new User(username, password, dob, ssn);

        when(userService.isUserRegistered(user)).thenReturn(false);
        when(exclusionService.validate(dob, ssn)).thenReturn(true);
        doNothing().when(userService).registerUser(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("/users/ValidUser")));

        verify(userService, times(1)).isUserRegistered(user);
        verify(userService, times(1)).registerUser(user);
        verify(exclusionService, times(1)).validate(dob, ssn);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void registerBlackListedUserForDob() throws Exception {
        final String username = "ValidUser";
        final String password = "ValidP0ssword";
        final String dob = "01-01-2000";
        final String ssn = "SSN101";

        final User user = new User(username, password, dob, ssn);

        when(userService.isUserRegistered(user)).thenReturn(false);
        when(exclusionService.validate(dob, ssn)).thenReturn(false);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andExpect(status().isBadRequest());

        verify(userService, times(1)).isUserRegistered(user);
        verify(exclusionService, times(1)).validate(dob, ssn);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void registerBlackListedUserForSsn() throws Exception {
        final String username = "ValidUser";
        final String password = "ValidP0ssword";
        final String dob = "11-11-2001";
        final String ssn = "SSN10";

        final User user = new User(username, password, dob, ssn);

        when(userService.isUserRegistered(user)).thenReturn(false);
        when(exclusionService.validate(dob, ssn)).thenReturn(false);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andExpect(status().isBadRequest());

        verify(userService, times(1)).isUserRegistered(user);
        verify(exclusionService, times(1)).validate(dob, ssn);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void registerUserAlreadyExist() throws Exception {
        final String username = "ExistingUser";
        final String password = "ValidP0ssword";
        final String dob = "11-11-2001";
        final String ssn = "SSN101";

        final User user = new User(username, password, dob, ssn);

        when(userService.isUserRegistered(user)).thenReturn(true);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andExpect(status().isConflict())
                .andExpect(status().isConflict());

        verify(userService, times(1)).isUserRegistered(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void registerInvalidUsername() throws Exception {

        final String username = "Invalid User";
        final String password = "ValidP0ssword";
        final String dob = "11-11-2001";
        final String ssn = "SSN101";

        final User user = new User(username, password, dob, ssn);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andExpect(status().isBadRequest())
                .andExpect(content().string("{\"status\":\"BAD_REQUEST\",\"message\":\"Username (alphanumerical, no spaces) is invalid\"}"));
    }

    @Test
    public void registerInvalidPassword() throws Exception {

        final String username = "ValidUser";
        final String password = "Invalid password";
        final String dob = "11-11-2001";
        final String ssn = "SSN101";

        final User user = new User(username, password, dob, ssn);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "{\"status\":\"BAD_REQUEST\",\"message\":\"Password (at least four characters, at least one upper case character, at least one number) is invalid\"}"));
    }

    @Test
    public void getAllUsers() throws Exception {
        List<User> users = Arrays.asList(new User("ValidUser1", "ValidP0ssword", "10-10-2010", "SSN111"),
                new User("ValidUser2", "ValidP0ssword", "10-10-2010", "SSN111"));

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].username", is("ValidUser1")))
                .andExpect(jsonPath("$[1].username", is("ValidUser2")));

        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void getUserByUsername() throws Exception {
        final String username = "ValidUser1";
        final String password = "ValidP0ssword";
        final String dob = "11-11-2001";
        final String ssn = "SSN101";

        final User user = new User(username, password, dob, ssn);

        when(userService.getUser(username)).thenReturn(user);

        mockMvc.perform(get("/users/{username}", username)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.username", is("ValidUser1")));

        verify(userService, times(1)).getUser(username);
        verifyNoMoreInteractions(userService);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
