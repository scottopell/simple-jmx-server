# Simple JMX Server

This java project creates a single custom mbean and exposes a JMX service at the
specified host and port.
The RMI registry is set to use the same name.

None of the `-Dcom.sun.jmxremote` flags are needed as the configuration all
occurs in code explicitly. See `JMXServer.java`


## Building and Running
Locally with maven:
```
$ mvn clean compile assembly:single
$ java -jar target/jmx-server-1.0.0-jar-with-dependencies.jar localhost 1099
```

Docker build:
```
$ docker build -t simplejmx .
$ docker run --rm simplejmx
```

