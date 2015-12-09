FROM java:8-jdk

ENV HOME /home/cfp
RUN groupadd -g 10000 cfp && \
    useradd -u 10000 -g 10000 -c "CFP user" -d $HOME -m cfp

EXPOSE 8080
EXPOSE 8443

WORKDIR $HOME
ENTRYPOINT java -jar call-for-paper.jar
VOLUME ["$HOME/config"]

USER cfp
ADD call-for-paper.jar $HOME
