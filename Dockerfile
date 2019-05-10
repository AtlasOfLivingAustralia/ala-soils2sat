FROM java:7 AS grails225
# taken from https://hub.docker.com/r/mozart/grails/dockerfile
# ...and modified for Grails 2.2.5 and Java 7 as the DockerHub repo doesn't provide it
MAINTAINER Manuel Ortiz Bey <ortiz.manuel@mozartanalytics.com>

ENV GRAILS_VERSION 2.2.5

# Install Grails
WORKDIR /usr/lib/jvm
RUN wget https://github.com/grails/grails-core/releases/download/v$GRAILS_VERSION/grails-$GRAILS_VERSION.zip && \
    unzip grails-$GRAILS_VERSION.zip && \
    rm -rf grails-$GRAILS_VERSION.zip && \
    ln -s grails-$GRAILS_VERSION grails

# Setup Grails path.
ENV GRAILS_HOME /usr/lib/jvm/grails
ENV PATH $GRAILS_HOME/bin:$PATH

# Create App Directory
RUN mkdir /app

# Set Workdir
WORKDIR /app

# Set Default Behavior
ENTRYPOINT ["grails"]


# pulling deps takes foooooooorever, so we'll cache it
FROM grails225 as withDeps
# FIXME need to find file that requires maven deps
ADD ./grails-app/conf/BuildConfig.groovy grails-app/conf/
# actually just triggering the dependency resolution
RUN grails compile


FROM withDeps
ADD . /app/
# TODO it would be nice to build a WAR (maybe even self executing with Jetty)
# but we need a way to inject config into the app, at least for DB connection.
EXPOSE 8080
CMD ["run-app"]
ENTRYPOINT ["/bin/bash", "./docker/entrypoint.sh"]
