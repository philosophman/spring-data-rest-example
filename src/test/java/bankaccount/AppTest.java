package bankaccount;

import bankaccount.config.WebConfig;
import bankaccount.domain.BankAccount;
import bankaccount.domain.Customer;
import bankaccount.web.MoneyTransferDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.factory.DefaultJackson2ObjectMapperFactory;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.get;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {
    @LocalServerPort
    private int serverPort;

    @Before
    public void setup() {
        RestAssured.port = serverPort;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
                objectMapperConfig().jackson2ObjectMapperFactory(
                        new DefaultJackson2ObjectMapperFactory() {
                            @Override
                            public ObjectMapper create(Type cls, String charset) {
                                final ObjectMapper mapper = new ObjectMapper();
                                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                                return mapper;
                            }
                        }
                )
        );
    }

    @Test
    public void givenCustomer_whenRequestCustomer_thenGetCustomer() {
        final Customer customer = new Customer(null, "firstName", "lastName");
        final String url = createCustomer(customer);
        get(url).then()
                .statusCode(200)
                .body("firstName", equalTo(customer.getFirstName()))
                .body("lastName", equalTo(customer.getLastName()));
    }

    @Test
    public void givenNoCustomer_whenRequestCustomer_thenGet404() {
        get("/customer/1").then().statusCode(404);
    }

    @Test
    public void given2Accounts_whenTransferMoney_thenGetChangedMoneyAmount() {
        // create bank accounts
        final BankAccount account1 = new BankAccount(100_00L);
        final BankAccount account2 = new BankAccount(200_00L);
        final String accountUrl1 = createBankAccount(account1);
        final String accountUrl2 = createBankAccount(account2);

        // create customers (not necessary for test)
        final Customer customer1 = new Customer(null, "firstName1", "lastName1");
        final Customer customer2 = new Customer(null, "firstName2", "lastName2");
        final String customerUrl1 = createCustomer(customer1);
        final String customerUrl2 = createCustomer(customer2);

        // bind customers to bank accounts (not necessary for test)
        given().contentType("text/uri-list")
                .body(customerUrl1).when()
                .put(accountUrl1 + "/customer").then().statusCode(204);
        given().contentType("text/uri-list")
                .body(customerUrl2).when()
                .put(accountUrl2 + "/customer").then().statusCode(204);

        // check that account is bound to customer (not necessary for test)
        get(accountUrl1 + "/customer").then()
                .body("firstName", equalTo(customer1.getFirstName()));

        // transfer money
        BankAccount accountCreated1 = get(accountUrl1).then().extract().as(BankAccount.class);
        BankAccount accountCreated2 = get(accountUrl2).then().extract().as(BankAccount.class);
        assertEquals(account1.getMoneyAmount(), accountCreated1.getMoneyAmount());
        assertEquals(account2.getMoneyAmount(), accountCreated2.getMoneyAmount());
        assertNotNull(accountCreated1.getAccountNumber());
        assertNotNull(accountCreated2.getAccountNumber());
        given().body(new MoneyTransferDto(
                accountCreated1.getAccountNumber(),
                accountCreated2.getAccountNumber(),
                5000))
                .when().post(WebConfig.URL_TRANSFER.getHref())
                .then().statusCode(200);
        // check that money amount has been changed
        accountCreated1 = get(accountUrl1).then().extract().as(BankAccount.class);
        accountCreated2 = get(accountUrl2).then().extract().as(BankAccount.class);
        assertEquals(accountCreated1.getMoneyAmount().longValue(), 50_00L);
        assertEquals(accountCreated2.getMoneyAmount().longValue(), 250_00L);
    }

    @Test
    public void givenNoAccount_whenTransferMoney_whenGet400() {
        given().body(new MoneyTransferDto(
                1,
                2,
                5000))
                .when().post(WebConfig.URL_TRANSFER.getHref())
                .then().log().body()
                .statusCode(400);
    }

    /**
     * @return URL of created resource
     */
    private String createCustomer(Customer customer) {
        return createEntity(customer, "/customers");
    }

    /**
     * @return URL of created resource
     */
    private String createBankAccount(BankAccount account) {
        return createEntity(account, "/bankAccounts");
    }

    private String createEntity(Object entity, String path) {
        return given()
                .body(entity).when()
                .post(path)
                .then()
                .statusCode(201)
                .extract().header("location");
    }

    private RequestSpecification given() {
        return io.restassured.RestAssured.given()
                .log().uri()
                .contentType("application/json; charset=UTF-8")
                .port(serverPort);
    }
}
