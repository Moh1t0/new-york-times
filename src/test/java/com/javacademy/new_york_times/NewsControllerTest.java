package com.javacademy.new_york_times;

import com.javacademy.new_york_times.dto.NewsDto;
import com.javacademy.new_york_times.dto.PageDto;
import com.javacademy.new_york_times.entity.NewsEntity;
import com.javacademy.new_york_times.repository.NewsRepository;
import com.javacademy.new_york_times.service.NewsService;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBodyExtractionOptions;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@Cacheable(value = "news", condition = "#result != null")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class NewsControllerTest {

    @Autowired
    private NewsService newsService;

    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBasePath("/news")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();


    @Test
    @DisplayName("Получение всех новостей по странице")
    public void getAllSuccess() {
        NewsDto expected = NewsDto.builder()
                .number(1)
                .title("News #1")
                .text("Today is Groundhog Day #1")
                .author("Molodyko Yuri")
                .build();
        PageDto result = given(requestSpecification)
                .queryParam("page", 0)
                .get()
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .as(PageDto.class);

        NewsDto newsDto = result.getContent().get(0);
        assertEquals(expected, newsDto);
    }

    @Test
    @DisplayName("Получение новости по id")
    public void getByIdSuccess() {
        NewsDto expected = NewsDto.builder()
                .number(1)
                .title("News #1")
                .text("Today is Groundhog Day #1")
                .author("Molodyko Yuri")
                .build();
        NewsDto result = given(requestSpecification)
                .get("/1")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .body()
                .as(NewsDto.class);
        assertEquals(expected, result);
    }


    @Test
    @DisplayName("Успешное создание новости")
    public void createSuccess() {

        NewsDto createNews = NewsDto.builder()
                .title("test title")
                .text("test text")
                .author("test author")
                .build();

        NewsDto expected = NewsDto.builder()
                .number(1001)
                .author("test author")
                .title("test title")
                .text("test text")
                .build();

        given(requestSpecification)
                .body(createNews)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(201);

        NewsDto createdNews = newsService.save(createNews);
        Integer createdNumber = createdNews.getNumber();

        NewsDto result = newsService.findByNumber(createdNumber);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Удаление новости по id")
    public void deleteByIdSuccess() {
        Boolean resultDeleteNews = given(requestSpecification)
                .delete("/1")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .as(Boolean.class);

        assertTrue(resultDeleteNews);
    }
}