package br.com.sicredi.bank.aceitacao.product;

import br.com.sicredi.bank.dto.enums.ProductType;
import br.com.sicredi.bank.service.ProductService;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static br.com.sicredi.bank.dto.Constantes.PRODUCT_BUSINESS_SALARY_ERROR;
import static org.hamcrest.Matchers.containsString;

@DisplayName("Product")
@Epic("List Products")
public class ListProductTest {

    ProductService productService = new ProductService();

    @Test
    @Tag("error")
    @Description("Deve listar produtos com salário entre 1500 e 2999 com sucesso")
    public void mustListProductsWithSalaryBetween1500And2999Successfully() {
        var salary = BigDecimal.valueOf(2000);

        productService.listProducts(salary)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body(containsString(ProductType.PERSONAL.toString()))
                ;

//        assertEquals(1, response.size());
//        assertEquals(ProductType.PERSONAL, response.get(0).getType());
//        assertEquals(4, response.get(0).getTaxes());
    }

    @Test
    @Tag("error")
    @Description("Deve listar produtos com salário entre 3000 e 4999 com sucesso")
    public void mustListProductsWithSalaryBetween3000And4999Successfully() {
        var salary = BigDecimal.valueOf(4000);

        productService.listProducts(salary)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body(containsString(ProductType.PERSONAL.toString()))
                    .body(containsString(ProductType.PAYROLL.toString()))
                ;

//        assertEquals(1, response.size());
//        assertEquals(ProductType.PERSONAL, response.get(0).getType());
//        assertEquals(4, response.get(0).getTaxes());
    }

    @Test
    @Tag("error")
    @Description("Deve listar produtos com salário a partir de 5000 com sucesso")
    public void mustListProductsWithSalaryFrom5000Successfully() {
        var salary = BigDecimal.valueOf(4000);

        productService.listProducts(salary)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body(containsString(ProductType.PERSONAL.toString()))
                    .body(containsString(ProductType.PAYROLL.toString()))
                    .body(containsString(ProductType.FINANCING.toString()))
                ;

//        assertEquals(1, response.size());
//        assertEquals(ProductType.PERSONAL, response.get(0).getType());
//        assertEquals(4, response.get(0).getTaxes());
    }

    @Test
    @Tag("all")
    @Description("Deve não listar produtos com salário abaixo de 1500")
    public void mustNotListProductsWithSalaryBelow1500() {
        var salary = BigDecimal.valueOf(1499);

        productService.listProducts(salary)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(containsString(PRODUCT_BUSINESS_SALARY_ERROR))
                ;
    }

}
