##SolarSystem
SolarSystem simulation model based in Java used to predict weather

##Install and start mongodb
```
sudo apt-get install -y mongodb-org
sudo mkdir -p /data/db
mongod
```

##Create database

```
mongo
use SolarSystem
```
**SolarSystem empty database will be created**
```
db.enableFreeMonitoring()
The above command provides a permanent link to monitor your db stats online.
```

##Install maven
`sudo apt install maven`

##clone repo
`sudo git clone https://github.com/cepix/SolarSystem/`

##edit your profile (default is localhost)
-edit env profile in application.properties and create application-[profile].properties properties depending on your enviroment

##build app
```
sudo mvn verify
sudo mvn test
sudo mvn install
```

##start app
`nohup java -jar /var/www/SolarSystem/target/SolarSystem-1.0-SNAPSHOT.jar &`

##Point browser to:
`http://hostname:8080/swagger-ui.html`



