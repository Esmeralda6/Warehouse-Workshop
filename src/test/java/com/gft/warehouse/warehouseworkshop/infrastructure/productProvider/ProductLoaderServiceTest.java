package com.gft.warehouse.warehouseworkshop.infrastructure.productProvider;

import com.gft.warehouse.warehouseworkshop.application.dto.ExternalProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class ProductLoaderServiceTest {

    @Autowired
    private ProductLoaderService productLoaderService;

    @Test
    void shouldFetchProductsFromGitHubSuccessfully() {
        List<ExternalProductDTO> products = productLoaderService.fetchProducts();

        assertNotNull(products, "The list downloaded should not be null");
        assertFalse(products.isEmpty(), "The file on GitHub has products, the list shuld not be empty");

        assertNotNull(products.get(0).getId(), "The ID can't be null");

        System.out.println("====== PRODUCTS RECOVERED IN TEST ======");
        products.forEach(p -> System.out.println("ID: " + p.getId() + " -> Name: " + p.getName()));
        System.out.println("==========================================");
    }
}