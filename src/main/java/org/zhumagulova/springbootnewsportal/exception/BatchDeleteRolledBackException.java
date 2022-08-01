package org.zhumagulova.springbootnewsportal.exception;


import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class BatchDeleteRolledBackException extends Exception {

    public BatchDeleteRolledBackException(long[] notFoundNews) {
        super ("Delete transaction rolled back. The following ids not found: " +
                LongStream.of(notFoundNews)
                .mapToObj(Long::toString)
                .collect(Collectors.joining(", ")));
    }
}
