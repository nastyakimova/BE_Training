package com.github.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$..name", containsInAnyOrder("Jack", "Sam", "Tom")))
                .andExpect(jsonPath("$..weight", containsInAnyOrder(20.1, 20.1, 20.1)))
                .andExpect(jsonPath("$..height", containsInAnyOrder(15.4, 15.4, 15.4)));
    }

    @Test
    void shouldFindDogById() throws Exception {

    }

    @Test
    void shouldCreateDogTest() throws Exception {

    }

    @Test
    void shouldUpdateDogTest() {
    }

    @Test
    void shouldDeleteDogTest() {
    }
}


