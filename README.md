# Spring-boot-Prime-numbers-Producer
Demo project of Microservices with Spring Boot - Producer app. This app generates random numbers and sends them to Consumer. Generated numbers are saved to CSV file with default path '**/temp/producer**' which can be configured.

## To run projects from command line, make sure you have Java 11 installed and maven. <br/>
First build and run my other app - Consumer: https://github.com/avenge79/Spring-boot-Prime-numbers-Consumer.git.<br/>
To build this app, first execute
**mvn clean install**<br/>
Then go to **'target'** folder and run the project with<br/>
**java -jar producer-app-1.0.0.jar**<br/>
<br/>
Some properties can be configurd inside '**application.yml**'.<br/>

## Run with Docker:<br/>
Make sure you have **Docker** installed. Build both projects with **Maven** as described above. First create a Docker network for both projects:<br/>
**docker network create consumer-producer**<br/>
Then build Docker image:<br/>
**docker build -t producer-app -f Dockerfile .**<br/>
After that, use **docker-compose** to run the image using provided '**docker-compose.yml*'<br/>
**docker-compose up --detach**<br/>
<br/>
The default folder for output CSV files with generated numbers is '**/temp**'. You can change it in '**application.yml**' by changing the property<br/>
**csv:
  file:
    folder: /temp/producer**<br/>
Numbers will be appended to the file, so make sure if you want clean results to delete or rename previous files or folder before running the app. If you want to change it for Docker images, change these in **docker-compose.yml**.<br/>
You can also use websocket to send numbers, just set in application.yml<br/>
websocket:
  use: false<br/>
to '**true**' and for Docker<br/>
WEBSOCKET_USE: false<br/>
to '**true**' in '**docker-compose.yml*'.<br/>

Integration tests can be run with<br/>
**mvn test**<br/>
Make sure '**consumer**' is running.
