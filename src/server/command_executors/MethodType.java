package server.command_executors;

public enum MethodType {
    LOGIN, LOGOUT,
    ADD_EMP, REMOVE_EMP,
    GET_INVENTORY_BY_BRANCH, SALE_PRODUCT,
    ADD_CUSTOMER, DELETE_CUSTOMER, GET_ALL_CUSTOMERS,

    WAITING_FOR_CHAT_REQUEST,IS_SHIFT_MANAGER,GET_BRANCH_ID,
    REMOVE_FROM_WAITING_LIST, REMOVE_FROM_AVAILABLE_LIST
    //TODO: add others methods
}
