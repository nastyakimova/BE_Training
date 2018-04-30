package com.github.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.test.model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
import java.util.Arrays;
import java.util.List;

import static com.github.test.TestUtils.createRandomDog;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@ContextConfiguration("classpath:/application-context.xml")
@WebAppConfiguration
public class DogMockMvcTest extends AbstractTestNGSpringContextTests {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    private final ObjectMapper mapper = new ObjectMapper();
    private final String URL = "/dog";

    @BeforeMethod
    private void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .build();
    }

    @Test
    public void shouldGetAllDogs() throws Exception {
        List<Dog> allDogs = getAllDogs();
        assertThat(allDogs.size(), greaterThanOrEqualTo(3));
    }

    @Test
    void shouldFindDogById() throws Exception {
        Dog createdDog = getAllDogs().get(0);
        mockMvc.perform(get(URL + "/" + createdDog.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(result -> assertReflectionEquals(getObjectFromResponse(result, Dog.class), createdDog));
    }

    @Test
    void shouldCreateDogTest() throws Exception {
        final Dog newDog = createRandomDog();
        mockMvc.perform(post(URL).content(mapper.writeValueAsString(newDog)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(result -> assertReflectionEquals(getObjectFromResponse(result, Dog.class), newDog));
        List<Dog> allDogs = getAllDogs();
        assertTrue(allDogs.contains(newDog));
    }

    @Test
    void shouldUpdateDogTest() throws Exception {
        Dog createdDog = getAllDogs().get(0);
        Dog randomDog = createRandomDog();
        randomDog.setId(createdDog.getId());
        mockMvc.perform(put(URL + "/" + randomDog.getId())
                .content(mapper.writeValueAsString(randomDog)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(result -> assertReflectionEquals(getObjectFromResponse(result, Dog.class), randomDog));
        List<Dog> allDogs = getAllDogs();
        assertTrue(allDogs.contains(randomDog));
    }

    @Test
    void shouldNotUpdateNotExistingDog() throws Exception {
        Dog randomDog = createRandomDog();
        List<Dog> allDogs = getAllDogs();
        assertTrue(!allDogs.contains(randomDog));
        mockMvc.perform(put(URL + "/" + randomDog.getId())
                .content(mapper.writeValueAsString(randomDog)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldDeleteDogTest() throws Exception {
        Dog newDog = createRandomDog();
        mockMvc.perform(post(URL).content(mapper.writeValueAsString(newDog)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(delete(URL + "/" + newDog.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete(URL + "/" + newDog.getId()))
                .andExpect(status().isNotFound());

    }

    private <T> T getObjectFromResponse(MvcResult mvcResult, Class<T> clazz) throws IOException {
        return mapper.readValue(mvcResult.getResponse().getContentAsString(), clazz);
    }

    private List<Dog> getAllDogs() throws Exception {
        MvcResult result = mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        return Arrays.asList(getObjectFromResponse(result, Dog[].class));
    }
}


