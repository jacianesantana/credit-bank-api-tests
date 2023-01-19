package br.com.sicredi.bank.aceitacao.product;

import br.com.sicredi.bank.dto.enums.ProductType;
import br.com.sicredi.bank.dto.response.product.ProductResponse;
import br.com.sicredi.bank.service.ProductService;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Product")
@Epic("List Products")
public class ListProductTest {

    ProductService productService = new ProductService();

    @Test
    @Tag("error")
    @Description("Deve listar produtos com salário entre 1500 e 2999 com sucesso")
    public void mustListProductsWithSalaryBetween1500And2999Successfully() {
        var salary = BigDecimal.valueOf(2000);

        List<ProductResponse> response = productService.listProducts(salary)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(List.class)
                    //.body(containsString("PESSOAL"))
                ;

        assertEquals(1, response.size());
        assertEquals(ProductType.PESSOAL, response.get(0).getType());
        assertEquals(4, response.get(0).getTaxes());
    }

}
