# Antique Auction

## Prerequisites:

1. install java 8 or newer 
2. install apache maven 
3. install mysql server
4. create schema with name 'auction':
```bash
create schema auction;
```
## Build project:
1. Clone [repository](https://github.com/ElenaGrigorets/antique-auction.git)
2. Build project from project directory with 
```bash
mvn clean install
```
Jar file will appear in **target** folder.
As an option build could be avoided. Ready to use **auction-0.0.1-SNAPSHOT.jar**
 file is inside project directory.

## Run project:
1. Unzip antique-auction-images.zip (which is in project directory) to 
user home folder ( in Linux: /home/[username]/ ). 

2. Open terminal in folder where **auction-0.0.1-SNAPSHOT.jar** is and run command:
```bash
java -jar auction-0.0.1-SNAPSHOT.jar
```
3. Go to localhost:8081 in browser.
4. Login into the system with: admin/admin or user/user
