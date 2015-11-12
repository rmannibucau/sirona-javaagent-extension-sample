package com.github.rmannibucau.sirona.javaagent.extension;

import org.apache.sirona.javaagent.AgentContext;
import org.apache.sirona.javaagent.spi.InvocationListener;

import java.net.HttpURLConnection;

public class SironaOriginHttpHeaderAdder implements InvocationListener {
    @Override
    public boolean accept(final String key, final byte[] rawClassBuffer) {
        return key.equals("sun.net.www.protocol.http.HttpURLConnection.connect()");
    }

    @Override
    public void before(final AgentContext context) {
        HttpURLConnection.class.cast(context.getReference())
            .setRequestProperty("sirona-origin", "us");
    }

    @Override
    public void after(final AgentContext context, final Object result, final Throwable error) {
        // no-op
    }
}
