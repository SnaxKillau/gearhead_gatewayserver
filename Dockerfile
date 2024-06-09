FROM openjdk:17 as build

# Information of owner or maintainer of image
MAINTAINER gearhead

# Add the application's jar to the container
COPY target/swiggy-gateway-0.0.1-SNAPSHOT.jar swiggy-gateway-0.0.1-SNAPSHOT.jar

#Execute the application
ENTRYPOINT ["java", "-jar","/swiggy-gateway-0.0.1-SNAPSHOT.jar"]