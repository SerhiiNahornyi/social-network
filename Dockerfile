FROM maven:3.8.3-openjdk AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY checkstyle.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip=true


FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/social-network.jar /usr/local/lib/social-network.jar
EXPOSE 8090
CMD ["java","-jar","/usr/local/lib/social-network.jar"]
