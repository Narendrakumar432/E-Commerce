# 🛒 E-commerce Backend System

A complete, secure **RESTful backend API** built with **Java Spring Boot**, supporting operations such as managing **users, products, categories, cart, orders**, and **payments (Razorpay)** with **JWT authentication** and **role-based access control**.

---

## 📌 Project Overview

The E-commerce Backend System is designed to simulate real-world online shopping platforms like Amazon. It allows users to browse products, manage carts, place orders, and perform secure payments.

---

## 🏗️ Architecture

```
┌──────────────────────────────────────────────┐
│           Postman / Client                   │
└─────────────────────┬────────────────────────┘
                      │ HTTP Request
┌─────────────────────▼────────────────────────┐
│     Spring Security + JWT Filter             │
│     (Validates Bearer Token)                 │
└─────────────────────┬────────────────────────┘
                      │
┌─────────────────────▼────────────────────────┐
│            Controller Layer                  │
│  Auth | Product | Category | Cart            │
│  Order | Payment | Address                   │
└─────────────────────┬────────────────────────┘
                      │
┌─────────────────────▼────────────────────────┐
│             Service Layer                    │
│  Business Logic & Validation                 │
└─────────────────────┬────────────────────────┘
                      │
┌─────────────────────▼────────────────────────┐
│           Repository Layer                   │
│      (Spring Data JPA)                       │
└─────────────────────┬────────────────────────┘
                      │
┌─────────────────────▼────────────────────────┐
│          MySQL Database                      │
│ users | products | categories | cart         │
│ orders | payments | addresses                │
└──────────────────────────────────────────────┘
```

---

## ✨ Features

### 🔐 Authentication & Security

* JWT Authentication
* Role-based access (ADMIN, USER)
* Secure endpoints using Spring Security

### 📦 Product & Category Management

* Admin can create/update/delete categories
* Admin can add/manage products
* Users can view all products

### 🛒 Cart Management

* Add products to cart
* Update quantity
* Remove items
* Auto total price calculation

### 📦 Order Management

* Place order from cart
* Link order with user & address
* Order status tracking (PENDING)

### 💳 Payment Integration (Razorpay)

* Create payment order
* Verify payment
* One order → one payment constraint
* Secure validation

### 📍 Address Management

* Add multiple addresses
* Mark default address
* Used during order placement

---

## 🛠️ Tech Stack

| Technology      | Purpose              |
| --------------- | -------------------- |
| Java 17         | Programming language |
| Spring Boot     | Backend framework    |
| Spring Security | Authentication       |
| JWT             | Token-based security |
| Spring Data JPA | ORM                  |
| Hibernate       | JPA implementation   |
| MySQL           | Database             |
| Razorpay        | Payment integration  |
| Maven           | Build tool           |
| Postman         | API testing          |

---

## 📁 Project Structure

```
ecommerce/
├── config/
│   ├── SecurityConfig.java
│   ├── JwtFilter.java
│   ├── JwtService.java
├── controller/
│   ├── AuthController.java
│   ├── ProductController.java
│   ├── CategoryController.java
│   ├── CartController.java
│   ├── OrderController.java
│   ├── PaymentController.java
│   └── AddressController.java
├── service/
├── repository/
├── entity/
├── dto/
├── exception/
└── resources/
```

---

## 🗄️ Database Schema

### Tables:

**users**

```sql
id | name | email | password | role
```

**categories**

```sql
id | name
```

**products**

```sql
id | name | description | price | stock | category_id
```

**cart**

```sql
id | user_id
```

**cart_items**

```sql
id | cart_id | product_id | quantity
```

**orders**

```sql
id | user_id | total_amount | status
```

**order_items**

```sql
id | order_id | product_id | quantity | price
```

**payments**

```sql
id | order_id | amount | razorpay_order_id | status
```

---

## 🚀 Getting Started

### Prerequisites

* Java 17+
* MySQL
* Maven

### Installation

```bash
git clone https://github.com/YOUR_USERNAME/ecommerce-backend.git
cd ecommerce-backend
```

### Configure DB

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
```

### Run

```bash
mvn spring-boot:run
```

---

## 📋 API Endpoints

### 🔐 Auth

* POST /api/auth/register
* POST /api/auth/login

### 📦 Products

* GET /api/products
* POST /api/admin/products

### 🛒 Cart

* POST /api/cart/add
* GET /api/cart

### 📦 Orders

* POST /api/orders
* GET /api/orders

### 💳 Payments

* POST /api/payments/create
* POST /api/payments/verify

---

## 🔒 Security Flow

```
Login → Get JWT → Use Token → Access APIs
```

---

## ⚠️ Important Rules

* Same user must perform Cart → Order → Payment
* One order can have only one payment
* Payment verification requires valid Razorpay response

---

## 👨‍💻 Developer

Narendra Kumar
Java Backend Developer

---
