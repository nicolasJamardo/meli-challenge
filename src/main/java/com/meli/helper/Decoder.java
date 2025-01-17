package com.meli.helper;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Decoder {

    /**
     * Decodes the message based on a list of strings
     *
     * @param messages - list of messages recieved
     * @return - String - Decoded message
     */
    public static String decodeMessage(List<List<String>> messages) {
        List<String> finalMessage = new ArrayList<>();
        AtomicBoolean possibleEndOfMessage = new AtomicBoolean(false);
        messages.forEach(Collections::reverse);

        int smallestSize = Collections.min(messages.stream().map(List::size).collect(Collectors.toList()));

        IntStream
                .range(0, smallestSize)
                .forEach(idx -> {
                    List<String> words = messages.stream().map(m -> m.get(idx)).collect(Collectors.toList());
                    String possibleWord = possibleWord(words);
                    if (possibleEndOfMessage.get() && !possibleWord.isEmpty()) {
                        finalMessage.add(null);
                    }
                    if (StringUtils.isEmpty(possibleWord)) {
                        possibleEndOfMessage.set(true);
                    } else {
                        finalMessage.add(possibleWord);
                    }
                });

        return finalMessage.contains(null) ? null : String.join(" ", Lists.reverse(finalMessage));
    }

    /**
     * Validates a possible word removing duplicates and checking if two words dont match
     *
     * @param words - three words from each list from each satellite
     * @return - The possible word for the decoded message
     */
    private static String possibleWord(List<String> words) {
        words.remove("");
        Set<String> set = new HashSet<>(words);
        return set.size() > 1 ? null : (set.size() == 1 ? set.iterator().next() : null);
    }
}
