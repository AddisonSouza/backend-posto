FROM tomcat:10.1-jdk17

WORKDIR /usr/local/tomcat
RUN rm -rf webapps/ROOT
COPY target/meuapp.war webapps/ROOT.war

EXPOSE 7858
CMD ["catalina.sh", "run"]
