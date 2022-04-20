package com.nhnacademy.scurl;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Objects;

/**
 * 1. scurl http://httpbin.org/get
 * 2. scurl -X GET http://httpbin.org/get
 */
public class ScurlClient {
    public static void main(String[] args) {
        if (args.length <= 0) {
            throw new ArrayIndexOutOfBoundsException("인자를 설정해 주세요.");
        }

        if (!Objects.equals(args[0], "scurl")) {
            throw new IllegalArgumentException("scurl 명령어로 시작해야 합니다.");
        }

        if (args[1].length() > 2) {
            doGet("-X", "GET", "httpbin.org");
        }

        if (Objects.equals(args[1], "-X")) {
            doGet(args[1], args[2], "httpbin.org");
        }

        // reader
        //     .lines()
        //     .filter(line -> line.startsWith("{"))
        //     .forEach(System.out::println);

        // String line;
        // while ((line = reader.readLine()) != null) {
        //     if (line.startsWith("{")) {
        //         isResponseBody = true;
        //     }
        //     if (ScurlClient.isResponseBody) {
        //         System.out.println(line);
        //     }
        // }
    }

    private static void doGet(String option, String method, String hostname) {
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

            System.out.println(response.split("\r\n\r\n")[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
