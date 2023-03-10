package br.com.sicredi.bank.acceptance.product;

import br.com.sicredi.bank.model.enums.ProductType;
import br.com.sicredi.bank.service.ProductService;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static br.com.sicredi.bank.utils.Message.PRODUCT_BUSINESS_SALARY_ERROR;
import static org.hamcrest.Matchers.containsString;

@DisplayName("Product")
@Epic("List Products")
public class ListProductTest {

    ProductService productService = new ProductService();

    @Test
    @Tag("all")
    @Description("Must list products with salary between 1500 and 2999 successfully")
    public void mustListProductsWithSalaryBetween1500And2999Successfully() {
        var salary = BigDecimal.valueOf(2000);

        productService.listProducts(salary)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body(containsString(ProductType.PERSONAL.toString()));
    }

    @Test
    @Tag("all")
    @Description("Must list products with salary between 3000 and 4999 successfully")
    public void mustListProductsWithSalaryBetween3000And4999Successfully() {
        var salary = BigDecimal.valueOf(4000);

        productService.listProducts(salary)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body(containsString(ProductType.PERSONAL.toString()))
                    .body(containsString(ProductType.PAYROLL.toString()));
    }

    @Test
    @Tag("all")
    @Description("Must list products with salary from 5000 successfully")
    public void mustListProductsWithSalaryFrom5000Successfully() {
        var salary = BigDecimal.valueOf(5000);

        productService.listProducts(salary)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body(containsString(ProductType.PERSONAL.toString()))
                    .body(containsString(ProductType.PAYROLL.toString()))
                    .body(containsString(ProductType.FINANCING.toString()));
    }

    @Test
    @Tag("all")
    @Description("Must not list products with salary below 1500")
    public void mustNotListProductsWithSalaryBelow1500() {
        var salary = BigDecimal.valueOf(1499);

        productService.listProducts(salary)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(PRODUCT_BUSINESS_SALARY_ERROR));
    }

}
