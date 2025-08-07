package seminario.invoicing.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import seminario.invoicing.dto.ProductDTORequest;
import seminario.invoicing.model.Product;
import seminario.invoicing.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StepDefinitions {

    @LocalServerPort
    private int port;

    private RequestSpecification request;
    private Response response;

    private final ProductRepository productRepository;
    private final JdbcTemplate jdbcTemplate;

    public StepDefinitions(ProductRepository productRepository, JdbcTemplate jdbcTemplate) {
        this.productRepository = productRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Before
    public void setUp() {
        String cleanUpSqlSentence = "TRUNCATE TABLE sale_products, sales, products RESTART IDENTITY CASCADE";
        jdbcTemplate.execute(cleanUpSqlSentence);

        request = given()
                .baseUri("http://localhost")
                .port(port)
                .contentType(ContentType.JSON);
    }

    @DataTableType
    public ProductDTORequest productDTORequestTransformer(Map<String, String> entry) {
        return ProductDTORequest.builder()
                .name(entry.get("name"))
                .manufacturer(entry.get("manufacturer"))
                .amountInStock(Integer.parseInt(entry.get("amountInStock")))
                .price(new BigDecimal(entry.get("price")))
                .build();
    }

    @DataTableType
    public Product Product(Map<String, String> entry) {
        return Product.builder()
                .name(entry.get("name"))
                .manufacturer(entry.get("manufacturer"))
                .amountInStock(Integer.parseInt(entry.get("amountInStock")))
                .price(new BigDecimal(entry.get("price")))
                .entryDate(LocalDate.now())
                .build();
    }

    @Given("API is ready to receive new products")
    public void apiIsReadyToReceiveNewProduct() {

    }

    @Given("the following products exist in the database:")
    public void theFollowingProductsExist(DataTable dataTable) {
        // Convert the Gherkin table to a list of maps, where each map represents a row
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // Iterate over each row to create and save a product
        for (Map<String, String> columns : rows) {
            Product product = Product.builder()
                    .name(columns.get("name"))
                    .manufacturer(columns.get("manufacturer"))
                    .amountInStock(Integer.parseInt(columns.get("amountInStock")))
                    .price(new BigDecimal(columns.get("price")))
                    .entryDate(LocalDate.now()) // Set a default date
                    .build();
            productRepository.save(product);
        }
    }

    @Given("API has products on database")
    public void apiHasProductsOnDatabase() {
        Product product1 = Product.builder()
                .name("Mouse Gamer Pro")
                .manufacturer("Logi")
                .price(BigDecimal.valueOf(75.50))
                .entryDate(LocalDate.now())
                .amountInStock(150)
                .build();

        Product product2 = Product.builder()
                .name("Mechanical Keyboard")
                .manufacturer("Razer")
                .price(BigDecimal.valueOf(120.00))
                .entryDate(LocalDate.now())
                .amountInStock(80)
                .build();

        productRepository.saveAll(List.of(product1, product2));
    }

    @Given("a product exists with the name {string}")
    public void existsAProductWithTheName(String name) {
        Product product = Product.builder()
                .name(name)
                .manufacturer("GenericBrand")
                .price(BigDecimal.valueOf(100))
                .entryDate(LocalDate.now())
                .amountInStock(10)
                .build();
        productRepository.save(product);
    }

    @Given("a product with the following details exists in the database:")
    public void a_product_with_the_following_details_exists_in_the_database(Product product) {
        productRepository.save(product);
    }

    @When("I send a POST request to {string} with the following body:")
    public void sendingAPostRequestTo(String path, ProductDTORequest productDTO) {
        response = request
                .body(productDTO)
                .when()
                .post(path);
    }

    @When("I send a GET request to {string}")
    public void when_i_send_a_get_request_to(String path) {
        response = request
                .when()
                .get(path);
    }

    @Then("the response status code must be {int}")
    public void responseStatusCodeMustBe(int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Then("the response body must be {string}")
    public void responseBodyMustBe(String expectedBody) {
        response.then().body(equalTo(expectedBody));
    }

    @Then("the response must contain the error message {string}")
    public void responseMustContainErrorMessage(String errorMessage) {
        response.then().body("message", containsString(errorMessage));
    }

    @Then("the response must contain the validation error {string}")
    public void responseMustContainValidationError(String validationError) {
        response.then().body("errors.name", equalTo(validationError));
    }

    @Then("the response must be list type")
    public void the_response_must_be_list_type() {
        response.then().body("$", instanceOf(List.class));
    }

    @Then("the response body should contain the following product details:")
    public void theResponseBodyShouldContainTheFollowingProductDetails(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> columns : rows) {
            String field = columns.get("field");
            String expectedValue = columns.get("value");

            if (field.equals("price")) {
                response.then().body(field, equalTo(Float.parseFloat(expectedValue)));
            } else if (field.equals("amountInStock") || field.equals("id")) {
                response.then().body(field, equalTo(Integer.parseInt(expectedValue)));
            } else {
                response.then().body(field, equalTo(expectedValue));
            }
        }
    }

}