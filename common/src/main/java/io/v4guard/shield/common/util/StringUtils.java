package io.v4guard.shield.common.util;

import java.util.List;
import java.util.StringJoiner;

public class StringUtils {

    public static String buildMultilineString(List<String> lines) {
        StringJoiner message = new StringJoiner("\n");

        for (String line : lines) {
            message.add(line);
        }

        return message.toString();
    }
}
