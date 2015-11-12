package com.github.rmannibucau.sirona.javaagent.extension;

import com.sun.net.httpserver.HttpServer;
import org.apache.sirona.javaagent.AgentArgs;
import org.apache.sirona.javaagent.JavaAgentRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(JavaAgentRunner.class)
public class SironaOriginHttpHeaderAdderTest {
    private static HttpServer server;

    @BeforeClass
    public static void startHttpServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(1234), 0);
        server.start();
    }

    @AfterClass
    public static void stopServer() {
        server.stop(0);
    }

    @Test
    @AgentArgs(removeTargetClassesFromClasspath = false)
    public void addHeader() throws IOException {
        final URL url = new URL("http://localhost:" + server.getAddress().getPort());
        final URLConnection connection = url.openConnection();
        connection.setConnectTimeout(200);
        connection.setReadTimeout(100);
        try {
            connection.connect();
        } catch (final Exception e) {
            fail(e.getMessage());
        } finally {
            try {
                HttpURLConnection.class.cast(connection).disconnect();
            } catch (final Exception e) {
                // no-op
            }
        }


        assertEquals("us", connection.getRequestProperty("sirona-origin"));
    }
}
