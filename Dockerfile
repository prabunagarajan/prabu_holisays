FROM openjdk:8
# tzdata for timzone
RUN apt-get install -y tzdata
 
# timezone env with default
ENV TZ Asia/Kolkata
WORKDIR /usr/src/myapp
COPY target/devarCabs.jar /usr/src/myapp/ROOT.jar
EXPOSE 3506
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "ROOT.jar"]



