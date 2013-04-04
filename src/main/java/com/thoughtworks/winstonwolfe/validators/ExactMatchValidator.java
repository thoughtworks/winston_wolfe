package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExactMatchValidator implements ResponseValidator {
    private final DataSource expected;

    public ExactMatchValidator(DataSource expected) {
        this.expected = expected;
    }

    public ValidationResults validateAgainst(DataSource actual) throws Exception {
        String actualResponseData = actual.getData();
        String expectedResponseData = expected.getData();

        List<String> successMessages = new ArrayList<String>();
        List<String> failureMessages = new ArrayList<String>();

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(actualResponseData, expectedResponseData);
        if (!diff.similar()) {
            StringBuffer stringBuffer = new StringBuffer();
            diff.appendMessage(stringBuffer);

            failureMessages.add(String.format("The expected response did not match the actual response.%s", stringBuffer.toString()));
        } else {
            successMessages.add("The response met expectations");
        }

        return new ValidationResults(successMessages, failureMessages);
    }
}