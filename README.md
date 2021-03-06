# Online-Grocery-Store
Online Grocery is an online store where you can buy groceries and have them delivered at your doorstep. This file will describe the various entities and their relationships. Also, will describe how to run the project, as well as how to test various API's. The Project doesnot have a front end yet.

EER Diagram For the Store is As Follows:


![Online Grocery Store EER](/images/eer2.png)

A User is associated with a single Cart.
A User can have many Orders.
A Cart can have many "Order-Product" entities.
Many Products can belong to an order.
An order is created on Cart checkout.

For detailed information on various API specs, go to the /docs/specs folder. The API's can be accessed at http://localhost:8080/

# Technologies Used

* Java 
* Spring Boot 2.2
* H2 Database
* Spring Data JPA
* Hibernate
* Maven

# How to Run 

* You would need java and maven installed on your system to run the JAR file
* To build and run the project, 
1. Go to /Online-Grocery-Store 
2. Run mvn clean install
3. A JAR file with name online-grocery-store-0.0.1-SNAPSHOT.jar will be created in the "target" folder.
4. Run command "java -jar online-grocery-store-0.0.1-SNAPSHOT.jar"
5. This will bootup the project and also create the in memory database.
6. You can access the API's using POSTMAN or DHC.
7. You can access the database by copy pasting this link in your browser "http://localhost:8080/h2-console". The Username is 'sa' and password is empty. 
8. Set the property "spring.datasource.url=jdbc:h2:mem:grocerystoredb".

# Test Flow
* To easily test the system, there are a list of API's you can copy paste and test the system flow. You can find it in /docs/TEST THE SYSTEM

