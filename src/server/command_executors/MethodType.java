package server.command_executors;

public enum MethodType {
    // Authentication
    LOGIN,
    LOGOUT,

    // Employee Management
    ADD_EMP,
    REMOVE_EMP,
    EDIT_EMP,
    SHOW_BRANCH_EMPLOYEE,
    GET_ALL_EMP,
    IS_EMPLOYEE_EXISTS,

    // Customer Management
    ADD_CUSTOMER,
    DELETE_CUSTOMER,
    GET_ALL_CUSTOMERS,

    // Inventory and Sales
    GET_INVENTORY_BY_BRANCH,
    SALE_PRODUCT,
    SHOW_SALES_BY_PRODUCT,
    SHOW_SALES_BY_BRANCH,
    SHOW_SALES_BY_DATE,
    ADD_PRODUCT_TO_BRANCH,

    // Chat Management
    WAITING_FOR_CHAT_REQUEST,
    IS_SHIFT_MANAGER,
    GET_BRANCH_ID,
    REMOVE_FROM_WAITING_LIST,
    REMOVE_FROM_AVAILABLE_LIST,
    GET_OPTIONAL_CHAT,
    AVAILABLE_FOR_CHAT_REQUEST,
    JOIN_EXIST_CHAT,
    START_CHAT_MESSAGE,
    CLOSE_CHAT,
    SEND_MESSAGE,
    OPEN_CHAT
}
