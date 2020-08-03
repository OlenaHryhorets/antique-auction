# Antique Auction
Web auction application for an antique items seller.<br />Admin user(login:admin, password:admin) can add, edit or delete items.<br />Users can bid on items and log into their profile to check the status of items.

##In order to run this project from Docker you'll need:
1. docker installed
2. run the followings commands:  

```bash
sudo docker run -d -p 3308:3306 -e MYSQL_DATABASE=auction -e MYSQL_ROOT_PASSWORD=root mysql
sudo docker pull hryhorets/auction-image:latest
sudo docker run --network host 45799bd4bde4
```
3. Go to _http://localhost:8081_ in browser.
##To run this project from Github
##Prerequisites:
1. Install java 8 or newer 
2. Install mysql server
3. Create schema with name 'auction':
```bash
create schema auction;
```
##Build project:

1. Install apache maven 
2. Clone [repository](https://github.com/ElenaGrigorets/antique-auction.git)
3. Build project from project directory with 
```bash
mvn clean install
```
Jar file will appear in **target** folder.
As an option build could be avoided. Ready to use **auction-0.0.1-SNAPSHOT.jar**
 file is inside project directory.

## Run project:
1. Unzip antique-auction-images.zip (which is in project directory) to 
user home folder  
( in Linux: /home/[username]/ ) 

2. Open terminal in folder where **auction-0.0.1-SNAPSHOT.jar** is and run command:
```bash
java -jar auction-0.0.1-SNAPSHOT.jar
```
3. Go to _http://localhost:8081_ in browser.
4. Login into the system with default: admin/admin or user/user

##REST API Documentation
Open in Swagger-UI:  
http//localhost:8081/swagger-ui.html  
Open in Postman:  
http://localhost:8081/v2/api-docs
