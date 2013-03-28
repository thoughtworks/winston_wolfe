package com.thoughtworks.winstonwolfe.config;

import java.io.FileNotFoundException;

interface ConfigLoader {
    WinstonConfig load(String path) throws FileNotFoundException;
}