FROM openjdk:17-jdk-alpine
# Add a volume pointing to /tmp
VOLUME /tmp
# The application's jar file
ARG JAR_FILE=target/GestionCompte-0.0.1-SNAPSHOT.jar
# Add the application's jar to the container
ADD ${JAR_FILE} GestionCompte.jar
# Expose port 8085
EXPOSE 8085
# Run the jar file
ENTRYPOINT ["java", "-jar", "/GestionCompte.jar"]
