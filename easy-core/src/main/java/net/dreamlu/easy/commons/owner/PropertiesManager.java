/*
 * Copyright (c) 2012-2015, Luigi R. Viggiano
 * All rights reserved.
 *
 * This software is distributable under the BSD license.
 * See the terms of the BSD license in the documentation provided with this software.
 */

package net.dreamlu.easy.commons.owner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import net.dreamlu.easy.commons.owner.Config.LoadPolicy;
import net.dreamlu.easy.commons.owner.Config.LoadType;
import net.dreamlu.easy.commons.owner.Config.Sources;

/**
 * Loads properties and manages access to properties handling concurrency.
 *
 * @author Luigi R. Viggiano
 */
class PropertiesManager {
    private final Class<? extends Config> clazz;
    private final Map<?, ?>[] imports;
    private final Properties properties;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReadLock readLock = lock.readLock();
    private final WriteLock writeLock = lock.writeLock();

    private final LoadType loadType;
    private final List<URI> uris;

    private final LoadersManager loaders;

    PropertiesManager(Class<? extends Config> clazz, Properties properties,
                      VariablesExpander expander, LoadersManager loaders, Map<?, ?>... imports) {
        this.clazz = clazz;
        this.properties = properties;
        this.loaders = loaders;
        this.imports = imports;
        ConfigURIFactory urlFactory = new ConfigURIFactory(clazz.getClassLoader(), expander);
        uris = toURIs(clazz.getAnnotation(Sources.class), urlFactory);

        LoadPolicy loadPolicy = clazz.getAnnotation(LoadPolicy.class);
        loadType = (loadPolicy != null) ? loadPolicy.value() : Config.LoadType.FIRST;
    }

    private List<URI> toURIs(Sources sources, ConfigURIFactory uriFactory) {
        String[] specs = specs(sources, uriFactory);
        List<URI> result = new ArrayList<URI>();
        for (String spec : specs) {
            try {
                URI uri = uriFactory.newURI(spec);
                if (uri != null)
                    result.add(uri);
            } catch (URISyntaxException e) {
                throw Util.unsupported(e, "Can't convert '%s' to a valid URI", spec);
            }
        }
        return result;
    }

    private String[] specs(Sources sources, ConfigURIFactory uriFactory) {
        if (sources != null) return sources.value();
        return defaultSpecs(uriFactory);
    }

    private String[] defaultSpecs(ConfigURIFactory uriFactory) {
        String prefix = uriFactory.toClasspathURLSpec(clazz.getName());
        return loaders.defaultSpecs(prefix);
    }

    Properties load() {
        writeLock.lock();
        try {
            return load(properties);
        } finally {
            writeLock.unlock();
        }
    }

    private Properties load(Properties props) {
        PropertiesMapper.defaults(props, clazz);
        Properties loadedFromFile = doLoad();
        merge(props, loadedFromFile);
        merge(props, Util.reverse(imports));
        return props;
    }

    Properties doLoad() {
        return loadType.load(uris, loaders);
    }

    private static void merge(Properties results, Map<?, ?>... inputs) {
        for (Map<?, ?> input : inputs)
            results.putAll(input);
    }

    public String getProperty(String key) {
        readLock.lock();
        try {
            return properties.getProperty(key);
        } finally {
            readLock.unlock();
        }
    }

}
