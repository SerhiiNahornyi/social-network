# Step : Test and package
FROM maven:3.8.3-openjdk as build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY checkstyle.xml .
COPY src/ /build/src/
RUN mvn package

# Step : Package image
FROM openjdk:11-jre-slim
COPY --from=build /build/target/social-network.jar /usr/local/lib/social-network.jar
EXPOSE 8090
CMD ["java","-jar","/usr/local/lib/social-network.jar"]
