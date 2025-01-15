package server.models.customer;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class CustomerInstanceCreator implements InstanceCreator<Customer> {


    private final JsonObject jsonObject;

    public CustomerInstanceCreator(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public Customer createInstance(Type type) {
        CustomerType customerType = CustomerType.valueOf(jsonObject.get("type").getAsString());

        return switch (customerType) {
            case NEW -> new NewCustomer(jsonObject);
            case RETURNING -> new ReturningCustomer(jsonObject);
            case VIP -> new VIPCustomer(jsonObject);
        };
    }
}
