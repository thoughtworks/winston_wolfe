package com.thoughtworks.winstonwolfe.reporting;

import com.thoughtworks.winstonwolfe.validators.ValidationResults;

public class HtmlReport {
    private String request = "";
    private String response = "";
    private ValidationResults results;

    public String render() {
        return String.format("<html><head>%s</head><body>%s%s%s</body></html>", renderStyle() ,renderResults(), renderRequest(), renderResponse() );
    }

    private String renderStyle() {
      return "<style>#satisfactions {color: green} #disappointments {color: red} #request, #response {float:left; width: 40%;} textarea {width: 100%; height: 500;}</style>";
    }
    private String renderResults() {
        if (results == null) {
            return "";
        }

        return renderSatisfactionMessages() + renderDisappointmentMessages();
    }

    private String renderDisappointmentMessages() {
        String resultsAsHtml = "<div id=\"disappointments\"><ul>";

        for (String satisfaction : results.getFailureMessages()) {
            resultsAsHtml += String.format("<li>%s</li>", satisfaction);
        }

        resultsAsHtml += "</ul></div>";
        return resultsAsHtml;
    }

    private String renderSatisfactionMessages() {
        String resultsAsHtml = "<div id=\"satisfactions\"><ul>";

        for (String satisfaction : results.getSuccessMessages()) {
            resultsAsHtml += String.format("<li>%s</li>", satisfaction);
        }

        resultsAsHtml += "</ul></div>";
        return resultsAsHtml;
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