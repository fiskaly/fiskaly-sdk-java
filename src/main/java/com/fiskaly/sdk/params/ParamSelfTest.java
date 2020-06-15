package com.fiskaly.sdk.params;

public class ParamSelfTest {

    public final String context;

    public ParamSelfTest(String context) {

        if (context == null || context.isEmpty()) {
            throw new IllegalArgumentException("Missing or empty \"context\" parameter");
        }

        this.context = context;
    }

}
