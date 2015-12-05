FROM java:8-jdk

ENV HOME /home/cfp
RUN useradd -c "CFP user" -d $HOME -m cfp

EXPOSE 8080
EXPOSE 8443

WORKDIR $HOME
ENTRYPOINT java -jar call-for-paper.jar

USER cfp
ADD call-for-paper.jar $HOME
ADD application.properties $HOME
