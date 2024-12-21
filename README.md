# Java-Console-Based-Pharmacy-Information-System

This project is a Java console application designed to manage pharmacy operations. It includes prescription validation, stock management, customer handling, and transaction processing. The system follows the MVC pattern for organized and maintainable code. Created by Jeorgie C. Estrada for Armada Logics.

## Features

### Login
1. Mask password or hide if not supported by IDE
2. Simple Encrypt/Decrypt Password
3. Login Attempt Counter

### Medicine Management
1. Add Medicine
2. Update Medicine
3. View List of Medicines
4. Delete Medicine
5. Search Medicine
6. Check Expired Medicines (with Alert)
7. Check Low-Level Stocks
8. Re-Stocking Medicine

### Customer Management
1. View All Customers
2. Add Customer
3. Edit/Update Customer
4. Delete Customer

### Prescription Checker and Validation
1. Check if customer has a prescription and if it is valid

### Dispensing/Selling Medicine Process
1. Check if the customer is an existing customer or a walk-in
2. Check if the customer has a prescription and if it is valid
3. Validate the medicine based on steps 1 and 2
4. Do not dispense medicine if it is expired
5. Continue adding medicines to the cart
6. Upon checkout, proceed to the billing summary
7. Handle payment and change
8. If successful, record the transaction in sales history and customer history

### Stock Management
1. Check low stocks and re-stock

### Reports
1. Sales Report (Total sales, date generated, and who generated)
   - Today
   - Specific Date
   - Date Range
   - By Cashier

2. Inventory Report (Total stock value, date generated, and who generated)
   - Today
   - Detailed Inventory (by Date Range)

3. Customer History
   - Specific customer
   - Specific by date range
   - All customer history
   - All customer history by date range

4. Re-stocking History

### User Roles
- **Superadmin**
  1. Create User (encrypt password)
  2. View Users
  3. Update User (encrypt password)
  4. Delete User
  5. Set Pharmacy Name

- **Pharmacist**
  1. Manage Medicine
  2. Check Prescription
  3. Dispense Medicine
  4. View Reports
  5. Customer Management

- **Assistant**
  1. Manage Medicine
  2. Check Prescription
  3. Dispense Medicine
  4. View Reports
  5. Customer Management

- **Guest/Unregistered**
  1. Search Medicine for Inquiry/availability

## Requirements
- Java Development Kit (JDK) 8 or higher

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/pharmacy-information-system.git
   
2. Navigate to the project directory:
   cd pharmacy-information-system

3. Compile the project:
   javac PharmacyIS.java

4. Run the application:
   java PharmacyIS
