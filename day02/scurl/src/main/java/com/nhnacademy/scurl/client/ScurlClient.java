package com.nhnacademy.scurl.client;

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
    static ScurlArgs scurlArgs = new ScurlArgs();

    public static void main(String[] args) {
        JCommander.newBuilder().addObject(scurlArgs).build().parse(args);

        if (args.length <= 0) {
            throw new ArrayIndexOutOfBoundsException("인자를 설정해 주세요.");
        }

        if (!scurlArgs.isScurl()) {
            throw new IllegalArgumentException("scurl 명령어로 시작해야 합니다.");
        }

        // TODO 1: scurl http://httpbin.org/get
        // TODO 2: scurl -X GET http://httpbin.org/get
        // TODO 3: scurl -v http://httpbin.org/get
        if (scurlArgs.getEtc().contains("http://httpbin.org/get") ||
            Objects.equals(scurlArgs.getMethod(), "GET")) {

            doRequest("GET", "http://httpbin.org/get");
        }

        // TODO 4: scurl -v -H "X-Custom-Header: NA" http://httpbin.org/get
        if (/* Objects.equals(args[1], "-v") && */ Objects.equals(args[2], "-H")) {
            // doGet(args[1], "GET", "httpbin.org", args[3]);
        }

        // TODO 5: scurl -v -X POST -d "{ \"hello\": "\world\" }" -H "Content-Type: application/json" http://httpbin.org/post
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
                .append(method).append(" /get HTTP/1.1").append(System.lineSeparator())
                .append("Host: ").append(url.getHost()).append(System.lineSeparator());

            if (scurlArgs.getHeader() != null) {
                request.append("X-Custom-Header: NA");
            }
            request.append(System.lineSeparator()).append(System.lineSeparator());

            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(request.toString());
            writer.flush();

            byte[] bytes = new byte[2048];
            int numberOfBytes = socket.getInputStream().read(bytes);
            String response = new String(bytes, 0, numberOfBytes, UTF_8);

            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out))) {
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

    private static void verbose(String response) {
        System.out.println(response);
    }
}
