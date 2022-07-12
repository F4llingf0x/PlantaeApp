# Vizsgaremek - Plantae App

## Applikáció leírás

A Plantae App egy szobanövény felügyelő applikáció, amely segítséget nyújt a szobanövények megfelő gondozásához. 
Amennyiben különféle szenzorokkal látjuk el a növényeinket (hőmérséklet, páratartalom, talajnedvesség), lehetőségünk
van a szenzorok inicializálása után azok felügyeletére. Az adatbázisba felvihető növényeknek be lehet állítani 
különféle határértékeket különféle sensortípusoknak, amely meghaladása a növény károsodásával járhat. A szenzorok az
inicializálás folyamtán keresztül egyedi azonosítóval lesznek ellátva, és beállításra kerül a mintavételezési periódus.
A szenzorok programját a vizsgaremek nem tartalmazza. A program vizsgálja, hogy az adott növényekhez tartozó szenzorok
utolsó mérései nem haladják-e meg a megszabott határértéket, amennyiben meghaladják, jelzést küld a megfelelő
információval, amellyel lokalizálható a probléma. A törlés minden esetben "soft delete", azaz helyreállítható törlés.
Lehetőségünk van a létrehozás és törlés mellett különféle lekérdezésekre, és módosításokra.
A végpontok részletes leírása a 
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config
címen érhető el, a program futtatása után.


## Applikáció használata
### docker hálózat létrehozása
docker network create plantaenetwork

### docker mysql konténer létrehozása
docker run --name plantaedb --network plantaenetwork -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=plantae -d -p 3310:3306 mysql:latest

### alkalmazás buildelése
konzolból futtatás:
mvn clean package
utasítást a vizsgaremek-F4llingf0x mappában állva 
tesztek futtatása nélkül:
mvn clean package -DskipTests

### docker image létrehozása
docker build -t plantaeapp .

### docker konténer létrehozása és indítása
docker run --name plantaeapp --network plantaenetwork -p 8080:8080 -d plantaeapp
