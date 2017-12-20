package com.test.ankitjain.registerservice.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.test.ankitjain.registerservice.helper.RequestValidator;
import com.test.ankitjain.registerservice.model.User;
import com.test.ankitjain.registerservice.service.ExclusionServiceMock;
import com.test.ankitjain.registerservice.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RegisterServiceController.class, secure = false)
public class RegisterServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RequestValidator validator;

    @MockBean
    private ExclusionServiceMock exclusionServiceMock;

    @Test
    public void registerUserWithInvalidUsername() throws Exception {

        final String USER_JSON_INVALID_USERNAME = "{\r\n" + "  \"username\": \"Ankit Jain\",\r\n" + "  \"password\": \"Ab1anshfc\",\r\n"
                + "  \"dob\" : \"21-01-1986\",\r\n" + "  \"ssn\": \"SSN34\"\r\n" + "}";

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user").accept(MediaType.APPLICATION_JSON).content(USER_JSON_INVALID_USERNAME)
                .contentType(MediaType.APPLICATION_JSON);

        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void registerUserWithInvalidPassword() throws Exception {

        final String USER_JSON_INVALID_PASSWORD = "{\r\n" + "  \"username\": \"AnkitJain\",\r\n" + "  \"password\": \"banshfc\",\r\n"
                + "  \"dob\" : \"21-01-1986\",\r\n" + "  \"ssn\": \"SSN21\"\r\n" + "}";

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user").accept(MediaType.APPLICATION_JSON).content(USER_JSON_INVALID_PASSWORD)
                .contentType(MediaType.APPLICATION_JSON);

        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
    
    @Test
    public void registerBlacklistedUser() throws Exception {

        final String USER_JSON_BLACKLISTED = "{\r\n" + "  \"username\": \"AnkitJain\",\r\n" + "  \"password\": \"banshfc\",\r\n" + "  \"dob\" : \"01-01-2000\",\r\n"
                + "  \"ssn\": \"SSN10\"\r\n" + "}";

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user").accept(MediaType.APPLICATION_JSON).content(USER_JSON_BLACKLISTED)
                .contentType(MediaType.APPLICATION_JSON);

        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
    /*
    @Test
    public void registerValidUser() throws Exception {

        final String USER_JSON_VALID = "{\r\n" + 
                "  \"username\": \"AnkitJain\",\r\n" + 
                "  \"password\": \"Abc1as2\",\r\n" + 
                "  \"dob\" : \"20-01-1987\",\r\n" + 
                "  \"ssn\": \"SSN101\"\r\n" + 
                "}";

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user").accept(MediaType.APPLICATION_JSON).content(USER_JSON_VALID)
                .contentType(MediaType.APPLICATION_JSON);

        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        System.out.println("result ------ " + result);
        final MockHttpServletResponse response = result.getResponse();
        System.out.println("response ------ " + response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }
    
    @Test
    public void getUser() throws Exception {
        User user = new User();
        user.setUsername("TestUser");
        user.setPassword("1BDJ2cjd");
        user.setDob("20-01-1987");
        user.setSsn("ssn101");
        
        Mockito.when(userService.getUser("TestUser")).thenReturn(user);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/user").accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        
        System.out.println(result.getResponse());
        String expected = "{\r\n" + 
                "  \"username\": \"TestUser\",\r\n" + 
                "  \"password\": \"1BDJ2cjd\",\r\n" + 
                "  \"dob\" : \"20-01-1987\",\r\n" + 
                "  \"ssn\": \"ssn101\"\r\n" + 
                "}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }*/
}
