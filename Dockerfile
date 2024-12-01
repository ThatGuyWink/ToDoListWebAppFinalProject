FROM openjdk:8-jre
EXPOSE 8080
ADD target/ToDoListWebApp-1.0-SNAPSHOT ToDoListWebApp-1.0-SNAPSHOT.war
ENTRYPOINT ["java", "-jar", "/app/Project-Web-App.jar"]
