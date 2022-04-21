package com.nhnacademy.scurl.client;

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.beust.jcommander.JCommander;
import com.nhnacademy.scurl.command.ScurlArgs;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;

public class ScurlClient {
    static ScurlArgs scurlArgs = new ScurlArgs("GET");

    public static void main(String[] args) {
        JCommander.newBuilder().addObject(scurlArgs).build().parse(args);

        if (args.length <= 0) {
            throw new ArrayIndexOutOfBoundsException("인자를 설정해 주세요.");
        }

        if (scurlArgs.getEtc().isEmpty()) {
            throw new IllegalArgumentException("URL 이 존재하지 않습니다.");
        }

        // DONE 1: (scurl.jar) http://httpbin.org/get
        // DONE 2: (scurl.jar) -X GET http://httpbin.org/get
        // DONE 3: (scurl.jar) -v http://httpbin.org/get
        // DONE 4: (scurl.jar) -v -H "X-Custom-Header: NA" http://httpbin.org/get
        if (Objects.equals(scurlArgs.getMethod(), "GET")) {
            doRequest("GET", "http://httpbin.org/get");
        }

        // DONE 5: (scurl.jar) -v -X POST -d "{ \"hello\": "\world\" }" -H "Content-Type: application/json" http://httpbin.org/post
        if (Objects.equals(scurlArgs.getMethod(), "POST")) {
            doRequest("POST", "http://httpbin.org/post");
        }

        // TODO 6: scurl -L http://httpbin.org/status/302
        // TODO 7: scurl -F "upload=@file_path" http://httpbin.org/post
    }

    private static void doRequest(String method, String hostUrl) {
        BufferedWriter writer = null;

        try (Socket socket = new Socket()) {
            URL url = new URL(hostUrl);
            socket.connect(new InetSocketAddress(url.getHost(), 80));

            StringBuilder request = new StringBuilder();
            request
                .append(method).append(" /").append(method.toLowerCase()).append(" HTTP/1.1")
                .append(lineSeparator())
                .append("Host: ").append(url.getHost()).append(lineSeparator())
                .append("User-Agent: curl/7.79.1\"").append(lineSeparator())
                .append("Accept: */*\"").append(lineSeparator());

            String requestCopy = new String(request);

            if (Objects.equals(scurlArgs.getMethod(), "POST")) {
                request
                    .append("Content-Type: application/json").append(lineSeparator())
                    .append("Content-Length: ").append(scurlArgs.getBody().getBytes(UTF_8).length)
                    .append(lineSeparator()).append(lineSeparator())
                    .append(scurlArgs.getBody());
            }

            if (scurlArgs.getHeader() != null) {
                request.append("X-Custom-Header: NA");
            }
            request.append(lineSeparator()).append(lineSeparator());

            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(request.toString());
            writer.flush();

            byte[] bytes = new byte[2048];
            int numberOfBytes = socket.getInputStream().read(bytes);
            String response = new String(bytes, 0, numberOfBytes, UTF_8);

            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out))) {
                if (scurlArgs.isVerbose()) {
                    out.write(requestCopy);
                    if (Objects.equals(scurlArgs.getMethod(), "POST")) {
                        out.write("Content-Type: application/json" + lineSeparator());
                        out.write("Content-Length: " + scurlArgs.getBody().getBytes(UTF_8).length);
                    }
                    out.write(lineSeparator());
                }
                if (scurlArgs.getHeader() != null) {
                    out.write("X-Custom-Header: NA");
                    out.write(lineSeparator());
                }
                out.write(lineSeparator());

                if (!scurlArgs.isVerbose()) {
                    response = response.split("\r\n\r\n")[1];
                }
                out.write(response);
                out.flush();
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
