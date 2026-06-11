package source.config.api;

import java.net.InetAddress;

public class HostNameIpAddressResolver {

    private HostNameIpAddressResolver() {
        // Private constructor to prevent instantiation
    }

    public static String resolveHostName(String hostName) throws Exception {
        return InetAddress.getLocalHost().getHostName();
    }

    public static String resolveIpAddress(String hostName) throws Exception {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
