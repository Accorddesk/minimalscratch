FROM openjdk:14-alpine
COPY build/libs/myAppJdk11KotlinExtensions-*-all.jar myAppJdk11KotlinExtensions.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "myAppJdk11KotlinExtensions.jar"]