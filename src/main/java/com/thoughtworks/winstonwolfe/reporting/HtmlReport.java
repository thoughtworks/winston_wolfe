package com.thoughtworks.winstonwolfe.reporting;

public class HtmlReport {
    private String request = "";

    public String render() {
        return String.format("<html><body>%s</body></html>", renderRequest());
    }

    private String renderRequest() {
        if (request.isEmpty()) {
            return "";
        }

        return String.format("<div id=\"request\"><textarea>%s</textarea></div>", request);
    }

    public void setRequest(String request) {
        this.request = request;
    }
}