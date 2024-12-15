package tests;

import models.SaleReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.SalesManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

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
