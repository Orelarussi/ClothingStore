package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.models.SaleReport;
import server.services.SalesManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SalesManagerTest {
    private SalesManager salesManager;

    @BeforeEach
    public void setUp() {
        salesManager = new SalesManager();
    }

    @Test
    public void testAddSaleReport() {
        SaleReport sale = new SaleReport("Branch01", "T-Shirt", "Clothing", 10, 199.9, LocalDate.now());
        salesManager.addSaleReport(sale);

        assertEquals(1, salesManager.getAllSaleReports().size());
    }
}
