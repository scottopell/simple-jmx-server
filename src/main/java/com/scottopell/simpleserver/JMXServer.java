package com.scottopell.simpleserver;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;

public class JMXServer {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java JMXServer <hostname> <port>");
            return;
        }

        String hostname = args[0];
        int intPort = Integer.parseInt(args[1]);
        String port = args[1];

        // Initialize RMI registry at same port as the jmx service
        LocateRegistry.createRegistry(intPort);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        String domain = "Bohnanza";
        ObjectName mbeanName = new ObjectName(domain + ":name=MyMBean");
        MyIntMBean mbean = new MyInt();
        mbs.registerMBean(mbean, mbeanName);

        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + hostname + ":" + port + "/jmxrmi");
        JMXConnectorServer connector = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);

        long startTime = System.nanoTime();
        System.out.println("Starting JMXConnectorServer");
        connector.start();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        String humanReadableDuration = String.format(
                "%d min, %d sec, %d ms, %d ns",
                TimeUnit.NANOSECONDS.toMinutes(duration),
                TimeUnit.NANOSECONDS.toSeconds(duration) % 60,
                TimeUnit.NANOSECONDS.toMillis(duration) % 1000,
                duration % 1000000
                );
        System.out.println("Starting the JMXConnectorServer took " + humanReadableDuration);
    }
}
