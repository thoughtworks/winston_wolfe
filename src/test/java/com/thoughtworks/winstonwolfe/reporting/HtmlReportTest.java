package com.thoughtworks.winstonwolfe.reporting;

import com.thoughtworks.winstonwolfe.validators.ValidationResults;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HtmlReportTest {
    @Test
    public void shouldRenderEmptyDocumentWhenNothingToReport() {
        HtmlReport report = new HtmlReport();

        assertThat(report.render(), containsString("<body></body>"));
    }

    @Test
    public void shouldRenderTheRequestDocument() {
        HtmlReport report = new HtmlReport();
        report.setRequest("<blah />");

        assertThat(report.render(), containsString("<div id=\"request\"><h3>Sent Request</h3><textarea disabled=\"true\"><blah /></textarea></div>"));
    }

    @Test
    public void shouldRenderTheResponseDocument() {
        HtmlReport report = new HtmlReport();
        report.setResponse("<mahResponse />");

        assertThat(report.render(), containsString("<div id=\"response\"><h3>Received Response</h3><textarea disabled=\"true\"><mahResponse /></textarea></div>"));
    }

    @Test
    public void shouldRenderPassedExpectationsAsSatisfactions() {
        HtmlReport report = new HtmlReport();
        ValidationResults results = mock(ValidationResults.class);
        List<String> listOfStrings = new ArrayList<String>();
        listOfStrings.add("Testing is awesome");
        listOfStrings.add("Pass Pass Pass Pass");
        when(results.getSuccessMessages()).thenReturn(listOfStrings);
        report.addResults(results);

        String result = "";
        result += "<div id=\"satisfactions\"><ul>";
        result += "<li>Testing is awesome</li>";
        result += "<li>Pass Pass Pass Pass</li>";
        result += "</ul></div>";

        assertThat(report.render(), containsString(result));
    }

    @Test
    public void shouldRenderFailedExpectationsAsDisappointments() {
        HtmlReport report = new HtmlReport();
        ValidationResults results = mock(ValidationResults.class);
        List<String> listOfStrings = new ArrayList<String>();
        listOfStrings.add("Testing has been going to be awesome");
        listOfStrings.add("Fail Fail Fail Fail");
        when(results.getFailureMessages()).thenReturn(listOfStrings);
        report.addResults(results);

        String result = "";
        result += "<div id=\"disappointments\"><ul>";
        result += "<li>Testing has been going to be awesome</li>";
        result += "<li>Fail Fail Fail Fail</li>";
        result += "</ul></div>";

        assertThat(report.render(), containsString(result));
    }
}