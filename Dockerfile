FROM adoptopenjdk:11-jre-hotspot
RUN mkdir /opt/app
COPY target/vizsgaremek-F4llingf0x-1.0-SNAPSHOT.jar /opt/app/vizsgaremek-F4llingf0x.jar
CMD ["java", "-jar", "/opt/app/vizsgaremek-F4llingf0x.jar"]
