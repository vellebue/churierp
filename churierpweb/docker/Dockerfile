#Create docker image (Execute from churierpweb root dir):
#docker image build -f ./docker/Dockerfile -t org.bastanchu/churierpweb:1.0-SNAPSHOT .
#Run instance (interactive mode)
#docker run -it --rm -p 8888:8080 org.bastanchu/churierpweb:1.0-SNAPSHOT
#Remove image
#docker image rm org.bastanchu/churierpweb:1.0-SNAPSHOT
FROM tomcat:9.0

ARG version="1.0-SNAPSHOT"
ARG basedir=""
ARG jdbc_url=""
ARG jdbc_username=""
ARG jdbc_password=""
LABEL "component"="churierpweb" "version"=$version

USER root

WORKDIR /usr/local/tomcat

#ENV JDBC_URL=$jdbc_url
#ENV JDBC_USERNAME=$jdbc_username
#ENV JDBC_PASSWORD=$jdbc_password

COPY /target/churierpweb-$version.war /usr/local/tomcat/webapps/churierpweb.war

#COPY /docker/setenv.sh /usr/local/tomcat/bin/setenv.sh

#RUN chmod 0700 /usr/local/tomcat/bin/setenv.sh

#CMD ["start_tomcat.sh"]