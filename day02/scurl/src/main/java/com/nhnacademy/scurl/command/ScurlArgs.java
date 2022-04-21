package com.nhnacademy.scurl.command;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ScurlArgs {
    @Parameter(names = "-X")
    private String method;

    @Parameter(names = "-v")
    private boolean verbose;

    @Parameter(names = "-H")
    private String header;

    @Parameter(names = "-d")
    private String body;

    @Parameter
    private List<String> etc = new ArrayList<>();

    public ScurlArgs(String method) {
        this.method = "GET";
    }

    public String getMethod() {
        return method;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public String getHeader() {
        return header;
    }

    public List<String> getEtc() {
        return etc;
    }
}
