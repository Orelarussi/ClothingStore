package server.models.purchase_plan;

import com.google.gson.InstanceCreator;
import server.models.customer.CustomerType;

import java.lang.reflect.Type;

public class PurchasePlanInstanceCreator implements InstanceCreator<PurchasePlan> {

    private final CustomerType customerType;

    public PurchasePlanInstanceCreator(CustomerType customerType) {
        this.customerType = customerType;
    }

    @Override
    public PurchasePlan createInstance(Type type) {
       return switch (customerType){
           case VIP -> new NewCustomerPurchasePlan();
           case NEW -> new ReturningCustomerPurchasePlan();
           case RETURNING -> new VIPCustomerPurchasePlan();
       };
    }
}
