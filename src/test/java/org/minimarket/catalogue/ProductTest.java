package org.minimarket.catalogue;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    void testProductCreation() {
        Product product = new Product("radio", 50.0, 91, "electronics");

        assertEquals("radio", product.getName());
        assertEquals(50.0, product.getPrice());
        assertEquals(91, product.getQuantity());
    }
}

