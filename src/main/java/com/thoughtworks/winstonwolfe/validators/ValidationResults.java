package com.thoughtworks.winstonwolfe.validators;

import java.util.Arrays;
import java.util.List;

public class ValidationResults {
    private final List<String> successMessages;
    private final List<String> failureMessages;

    public ValidationResults(List<String> successMessages, List<String> failureMessages) {
        this.successMessages = successMessages;
        this.failureMessages = failureMessages;
    }

    public List<String> getSuccessMessages() {
        return successMessages;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public void assertSuccess() {
        if (!failureMessages.isEmpty()) {
            throw new RuntimeException(Arrays.toString(failureMessages.toArray()));
        }
    }
}