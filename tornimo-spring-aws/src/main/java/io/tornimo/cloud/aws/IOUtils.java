package io.tornimo.cloud.aws;

import java.io.*;
import java.nio.charset.Charset;

final class IOUtils {

    private static final int EOF = -1;

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    public static String toString(InputStream inputStream, Charset charset) {
        if (inputStream == null) {
            return "";
        }

        try (StringWriter writer = new StringWriter();
             InputStreamReader reader = new InputStreamReader(inputStream, charset);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            char[] chars = new char[DEFAULT_BUFFER_SIZE];
            int readChars;
            while ((readChars = bufferedReader.read(chars)) != EOF) {
                writer.write(chars, 0, readChars);
            }
            return writer.toString();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static String toString(InputStream inputStream) {
        return toString(inputStream, Charset.defaultCharset());
    }

    private IOUtils() {
    }
}