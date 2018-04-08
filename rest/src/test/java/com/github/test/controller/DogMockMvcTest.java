package com.github.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Test
@ContextConfiguration("classpath:/application-context.xml")
@WebAppConfiguration
public class DogMockMvcTest extends AbstractTestNGSpringContextTests {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @BeforeMethod
    private void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldGetAllDogs() throws Exception {
        mockMvc.perform(get("/dog"))
                .andExpect(status().isOk());
    }
}


