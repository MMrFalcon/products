# Products app
[![CircleCI](https://circleci.com/gh/MMrFalcon/products.svg?style=svg)](https://circleci.com/gh/MMrFalcon/products)
## Basic Auth with CentOS image and MSSQL example

## HOW TO RUN (WINDOWS)

### Database by docker

Open up your PowerShell and type

`docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=YoUr!2PassworD"  -p 1433:1433 --name sql1 -d mcr.microsoft.com/mssql/server:2017-latest `

`docker exec -it sql1 "bash"`

`/opt/mssql-tools/bin/sqlcmd -S localhost -U SA `

Type password

`CREATE DATABASE TestDB`
`GO`

`USE TestDB`

Create table for products and add some value 

 `CREATE TABLE Product (id INT, name NVARCHAR(50), quantity INT)
 INSERT INTO Product VALUES (1, 'banana', 150); INSERT INTO Product VALUES (2, 'orange', 154);
 GO`
 
  Create table for User and add user with BCrypt encoded password
  
 `CREATE TABLE User_Android (id INT NOT NULL UNIQUE, login varchar(100) UNIQUE, password varchar(255))`
 
 Example with "admin" password
 
 `INSERT INTO User_Android (id, login, password) VALUES (1, 'admin', '{bcrypt}$2a$04$0avIpWBWXVSQ9Oo6/Zm2RuukBi/iuxvvidnoIA4uKln59sxVgVvq2')`

***

Go to `application-dev.properties` and change `spring.datasource.url=jdbc:sqlserver://192.168.100.2:1433;databaseName=TestDB` to your local machine address

Change password in the same place of file

***
### Run application in CentOS image

Open your PowerShell and change directory to the root level of the application and type:

`./mvnw clean package`

When job is done it is possible to build docker image from fat jar of packaged application by typing:

`docker build -t image_name .`

This command run Dockerfile from root level of the application. Now it is time to run this image by typing:

`docker run -d -p 8080:8080 image_name`

Type `docker logs image_name` if you want to be ensure that application run.

Application works on localhost:8080

## OPTIONAL Create Procedure

``` 
CREATE PROCEDURE dbo.testProductCreation 
 	
 	@Id int,
 	
 	@Name nvarchar(50),
 	
 	@Quantity int = 1
 	
 AS
 
 BEGIN 	
 
 	SET NOCOUNT ON; 
 	
 	INSERT INTO Product(id, name, quantity) VALUES (@Id, @Name, @Quantity)
 END
 
 GO
```
 

## JSON example 

* **First add BasicAuth. For this example admin admin Postman: Authorization - Type: Basic Auth**

* **Ensure that Database and Image are working  by typing `docker ps`**

* **Post method for endpoint:** localhost:8080/product/create

`{
	"name":"banana2",
	"quantity": 12
}`

* **Optional - execute ms sql procedure by post method:** localhost:8080/product/create/procedure

`{
    "id":3000,
	"name":"banana2",
	"quantity": 12
}`


