import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.bellintegrator.Specifications;
import ru.bellintegrator.data.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

public class Tests {

    @Test
    public void avatarFileNameTest() {

        List<PersonData> data = given()
                .spec(Specifications.requestSpec())
                .when()
                .get("/api/users?page=2")
                .then()
                //.log().body()
                .spec(Specifications.responseSpec200())
                .extract().body().jsonPath().getList("data", PersonData.class);

        String fileName = "128.jpg";
        Assert.assertTrue(data.stream().allMatch(x -> x.getAvatar().endsWith(fileName)));
    }

    @Test
    public void successfulRegistrationTest() {

        User user = new User("eve.holt@reqres.in", "pistol");

        RegisteredUser registeredUser = given()
                .spec(Specifications.requestSpec())
                .body(user)
                .when()
                .post("/api/register")
                .then()
                //.log().body()
                .spec(Specifications.responseSpec200())
                .extract().body().as(RegisteredUser.class);

        Assert.assertNotNull(registeredUser.getId(), "Неуспешная регистрация");
        Assert.assertNotNull(registeredUser.getToken(), "Неуспешная регистрация");
    }

    @Test
    public void unsuccessfulRegistrationTest() {

        User user = new User("my_cat_is_hungry@meow.com");

        RegistrationError error = given()
                .spec(Specifications.requestSpec())
                .body(user)
                .when()
                .post("/api/register")
                .then()
                //.log().all()
                .spec(Specifications.responseSpec400())
                .extract().body().as(RegistrationError.class);

        Assert.assertNotNull(error.getError(), "Регистрация должна быть неуспешной");
    }

    @Test
    public void sortedByYearTest() {

        Resource resource = given()
                .spec(Specifications.requestSpec())
                .when()
                .get("/api/unknown")
                .then()
                //.log().all()
                .spec(Specifications.responseSpec200())
                .extract().as(Resource.class);

        List<Integer> years = new ArrayList<>();
        resource.getData().stream().forEach(x -> years.add(x.getYear()));

        List<Integer> yearsSorted = years;
        Collections.sort(yearsSorted);

        Assert.assertEquals(years, yearsSorted, "Данные не отсортированы по годам");
    }
}
