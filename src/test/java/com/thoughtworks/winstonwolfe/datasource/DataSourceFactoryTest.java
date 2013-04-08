package com.thoughtworks.winstonwolfe.datasource;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.endpoint.HttpServiceEndPoint;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataSourceFactoryTest {
    @Test
    public void shouldBuildFileDataSourceFactoryWhenNoApplyChangesConfigured() {
        WinstonConfig config = mock(WinstonConfig.class);
        when(config.exists("apply_changes")).thenReturn(false);

        DataSourceFactory dataSourceFactory = new DataSourceFactory(config);
        DataSource dataSource = dataSourceFactory.buildDataSource();
        assertThat(dataSource, is(instanceOf(FileDataSource.class)));
    }

    @Test
    public void shouldBuildApplyChangesDataSourceWhenConfigured() {
        WinstonConfig subConfig = mock(WinstonConfig.class);
        when(subConfig.getFlatStringMap()).thenReturn(new HashMap<String, String>());

        WinstonConfig config = mock(WinstonConfig.class);
        when(config.exists("apply_changes")).thenReturn(true);
        when(config.getSubConfig("apply_changes")).thenReturn(subConfig);
        when(config.getSubConfig("request_selectors")).thenReturn(subConfig);



        DataSourceFactory dataSourceFactory = new DataSourceFactory(config);
        DataSource dataSource = dataSourceFactory.buildDataSource();
        assertThat(dataSource, is(instanceOf(ApplyChangesDataSource.class)));
    }
}