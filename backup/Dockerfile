FROM ubuntu
MAINTAINER team@breizhcamp.org
ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update \
    && apt-get install -yq --no-install-recommends mysql-client \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* \
    && rm -rf /tmp/* 

ADD backup.sh /backup.sh

CMD ["sh", "backup.sh"]
