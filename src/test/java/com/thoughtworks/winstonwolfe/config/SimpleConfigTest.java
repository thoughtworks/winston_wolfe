package com.thoughtworks.winstonwolfe.config;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleConfigTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldReturnTheUnderlyingMap() {
        Map<String, Object> map = new HashMap<String, Object>();

        SimpleConfig config = new SimpleConfig(map, "basePath");
        assertThat(config.getMap(), is(map));
    }

    @Test
    public void shouldReturnStringValueByKey() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", "value");

        SimpleConfig config = new SimpleConfig(map, "basePath");
        assertThat(config.getString("key"), is("value"));
    }

    @Test
    public void shouldComplainIfKeyNotFound() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The configuration key 'key' could not be found.");

        Map<String, Object> map = new HashMap<String, Object>();

        SimpleConfig config = new SimpleConfig(map, "basePath");
        config.getString("key");
    }

    @Test
    public void shouldComplainIfValueIsNotAString() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The configuration value for 'key' is not a String.");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", 8);

        SimpleConfig config = new SimpleConfig(map, "basePath");
        config.getString("key");
    }


    @Test
    public void getListShouldReturnListValueByKey() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> list = new ArrayList();
        map.put("key", list);

        SimpleConfig config = new SimpleConfig(map, "basePath");
        assertThat(config.getList("key"), is(list));
    }

    @Test
    public void getListShouldComplainIfKeyNotFound() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The configuration key 'key' could not be found.");

        Map<String, Object> map = new HashMap<String, Object>();

        SimpleConfig config = new SimpleConfig(map, "basePath");
        config.getList("key");
    }

    @Test
    public void getListShouldComplainIfValueIsNotAList() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The configuration value for 'key' is not a List.");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", 8);

        SimpleConfig config = new SimpleConfig(map, "basePath");
        config.getList("key");
    }

    @Test
    public void getFLatStringMapShouldComplainIfConfigIsNotFlat() {

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The configuration value for 'key' is not a String.");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", new HashMap());

        SimpleConfig config = new SimpleConfig(map, "basePath");
        config.getFlatStringMap();
    }

    @Test
    public void getFLatStringMapShouldComplainIfConfigIsNotAllStrings() {

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The configuration value for 'key' is not a String.");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", 8);

        SimpleConfig config = new SimpleConfig(map, "basePath");
        config.getFlatStringMap();
    }

    @Test
    public void getFLatStringMapShouldDoWhatItClaims() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", "value");
        map.put("anotherkey", "anothervalue");

        SimpleConfig config = new SimpleConfig(map, "basePath");
        Map<String,String> flatMap = config.getFlatStringMap();
        assertThat(flatMap.get("key"), is("value"));
        assertThat(flatMap.get("anotherkey"), is("anothervalue"));
    }


    @Test
    public void getSubConfigShouldReturnSubConfigValueByKey() {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> subMap = new HashMap<String, Object>();
        map.put("key", subMap);
        subMap.put("subkey", "value");

        SimpleConfig config = new SimpleConfig(map, "basePath");
        WinstonConfig subConfig = config.getSubConfig("key");
        assertThat(subConfig.getMap(), is(subMap));
    }

    @Test
    public void getSubConfigShouldComplainIfKeyNotFound() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The configuration key 'key' could not be found.");

        Map<String, Object> map = new HashMap<String, Object>();

        SimpleConfig config = new SimpleConfig(map, "basePath");
        config.getSubConfig("key");
    }

    @Test
    public void getSubConfigShouldComplainIfValueIsNotAMap() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The configuration value for 'key' is not a Map.");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", 8);

        SimpleConfig config = new SimpleConfig(map, "basePath");
        config.getSubConfig("key");
    }

    @Test
    public void getSubConfigShouldPassTheBasePathToTheSubConfig() {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> subMap = new HashMap<String, Object>();
        map.put("key", subMap);
        subMap.put("subkey", "value");

        SimpleConfig config = new SimpleConfig(map, "base");
        WinstonConfig subConfig = config.getSubConfig("key");
        assertThat(subConfig.getFile("subkey"), is(new File("base/value")));
    }

    @Test
    public void getFileShouldGetTheLocationOfTheFileAtKeyWithoutSlash() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("file", "here.txt");

        SimpleConfig config = new SimpleConfig(map, "basePath");

        File expectedFile = new File("basePath/here.txt");

        assertThat(config.getFile("file"), is(expectedFile));
    }

    @Test
    public void getFileShouldGetTheLocationOfTheFileAtKeyWithSlash() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("file", "/here.txt");

        SimpleConfig config = new SimpleConfig(map, "basePath");

        File expectedFile = new File("basePath/here.txt");

        assertThat(config.getFile("file"), is(expectedFile));
    }

    @Test
    public void shouldReturnValueIfKeyExists() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("foo", "value");

        SimpleConfig config = new SimpleConfig(map, "basePath");

        assertThat(config.exists("foo"), is(true));
        assertThat(config.exists("herp"), is(false));
    }

    @Test
    public void shouldReturnAnInteger() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", 1);

        SimpleConfig config = new SimpleConfig(map, "basePath");
        assertThat(config.getInt("key"), is(1));
    }
}