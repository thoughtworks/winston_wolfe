package com.thoughtworks.winstonwolfe.validators;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

public class ValidationResultsTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldComplainWhenAssertingSuccessWhenFailuresExist() {
        List<String> failureMessages = new ArrayList<String>();
        failureMessages.add("A banana");
        failureMessages.add("A grapefruit");

        ValidationResults results = new ValidationResults(new ArrayList<String>(), failureMessages);

        try {
            results.assertSuccess();
            fail("We expected an exception");
        }catch (RuntimeException e) {
            assertThat(e.getMessage(), containsString("A banana"));
            assertThat(e.getMessage(), containsString("A grapefruit"));
        }
    }

    @Test
    public void shouldBeSilentWhenAssertingSuccessWhenNoFailuresExist() {
        ValidationResults results = new ValidationResults(new ArrayList<String>(), new ArrayList<String>());
        results.assertSuccess();
    }
}