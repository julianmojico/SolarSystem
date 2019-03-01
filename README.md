## SolarSystem
SolarSystem simulation model based in Java used to predict weather

## :one: Install and start mongodb
```
sudo apt-get install -y mongodb-org
sudo mkdir -p /data/db
mongod
```

## :two: Create database :white_check_mark:

```
mongo
use SolarSystem
```
-SolarSystem empty database will be created
```
db.enableFreeMonitoring()
```
-The above command provides a permanent link to monitor your db stats online. :bar_chart:

## :three: Install maven :wrench:
`sudo apt install maven`

## :four: clone repo :page_facing_up:
`sudo git clone https://github.com/cepix/SolarSystem/`

## :five: edit your profile (default is localhost) :bangbang:
-edit env profile in application.properties and create application-[profile].properties properties depending on your enviroment

## :six: build app :four_leaf_clover:
```
sudo mvn verify
sudo mvn test
sudo mvn install
```

## :seven: start app :rocket:
`nohup java -jar /var/www/SolarSystem/target/SolarSystem-1.0-SNAPSHOT.jar &`

## :eight: Point browser to Swagger UI and start using REST API :bowtie:
`http://hostname:8080/swagger-ui.html`

![alt text](https://github.com/cepix/SolarSystem/blob/master/solarsystem%20swagger.jpg)
