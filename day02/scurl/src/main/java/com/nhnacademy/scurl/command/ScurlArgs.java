package com.nhnacademy.scurl.command;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ScurlArgs {
    @Parameter(names = "scurl")
    private boolean scurl;

    @Parameter(names = {"-X", "GET"})
    private String method;

    @Parameter(names = "-v")
    private boolean verbose;

    @Parameter(names = {"-H", "X-Custom-Header: NA"})
    private String header;

    @Parameter
    private List<String> etc = new ArrayList<>();

    public boolean isScurl() {
        return scurl;
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
