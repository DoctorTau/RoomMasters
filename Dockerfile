FROM adoptopenjdk/openjdk12:jdk-12.0.2_10

WORKDIR /home/RoomMasters
COPY ./Backend /home/RoomMasters
ENV DB_PATH /home/RoomMasters/db.sqlite

CMD java -jar ./out/artifacts/Backend_jar/Backend.jar