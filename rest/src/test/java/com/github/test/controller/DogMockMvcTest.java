package com.github.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.test.model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@ContextConfiguration("classpath:/application-context.xml")
@WebAppConfiguration
public class DogMockMvcTest extends AbstractTestNGSpringContextTests {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    private final ObjectMapper mapper = new ObjectMapper();
    private final String URL = "/dog";
    private final Dog newDog = new Dog("Rex", LocalDate.of(2001, 12, 1), 12.5, 14.1);


    @BeforeMethod
    private void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .build();
    }

    @Test
    public void shouldGetAllDogs() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$..name", containsInAnyOrder("Jack", "Sam", "Tom")))
                .andExpect(jsonPath("$..weight", containsInAnyOrder(20.1, 20.1, 20.1)))
                .andExpect(jsonPath("$..height", containsInAnyOrder(15.4, 15.4, 15.4)))
                .andExpect(jsonPath("$..birthDate", containsInAnyOrder("2011-05-03", "2011-05-03", "2011-05-03")));
    }

    @Test
    void shouldFindDogById() throws Exception {
        Dog createdDog = getCreatedDog();
        mockMvc.perform(get(URL + "/" + createdDog.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(result -> assertReflectionEquals(getObjectFromResponse(result, Dog.class), createdDog));
    }

    @Test
    void shouldCreateDogTest() throws Exception {
        mockMvc.perform(post(URL).content(mapper.writeValueAsString(newDog)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(result -> assertReflectionEquals(getObjectFromResponse(result, Dog.class), newDog));
    }

    @Test
    void shouldUpdateDogTest() throws Exception {
        Dog createdDog = getCreatedDog();
        createdDog.setName("Scooby");
        mockMvc.perform(put(URL + "/" + getCreatedDog().getId())
                .content(mapper.writeValueAsString(createdDog)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(result -> assertReflectionEquals(getObjectFromResponse(result, Dog.class), createdDog));
    }

    @Test
    void shouldDeleteDogTest() throws Exception {
        Dog createdDog = getCreatedDog();
        mockMvc.perform(delete(URL + "/" + createdDog.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete(URL + "/" + createdDog.getId()))
                .andExpect(status().isNotFound());

    }

    private <T> T getObjectFromResponse(MvcResult mvcResult, Class<T> clazz) throws IOException {
        return mapper.readValue(mvcResult.getResponse().getContentAsString(), clazz);
    }

    private Dog getCreatedDog() throws Exception {
        MvcResult result = this.mockMvc.perform(get(URL)).andReturn();
        return getObjectFromResponse(result, Dog[].class)[0];
    }
}


