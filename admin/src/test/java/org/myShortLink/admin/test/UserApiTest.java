package org.myShortLink.admin.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.myShortLink.admin.common.convention.result.Result;
import org.myShortLink.admin.controller.UserController;
import org.myShortLink.admin.dto.resp.UserRespDTO;
import org.myShortLink.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
public class UserApiTest {

    @Autowired
    private  MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testGetUserByUsername() throws Exception {
        String username = "ddm1293";
        UserRespDTO userRespDTO = new UserRespDTO(123L, username, "ddm1293@gmail.com", "7781237891");

        Mockito.when(userService.getUserByUsername(username))
                .thenReturn(userRespDTO);
        MvcResult res = mockMvc.perform(get("/user/{username}", username))
                .andExpect(status().isOk())
                .andReturn();

        String response = res.getResponse().getContentAsString();
        UserRespDTO resObj = new ObjectMapper()
                .readValue(response, new TypeReference<Result<UserRespDTO>>() {})
                .getData();
        assertEquals(userRespDTO, resObj);
    }

}
