package com.thoughtworks.winstonwolfe.selectors;

import com.thoughtworks.winstonwolfe.endpoint.HttpServiceEndPoint;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.swing.text.html.HTMLDocument;
import javax.xml.xpath.XPathExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

public class SelectorFactoryTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldComplainIfYouAskItToCreateInvalidSelectors() {
        Map<String, String> rawXpathMap = new HashMap<String, String>();
        rawXpathMap.put("selector", "my\\dod$@gy/xpath");
        rawXpathMap.put("second", "my\\dod$@gy/banana/stand");


        SelectorFactory factory = new SelectorFactory();

        try {
            factory.buildSelectors(rawXpathMap);
            fail("should have thrown an exception");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), containsString("The selector 'selector' has invalid xpath 'my\\dod$@gy/xpath'"));
            assertThat(e.getMessage(), containsString("The selector 'second' has invalid xpath 'my\\dod$@gy/banana/stand'"));
        }
    }

    @Test
    public void shouldReturnEmptyMapIfGivenAnEmptyMap() {
        SelectorFactory factory = new SelectorFactory();
        Map<String, XPathExpression> results = factory.buildSelectors(new HashMap<String, String>());
        assertThat(results.size(), is(0));
    }

    @Test
    public void shouldReturnAMapOfKeysAndSelectors() {
        Map<String, String> rawXpathMap = new HashMap<String, String>();
        rawXpathMap.put("selector", "//foo/bar");
        rawXpathMap.put("second", "//bar/foo");


        SelectorFactory factory = new SelectorFactory();

        Map<String, XPathExpression> results = factory.buildSelectors(rawXpathMap);

        assertThat(results.size(), is(2));
        assertThat(results.get("selector"), is(instanceOf(XPathExpression.class)));
        assertThat(results.get("second"), is(instanceOf(XPathExpression.class)));
    }
}