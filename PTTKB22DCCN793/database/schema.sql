CREATE DATABASE IF NOT EXISTS qlst
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE qlst;

DROP TABLE IF EXISTS tblOrderProduct;
DROP TABLE IF EXISTS tblOnlineOrder;
DROP TABLE IF EXISTS tblSalesProduct;
DROP TABLE IF EXISTS tblSalesInvoice;
DROP TABLE IF EXISTS tblImportProduct;
DROP TABLE IF EXISTS tblImportInvoice;
DROP TABLE IF EXISTS tblProduct;
DROP TABLE IF EXISTS tblSupplier;
DROP TABLE IF EXISTS tblCustomer;
DROP TABLE IF EXISTS tblStaff;

CREATE TABLE tblStaff (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash CHAR(64) NOT NULL,
    phone_number VARCHAR(20),
    email VARCHAR(150),
    address VARCHAR(255),
    role VARCHAR(50) NOT NULL
);

CREATE TABLE tblCustomer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash CHAR(64) NOT NULL,
    phone_number VARCHAR(20),
    date_of_birth DATE,
    address VARCHAR(255),
    is_member BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE tblSupplier (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150),
    phone_number VARCHAR(20),
    address VARCHAR(255)
);

CREATE TABLE tblProduct (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    type VARCHAR(100),
    unit VARCHAR(50),
    stock_quantity INT NOT NULL DEFAULT 0,
    unit_price DECIMAL(15,2) NOT NULL DEFAULT 0
);

CREATE TABLE tblImportInvoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_id INT NOT NULL,
    warehouse_staff_id INT NOT NULL,
    import_date DATETIME NOT NULL,
    total DECIMAL(15,2) NOT NULL,
    CONSTRAINT fk_import_supplier FOREIGN KEY (supplier_id) REFERENCES tblSupplier(id),
    CONSTRAINT fk_import_staff FOREIGN KEY (warehouse_staff_id) REFERENCES tblStaff(id)
);

CREATE TABLE tblImportProduct (
    id INT AUTO_INCREMENT PRIMARY KEY,
    import_invoice_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(15,2) NOT NULL,
    note VARCHAR(255),
    CONSTRAINT fk_import_product_invoice FOREIGN KEY (import_invoice_id) REFERENCES tblImportInvoice(id),
    CONSTRAINT fk_import_product_product FOREIGN KEY (product_id) REFERENCES tblProduct(id)
);

CREATE TABLE tblSalesInvoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    sales_staff_id INT,
    sales_date DATETIME NOT NULL,
    total DECIMAL(15,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_sales_customer FOREIGN KEY (customer_id) REFERENCES tblCustomer(id),
    CONSTRAINT fk_sales_staff FOREIGN KEY (sales_staff_id) REFERENCES tblStaff(id)
);

CREATE TABLE tblSalesProduct (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sales_invoice_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(15,2) NOT NULL,
    CONSTRAINT fk_sales_product_invoice FOREIGN KEY (sales_invoice_id) REFERENCES tblSalesInvoice(id),
    CONSTRAINT fk_sales_product_product FOREIGN KEY (product_id) REFERENCES tblProduct(id)
);

CREATE TABLE tblOnlineOrder (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    warehouse_staff_id INT,
    delivery_staff_id INT,
    order_date DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL,
    total DECIMAL(15,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES tblCustomer(id),
    CONSTRAINT fk_order_warehouse_staff FOREIGN KEY (warehouse_staff_id) REFERENCES tblStaff(id),
    CONSTRAINT fk_order_delivery_staff FOREIGN KEY (delivery_staff_id) REFERENCES tblStaff(id)
);

CREATE TABLE tblOrderProduct (
    id INT AUTO_INCREMENT PRIMARY KEY,
    online_order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(15,2) NOT NULL,
    CONSTRAINT fk_order_product_order FOREIGN KEY (online_order_id) REFERENCES tblOnlineOrder(id),
    CONSTRAINT fk_order_product_product FOREIGN KEY (product_id) REFERENCES tblProduct(id)
);

INSERT INTO tblStaff(full_name, username, password_hash, phone_number, email, address, role) VALUES
('Nguyễn Văn Quản', 'manager', 'cc6e9ea404e9dc39423f8b67b72a9fb8d38614ee7f998b45d24826dd20bbf90b', '0900000001', 'manager@example.com', '123 Đường A, Hà Nội', 'MANAGER'),
('Trần Thị Kho', 'warehouse', 'ed5b248cb4fd49d76d8607de61d8ab5729bdfdb4dca8fe3ae88bc52f99ed53e5', '0900000002', 'warehouse@example.com', '456 Đường B, Hà Nội', 'WAREHOUSE'),
('Lê Văn Bán', 'sales', 'c8bc59560d7b831ee66a3f343c6fd3afe30eb4f2c10ee172792418c5e265985c', '0900000003', 'sales@example.com', '789 Đường C, Hà Nội', 'SALES');

INSERT INTO tblSupplier(name, email, phone_number, address) VALUES
('Công ty Điện máy Việt', 'contact@dienmayviet.vn', '0901112222', '12 Nguyễn Trãi, Hà Nội'),
('Công ty Gia dụng Phương Nam', 'sale@phuongnam.vn', '0903334444', '88 Hai Bà Trưng, Đà Nẵng');

INSERT INTO tblProduct(name, type, unit, stock_quantity, unit_price) VALUES
('Máy giặt Inverter 9kg', 'Máy giặt', 'Chiếc', 10, 7800000),
('Tivi QLED 55 inch', 'Tivi', 'Chiếc', 5, 15900000),
('Nồi cơm điện 1.8L', 'Gia dụng', 'Chiếc', 20, 890000);
