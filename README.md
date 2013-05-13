# Winston Wolfe
*A best-of-breed, harm-minimisation testing tool in Java.*

## Harm-Minimisation
A harm-minimisation tool is one you use when you've already caused a lot of damage by not writing unit tests when you built the system. So you're now trying to minimise the damage of immature development practices. This is coupled with the shifting of responsibility from the development team and the testing team.

While these tools are often well made; they are not one that I would ever start off with in mind. This isn't an end-state tool. I only use such a tool here because of the situation I am in.

It's not all peaches and cream though as a common theme for these tools is that they ask the least appropriate people (testers) to put the only automated tests in the least appropriate place (external system interfaces).

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

- In order to *increase* **the systems we can test**, we will *support* **HTTPS endpoints for both requests and responses**
- In order to *increase* **the number of systems we can verify**, we will *support* **JDBC as a dataset provider**
- In order to *increase* **the number of systems we can verify**, we will *support* **Splunk**
- In order to *reduce* **the effort required in creating input xml**, we will *generate* **xml from XSDs**
- In order to *reduce* **the effort required in creating input xml**, we will *generate* **xml from the WSDL**
- In order to *reduce* **manual effort required in stub configuration**, we will *support* **a model for auto stub configuration**
- In order to *reduce* **time wasted running invalid tests**, we will *validate* **tests scripts before executing the test**
- In order to *improve* **usability writing test validation**, we will *support* **natural language terms like 'exists' and 'occurs twice' for validating results**
- In order to *improve* **data input usability**, we will *support* **natural language input terms like 'today' and 'a number'**
- In order to *increase* **the modularisation of test scripts**, we will *read* **input values from a file for use in test scripts**
- In order to *increase* **the modularisation of test scripts**, we will *read* **input values from a db dataset for use in test scripts**
- In order to *increase* **the modularisation of test scripts**, we will *read* **input values from the command line and use them in test scripts**
- In order to *increase* **the reusability of test setup**, we will *allow* **requests to be sent to the file system instead of a service**
- In order to *increase* **the range of test that can be run**, we will *support* **chaining of steps in to a single script**
- In order to *reduce* **test script complexity**, we will *support* **sharing of input values between request and response**
- In order to *reduce* **a test's dependency on specific data**, we will *allow* **parameterisation of selectors**
- In order to *reduce* **duplication of effort across tests**, we will *allow* **values to be externalised in modules**
- In order to *reduce* **configuration effort**, we will *allow* **modularised environment configuration**
- In order to *improve* **feedback about misconfiguration**, we will *provide* **command line error handling**
- In order to *increase* **the range of systems we can support**, we will *support* **XML namespace redefinition**
- In order to *increase* **the range of systems we can support**, we will *support* **nested XML namespaces**
- In order to *better* **handle YAML's implicit typing**, we will *convert* **all values to a string**
- In order to *better* **integrate with existing tools**, we will *provide* **reports in jUnit format**
- In order to *improve* **reporting of case-separate duplicate keys**, we will *preprocess* **files to remove case**
- In order to *improve* **error reporting**, we will *replace* **YAMLs handling of tab characters**
- In order to *increase* **the number of supported systems**, we will *support* **responses from multiple queues**
- In order to *increase* **the number of supported systems**, we will *support* **responses from queues on separate infrastructure**


- error loop for recursive loops in YAML loading
- API for consumers like Concordion.
- Highlight in the XML response the passes and failures (if possible)
- node is absent
- setup dependency managemetn
- Provide a simple standalone HTTP and JMS queue to easily allow trying out the tool (include yaml tests and endpoint configs)