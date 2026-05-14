package com.gft.warehouse.warehouseworkshop.infrastructure.productProvider;

import com.gft.warehouse.warehouseworkshop.application.dto.ExternalProductDTO;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.Yaml;
import java.util.List;
import java.util.Map;

@Builder
@Service
public class ProductLoaderService {
    private final String GITHUB_URL = "https://raw.githubusercontent.com/Esmeralda6/workshop-products-config/refs/heads/main/products/products-rules.yml";

    @SuppressWarnings("unchecked")
    public List<ExternalProductDTO> fetchProducts() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String yamlContent = restTemplate.getForObject(GITHUB_URL, String.class);
            System.out.println("=== CONTENT RECEIVED ON GITHUB ===");
            System.out.println(yamlContent);
            System.out.println("=====================================");

            if (yamlContent == null || yamlContent.isEmpty()) {
                System.err.println("The file downloaded on GitHub is empty");
                return List.of();
            }

            Yaml yaml = new Yaml();
            Map<String, Object> loadedYaml = yaml.load(yamlContent);

            List<Map<String, Object>> productsList = (List<Map<String, Object>>) loadedYaml.get("products");

            if (productsList == null) {
                System.err.println("Root 'products' does not exist on YAML.");
                return List.of();
            }

            return productsList.stream().map(map ->
                    ExternalProductDTO.builder()
                            .id(String.valueOf(map.get("id")))
                            .build()
            ).toList();

        } catch (Exception e) {
            System.err.println("Connection error");
            e.printStackTrace();
            throw e;
        }
    }
}
