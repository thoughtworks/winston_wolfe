package com.thoughtworks.winstonwolfe.reporting;

import com.thoughtworks.winstonwolfe.validators.ValidationResults;
import org.apache.commons.lang3.StringEscapeUtils;

public class HtmlReport {
    private String request = "";
    private String response = "";
    private ValidationResults results;

    public String render() {
        return String.format("<html><head>%s</head><body>%s%s%s</body></html>", renderStyle() ,renderResults(), renderRequest(), renderResponse() );
    }

    private String renderStyle() {
      return "<style>.success {color: green} .failure {color: red} #request, #response {float:left; width: 40%;} textarea {width: 100%; height: 500;}</style>";
    }
    private String renderResults() {
        if (results == null) {
            return "";
        }

        return renderSatisfactionMessages() + renderDisappointmentMessages();
    }

    private String renderDisappointmentMessages() {
        if (results.getFailureMessages().isEmpty()) {
            return "<div id=\"disappointments\"></div>";
        }

        String resultsAsHtml = "<div id=\"disappointments\"><h3>Failure Messages</h3><ul>";

        for (String disappointment : results.getFailureMessages()) {
            resultsAsHtml += formatFailureMessage(disappointment);
        }

        resultsAsHtml += "</ul></div>";
        return resultsAsHtml;
    }

    private String renderSatisfactionMessages() {
        if (results.getSuccessMessages().isEmpty()) {
            return "<div id=\"satisfactions\"></div>";
        }

        String resultsAsHtml = "<div id=\"satisfactions\"><h3>Success Messages</h3><ul>";

        for (String satisfaction : results.getSuccessMessages()) {
            resultsAsHtml += formatSuccessMessage(satisfaction);
        }

        resultsAsHtml += "</ul></div>";
        return resultsAsHtml;
    }

    private String formatSuccessMessage(final String message) {
        return String.format("<li class=\"success\">%s</li>", StringEscapeUtils.escapeHtml4(message));
    }

    private String formatFailureMessage(final String message) {
        return String.format("<li class=\"failure\">%s</li>", StringEscapeUtils.escapeHtml4(message));
    }

    private String renderRequest() {
        if (request.isEmpty()) {
            return "";
        }

        return String.format("<div id=\"request\"><h3>Sent Request</h3><textarea disabled=\"true\">%s</textarea></div>", request);
    }

    private String renderResponse() {
        if (response.isEmpty()) {
            return "";
        }

        return String.format("<div id=\"response\"><h3>Received Response</h3><textarea disabled=\"true\">%s</textarea></div>", response);
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void addResults(ValidationResults results) {
        this.results = results;
    }
}