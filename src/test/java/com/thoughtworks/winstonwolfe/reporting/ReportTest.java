package com.thoughtworks.winstonwolfe.reporting;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class ReportTest {
    @Test
    public void shouldRenderEmptyDocumentWhenNothingToReport() {
        HtmlReport report = new HtmlReport();

        assertThat(report.render(), is("<html><body></body></html>"));
    }

    @Test
    public void shouldRenderTheRequestDocument() {
        HtmlReport report = new HtmlReport();
        report.setRequest("<blah />");

        assertThat(report.render(), containsString("<div id=\"request\"><textarea><blah /></textarea></div>"));
    }
}