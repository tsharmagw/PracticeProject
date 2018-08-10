#This is a docker image to install cassandra and configure it, then add spark to it, then finally spark.
#For environment add git, java 1.8 and scala 2.11
FROM ubuntu 
MAINTAINER demousr@gmail.com 

RUN apt-get update 
RUN apt-get -y install  git
#In future image will get this from some link instead of copying
COPY  apache-cassandra-3.11.2-bin.tar.gz /home/
COPY  spark-2.0.0-bin-hadoop2.7.tgz /home/
COPY  jdk-8u91-linux-x64.tar.gz /home/
COPY  scala-2.11.8.tgz /home/
RUN tar -xvf /home/apache-cassandra-3.11.2-bin.tar.gz 
RUN tar -xvf  /home/spark-2.0.0-bin-hadoop2.7.tgz 
RUN tar -xvf  /home/jdk-8u91-linux-x64.tar.gz 
RUN tar -xvf /home/scala-2.11.8.tgz 
RUN rm /home/apache-cassandra-3.11.2-bin.tar.gz
RUN rm /home/spark-2.0.0-bin-hadoop2.7.tgz
RUN rm /home/jdk-8u91-linux-x64.tar.gz
RUN rm /home/scala-2.11.8.tgz
#renaming folders
RUN mv scala-2.11.8 scala
RUN mv jdk1.8.0_91  java
RUN mv apache-cassandra-3.11.2 cassandra
RUN mv spark-2.0.0-bin-hadoop2.7 spark2.0

#creating directory for logs and data in cassandra
RUN mkdir cassandra/logs
RUN mkdir cassandra/data


RUN echo "JAVA_HOME='/java'" >> ~/.bashrc
RUN echo "SCALA_HOME='/scala'" >> ~/.bashrc
ENV PATH $PATH:/java/bin
ENV PATH $PATH:/scala/bin 
 

RUN git clone https://github.com/filodb/FiloDB.git

