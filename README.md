# Winston Wolfe
*A best-of-breed, harm-minimisation testing tool in Java.*

## Harm-Minimisation
A harm-minimisation tool is one you use when you've already caused a lot of damage by not writing unit tests when you built the system. So you're now trying to minimise the damage of immature development practices. This is coupled with the shifting of responsibility from the development team and the testing team.

While these tools are often well made; they are not one that I would ever start off with in mind. This isn't an end-state tool. I only use such a tool here because of the situation I am in.

It's not all peaches and cream though as a common theme for these tools is that they ask the least appropriate people (testers) to put automated tests in the least appropriate place (external system interfaces).

**Winston Wolfe doesn't fix practices. Winston Wolfe fixes problems.**


## What does it do?
It loads, adjusts and then throws documents at HTTP or JMS endpoints and then runs XPath validation on the response.

### Alternatives
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

### An example with modularisation
All selectors can be stored in external files and shared between projects. Any file reference is relative to the location of that file, not the original script nor the execution path of Winston Wolfe.

    request_selectors:
      name: /xml/person/name
      import_files:
        - /path/to/imported_file.yaml
        - /path/to/another_file.yaml
    response_selectors:
      import_files:
        - /path/to/age_selectors.yaml

age\_selectors.yaml:

    age: /xml/person/age
    age_accuracy: /xml/person/age_accuracy

And files can be nested.

imported\_file.yaml:

    inline: //selector
    import_files:
      - /path/relative/to/here/more_selectors.yaml

# Environment Configuration
Each test script indicates the name of the request recipient. The environment configuration lets the script know where that recipient is.

JMS and HTTP are currently supported. The configuration file needs a bit of organising as it's a dumping ground for config at the moment.

    a_http_endpoint:
      http_url: http://localhost:8080
    a_jms_endpoint:
      context_factory: com.tibco.tibjms.naming.TibjmsInitialContextFactory
      provider_url: tcp://localhost:7778
      connection_factory_name: FTGatewayQueueConnectionFactory
      jndi_username: ""
      jndi_password: ""
      jms_username: ""
      jms_password: ""
      request_queue: REQUEST_QUEUE
      request_queue_type: static
      response_queue: RESPONSE_QUEUE_ON_SAME_BOX
      response_queue_type: dynamic
      encoding: UTF-8
      timeout: 5000
      additional_properties:
        SOAPJMS_targetService: "DoSomething"
        SOAPJMS_bindingVersion: "1.0"
        SOAPJMS_contentType: text/xml; charset="UTF-8"
        SOAPJMS_soapAction: "DoSomething"
        SOAPJMS_isFault: "false"
        SOAPJMS_requestIRI: "sds"
        SOAPJMS_requestURI: ""
        SOAPJMS_soapMEP: "http://www.w3.org/2003/05/soap/mep/request-response/"
        SOAPJMS_contentEncoding: UTF-8

Some notes:

- Empty quotes are used for blank values.
- Both static and dynamic request and response queues are supported.
- Additional properties are any string property that can be set on a JMS Message. None are prescribed and any text will be added to the message as a string property.
- Time is in milliseconds.
- Encoding is specified in three places. Two are optional and parameterised configuration will resolve this in the future.

# The Future
These are ideas, in no order, with no promises:

- WSDL/XSD generation for request documents (probably a standalone tool)
- Natural language input (today, a number, etc)
- Natural language validation (exists, occurs twice)
- Externalised 'apply changes' and 'verify response' values for sharing
- Sharing values between request and response
- Chaining Tests into a larger scenario
- File as target, write changes applied to request to a file
- DB as source, source values for changes and verification from a database
- File as source, source values from a file
- Command line arguments as source of values
- Sibling selector validation - verify an element has value when siblings have specified values
- Parameterised selectors - select element where aspect is driven from input value
- Support for auto-stub configuration
- Modularised environment configuration
- Better YAML data type support
- XML namespace support for nested namespaces
- XML namespace support (special case: redefining a namespace)
- Better command line error handling
- JUnit report output
- Pre-test validation of script to limit wasted test runs
- Better handling of tabs and case sensitivity in YAML files
- Response queue on separate infrastructure
