/*
 * Copyright (c) 2012-2015, Luigi R. Viggiano
 * All rights reserved.
 *
 * This software is distributable under the BSD license.
 * See the terms of the BSD license in the documentation provided with this software.
 */

package net.dreamlu.easy.commons.owner;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.dreamlu.easy.commons.owner.loaders.Loader;
import net.dreamlu.easy.commons.owner.loaders.PropertiesLoader;
import net.dreamlu.easy.commons.owner.loaders.SystemLoader;


/**
 * This class is responsible of locating an appropriate Loader for a given URL (based the extension in the resource
 * name)and load the properties from it.
 *
 * @author Luigi R. Viggiano
 * @since 1.0.5
 */
class LoadersManager implements Serializable {
    private static final long serialVersionUID = -226703093706920069L;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<Loader> loaders = new LinkedList<Loader>();

    LoadersManager() {
        registerLoader(new PropertiesLoader());
        registerLoader(new SystemLoader());
    }

    void load(Properties result, URI uri) throws IOException {
        Loader loader = findLoader(uri);
        loader.load(result, uri);
    }

    Loader findLoader(URI uri) {
        lock.readLock().lock();
        try {
            for (Loader loader : loaders)
                if (loader.accept(uri))
                    return loader;
            throw Util.unsupported("Can't resolve a Loader for the URL %s.", uri.toString());
        } finally {
            lock.readLock().unlock();
        }
    }

    final void registerLoader(Loader loader) {
        if (loader == null)
            throw new IllegalArgumentException("loader can't be null");
        lock.writeLock().lock();
        try {
            loaders.add(0, loader);
        } finally {
            lock.writeLock().unlock();
        }
    }

    void clear() {
        lock.writeLock().lock();
        try{
            loaders.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    String[] defaultSpecs(String prefix) {
        lock.readLock().lock();
        try {
            List<String> defaultSpecs = new ArrayList<String>(loaders.size());
            for (Loader loader : loaders) {
                String spec = loader.defaultSpecFor(prefix);
                if (spec != null)
                    defaultSpecs.add(spec);
            }
            return defaultSpecs.toArray(new String[0]);
        } finally {
            lock.readLock().unlock();
        }
    }

}
