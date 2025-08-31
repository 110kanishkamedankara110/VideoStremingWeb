FROM openjdk:8-jdk

ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV GLASSFISH_HOME=/usr/local/glassfish4
ENV PATH=$PATH:$JAVA_HOME/bin:$GLASSFISH_HOME/bin

RUN apt-get update && apt-get install -y --no-install-recommends \
    tzdata curl unzip zip inotify-tools && \
    ln -fs /usr/share/zoneinfo/Etc/UTC /etc/localtime && \
    dpkg-reconfigure -f noninteractive tzdata && \
    rm -rf /var/lib/apt/lists/*

# Install GlassFish
RUN curl -L -o /tmp/glassfish-4.1.zip http://download.java.net/glassfish/4.1/release/glassfish-4.1.zip && \
    unzip /tmp/glassfish-4.1.zip -d /usr/local && \
    rm -f /tmp/glassfish-4.1.zip

# Expose necessary ports
EXPOSE 8080 4848 8181

# Set admin password and enable secure admin
RUN echo "AS_ADMIN_PASSWORD=" > /tmp/password.txt && \
    echo "AS_ADMIN_NEWPASSWORD=admin123" >> /tmp/password.txt && \
    $GLASSFISH_HOME/bin/asadmin start-domain && \
    $GLASSFISH_HOME/bin/asadmin --user admin --passwordfile /tmp/password.txt change-admin-password && \
    echo "AS_ADMIN_PASSWORD=admin123" > /tmp/password.txt && \
    $GLASSFISH_HOME/bin/asadmin --user admin --passwordfile /tmp/password.txt enable-secure-admin && \
    $GLASSFISH_HOME/bin/asadmin stop-domain && \
    rm /tmp/password.txt

# Copy MySQL connector and WAR file
COPY mysql-connector-j-*.jar $GLASSFISH_HOME/glassfish/domains/domain1/lib/
COPY target/VideoStreamingWeb.war $GLASSFISH_HOME/glassfish/domains/domain1/autodeploy/

# Create the videoStream folder
RUN mkdir /videoStream

# Start GlassFish domain
CMD ["asadmin", "start-domain", "--verbose"]
