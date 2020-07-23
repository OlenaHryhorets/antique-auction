# Antique Auction

## Prerequisites:

1. Install java 8 or newer 
2. Install mysql server
3. Create schema with name 'auction':
```bash
create schema auction;
```
## Build project:

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
(in Linux: /home/[username]/). 

2. Open terminal in folder where **auction-0.0.1-SNAPSHOT.jar** is and run command:
```bash
java -jar auction-0.0.1-SNAPSHOT.jar
```
3. Go to _http://localhost:8081_ in browser.
4. Login into the system with: admin/admin or user/user
