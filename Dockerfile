FROM adoptopenjdk/openjdk12:jdk-12.0.2_10

WORKDIR /home/RoomMasters
COPY ./Backend/ .
RUN touch db.sqlite
ENV DB_PATH /home/RoomMasters/db.sqlite

CMD javac ./src/main/java/com/github/bogdan/Main.java

