# ClothingStore

**ClothingStore** is a Java-based application designed to manage various aspects of a clothing retail business, including branch operations, inventory, customer relations, sales reporting, and internal communications.

---

## Structure

### Employee Menu
- **Branch Menu**
  - Show Branch Employees
  - Add Product to Branch
- **Inventory Menu**
  - Show Branch Inventory
  - Sale Product to Customer
- **Customer Menu**
  - Add Customer
  - Delete Customer
  - Show All Customers in Chain
- **Sales Report Menu**
  - Show Sales Amount by Branch
  - Show Sales Amount by Product ID
  - Show Sales Amount by Date
- **Chat Menu**
  - Open Chat
  - Wait for Chat Request
  - **Chat Menu (if Shift Manager)**:
    - Start a New Chat with Another Branch
    - Wait for an Incoming Chat Request
    - Join an Ongoing Chat

### Admin Menu
- Add Employee
- Delete Employee
- Edit Employee
- View All Employees

---

## Features

### Employee Menu

#### Branch Menu
- **Show Branch Employees**: View a list of employees assigned to a specific branch.
- **Add Product to Branch**: Allocate new products to the branch's inventory.

#### Inventory Menu
- **Show Branch Inventory**: Display all products currently available in the branch.
- **Sale Product to Customer**: Process customer purchases and update inventory accordingly.

#### Customer Menu
- **Add Customer**: Register new customers into the system.
- **Delete Customer**: Remove customer records as needed.
- **Show All Customers in Chain**: Access a comprehensive list of customers across all branches.

#### Sales Report Menu
- **Show Sales Amount by Branch**: Generate sales reports filtered by branch.
- **Show Sales Amount by Product ID**: Analyze sales data for specific products.
- **Show Sales Amount by Date**: Review sales performance over selected time periods.

#### Chat Menu
- **Open Chat**: Initiate and participate in internal communications.
- **Wait for Chat Request**: Standby to respond to incoming chat messages.
- **Shift Manager Features**:
  - Start a New Chat with Another Branch.
  - Wait for an Incoming Chat Request.
  - Join an Ongoing Chat.

---

### Admin Menu

- **Add Employee**: Onboard new employees into the system.
- **Delete Employee**: Remove employee records as necessary.
- **Edit Employee**: Update existing employee information.
- **View All Employees**: Access a list of all employees within the organization.

---

## Notes on Login Requirements
- **Admin Menu Access**: To open the Admin Menu, you must log in with admin credentials.
- **Employee Menu Access**: To access the Employee Menu, you must log in with valid employee credentials.

---

## Technologies Used

- **Programming Language**: Java

---

## Installation and Setup

### Clone the Repository
```bash
git clone https://github.com/Orelarussi/ClothingStore.git
cd ClothingStore
