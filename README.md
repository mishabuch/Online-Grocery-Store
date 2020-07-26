# Online-Grocery-Store
Online Grocery Store

Online Grocery is an online store where you can buy groceries and have them delivered at your doorstep. This file will describe the various entities and their relationships. Also, will describe how to run the project, as well as how to test various API's.

EER Diagram For the Store is As Follows:

![Online Grocery Store EER](/images/eer.png)

A User is associated with a single Cart.
A User can have many Orders.
A Cart can have many "Order-Product" entities.
Many Products can belong to an order.
An order is created on Cart checkout.

For detailed information on various API specs, go to the /docs/specs folder.

# Technologies Used

* Java 
* Spring Boot 2.2
* H2 Database
* Spring Data JPA
* Hibernate
* Maven

