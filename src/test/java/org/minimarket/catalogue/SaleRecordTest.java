package org.minimarket.catalogue;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SaleRecordTest {

    @Test
    void testSaleRecordCreation() {
        SaleRecord sale = new SaleRecord("radio", 1, 50.0);

        assertEquals("radio", sale.getProductName());
        assertEquals(1, sale.getQuantity());
        assertEquals(50.0, sale.getSubtotal());
    }
}

