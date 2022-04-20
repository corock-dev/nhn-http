package com.nhnacademy.scurl.client;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Objects;

public class ScurlClient {
    public static void main(String[] args) {
        if (args.length <= 0) {
            throw new ArrayIndexOutOfBoundsException("인자를 설정해 주세요.");
        }

        if (!Objects.equals(args[0], "scurl")) {
            throw new IllegalArgumentException("scurl 명령어로 시작해야 합니다.");
        }

        // TODO 1: scurl http://httpbin.org/get
        if (args[1].length() > 2) {
            doGet("-X", "GET", "httpbin.org");
        }

        // TODO 2: scurl -X GET http://httpbin.org/get
        if (args[1].equals("-X")) {
            doGet(args[1], args[2], "httpbin.org");
        }

        // TODO 3: scurl -v http://httpbin.org/get
        if (Objects.equals(args[1], "-v")) {
            doGet(args[1], args[2], "httpbin.org");
        }

        // TODO 4: scurl -v -H "X-Custom-Header: NA" http://httpbin.org/get
        if (Objects.equals(args[1], "-v") && Objects.equals(args[2], "-H")) {
            doGet(args[1], "GET", "httpbin.org", args[3]);
        }
    }

    private static void doGet(String option, String method, String hostname, String customHeader) {
        try (Socket socket = new Socket()) {
            SocketAddress socketAddress = new InetSocketAddress(hostname, 80);
            socket.connect(socketAddress);

            StringBuilder request = new StringBuilder();
            request
                .append(method).append(" /get HTTP/1.1").append(System.lineSeparator())
                .append("Host: ").append(hostname).append(System.lineSeparator())
                .append(System.lineSeparator());

            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.print(request);
            writer.flush();

            byte[] buffer = new byte[2048];
            int readByteCount = socket.getInputStream().read(buffer);
            String response = new String(buffer, 0, readByteCount, UTF_8);

            if (Objects.equals(option, "-v")) {
                verbose(response);
                return;
            }
            System.out.println(response.split("\r\n\r\n")[1]);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void doGet(String option, String method, String hostname) {
        try (Socket socket = new Socket()) {
            SocketAddress socketAddress = new InetSocketAddress(hostname, 80);
            socket.connect(socketAddress);

            String request = method + " /get HTTP/1.1" + System.lineSeparator() +
                "Host: " + hostname + System.lineSeparator() +
                System.lineSeparator();

            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.print(request);
            writer.flush();

            byte[] buffer = new byte[2048];
            int readByteCount = socket.getInputStream().read(buffer);
            String response = new String(buffer, 0, readByteCount, UTF_8);

            if (Objects.equals(option, "-v")) {
                verbose(response);
                return;
            }

            String[] responses = response.split("\r\n\r\n");
            String responseHeader = responses[0];
            String responseBody = responses[1];

            System.out.println(responseBody);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void verbose(String response) {
        System.out.println(response);
    }
}
