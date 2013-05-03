package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.datasource.DataSource;
import org.w3c.dom.Document;

import javax.xml.xpath.XPathExpressionException;

public class SelectorMatchValidator implements ResponseValidator {
    private final WinstonConfig expectations;
    private final WinstonConfig selectors;

    public SelectorMatchValidator(WinstonConfig selectors, WinstonConfig expectations) {
        this.selectors = selectors;
        this.expectations = expectations;
    }

    @Override
    public ValidationResults validateAgainst(DataSource actualResponseDataSource) {
        ValidationResults results = new ValidationResults();
        for (String key : expectations.getMap().keySet()) {
            ValidationFactory validatorFactory = new ValidationFactory(actualResponseDataSource.getDocument(), key, expectations, selectors, "");

            try {
                Validation validation = validatorFactory.buildValidation();
                validation.validate();

                results.addValidationResults(validation.getResults());
            } catch (XPathExpressionException e) {
                e.printStackTrace();
                results.getFailureMessages().add(String.format("The xpath '%s' is not valid. Refer to the horrible stack trace on the console.", key));
            }
        }

        return results;
    }
}