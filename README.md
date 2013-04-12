# Winston Wolfe
*A best-of-breed, harm-minimisation testing tool in Java.*

## Harm-Minimisation
A harm-minimisation tool is one you use when you've already caused a lot of damage by not writing unit tests when you built the system. So you're now trying to minimise the damage of immature development practices. This is coupled with the shifting of responsibility from the development team and the testing team.

While these tools are often well made; they are not one that I would ever start off with in mind. This isn't an end-state tool. I only use such a tool here because of the situation I am in.

It's not all peaches and cream though as a common theme for these tools is that they ask the least appropriate people (testers) to put automated tests in the least appropriate place (external system interfaces).

**Winston Wolfe doesn't fix practices. Winston Wolfe fixes problems.**


## What does it do?
It loads, adjusts and then throws documents at HTTP or JMS endpoints and then runs XPath validation on the response.

## Alternatives
There are other products already on the market that fill this niche:

- SoapUI
- iTKO Lisa
- Parasoft SOAtest
- HP ServiceTest

So why not use them? Most of the issues we have with these tools is that they hinder collaboration and modularisation. Often collaboration results in the merging of XML documents and modularisation involves creating lots of XML documents to merge.

**Goal**: *Winston Wolfe is designed to support modularisation and collaboration through easier to merge files.*

## How does it work?
### A simple example
The simplest script looks a little like this:

    read: path/to/input.xml
    send_to: endpoint_key
    compare_response_to: path/to/expected_output.xml

The endpoint is defined in a separate file, grouped by environment:

    endpoint_key:
      http_url: http://myserver.com

The tool is executed in the following fashion:

    winston_wolfe path/to/environment_config.yaml path/to/test_script.yaml > report.html

At present the report is dumped to the console, so sending it into a file will allow you to view it in a browser.

All of the scripts are in YAML.

### A more complicated example:
Let's show our input xml:

    <xml>
        <person>
            <name>Ryan</name>
        </person>
    </xml>

And our test script:

    read: path/to/input.xml
    apply_changes:
      name: Perryn
    send_to: age_resolution_service
    verify_response:
      age: 37
      age_accuracy: Not Confirmed
    
    request_selectors:
      name: /xml/person/name
    response_selectors:
      age: /xml/person/age
      age_accuracy: /xml/person/age_accuracy

This configuration allows us to overwrite values on the way in and to use XPath to verify aspects of the response.
