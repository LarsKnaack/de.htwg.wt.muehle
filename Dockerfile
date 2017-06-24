FROM ingensi/oracle-jdk
MAINTAINER Ingensi labs <contact@ingensi.com>

RUN yum update -y && yum install -y unzip
RUN curl -O https://downloads.typesafe.com/typesafe-activator/1.3.12/typesafe-activator-1.3.12.zip
RUN unzip typesafe-activator-1.3.12.zip -d / && rm typesafe-activator-1.3.12.zip && chmod a+x /activator-1.3.12/activator
ENV PATH $PATH:/activator-1.3.12

EXPOSE 9000 8888
EXPOSE 8080 8080
RUN mkdir /app
WORKDIR /app

CMD ["activator", "run"]