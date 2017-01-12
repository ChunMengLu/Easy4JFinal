/*
 * Copyright (c) 2012-2015, Luigi R. Viggiano
 * All rights reserved.
 *
 * This software is distributable under the BSD license.
 * See the terms of the BSD license in the documentation provided with this software.
 */

package net.dreamlu.easy.commons.owner;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static net.dreamlu.easy.commons.owner.Config.LoadType.FIRST;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.net.URI;
import java.util.List;
import java.util.Properties;
/**
 * owner配置文件读取：http://owner.aeonbits.org/docs/importing-properties/
 *
 * @author Luigi R. Viggiano
 * @see java.util.Properties
 */
public interface Config extends Serializable {
    /**
     * Specifies the policy for loading the properties files. By default the first available properties file specified
     * by {@link Sources} will be loaded, see {@link LoadType#FIRST}. User can also specify that the load policy is
     * {@link LoadType#MERGE} to have the properties files merged: properties are loaded in order from the first file to
     * the last, if there are conflicts in properties names the earlier files loaded prevail.
     *
     * @since 1.0.2
     */
    @Retention(RUNTIME)
    @Target(TYPE)
    @Documented
    @interface LoadPolicy {
        LoadType value() default FIRST;
    }

    /**
     * Specifies the source from which to load the properties file. It has to be specified in a URI string format.
     * By default, allowed protocols are the ones allowed by {@link java.net.URL} plus
     * <tt>classpath:path/to/resource.properties</tt>, but user can specify his own additional protocols.
     *
     * @since 1.0.2
     */
    @Retention(RUNTIME)
    @Target(TYPE)
    @Documented
    @interface Sources {
        String[] value();
    }

    /**
     * Default value to be used if no property is found. No quoting (other than normal Java string quoting) is done.
     */
    @Retention(RUNTIME)
    @Target(METHOD)
    @Documented
    @interface DefaultValue {
        String value();
    }

    /**
     * The key used for lookup for the property.  If not present, the key will be generated based on the unqualified
     * method name.
     */
    @Retention(RUNTIME)
    @Target(METHOD)
    @Documented
    @interface Key {
        String value();
    }

    /**
     * Specifies the policy type to use to load the {@link org.aeonbits.owner.Config.Sources} files for properties.
     *
     * @since 1.0.2
     */
    enum LoadType {

        /**
         * The first available of the specified sources will be loaded.
         */
        FIRST {
            @Override
            Properties load(List<URI> uris, LoadersManager loaders) {
                Properties result = new Properties();
                for (URI uri : uris)
                    try {
                        loaders.load(result, uri);
                        break;
                    } catch (IOException ex) {
                        // happens when a file specified in the sources is not found or cannot be read.
                        Util.ignore();
                    }
                return result;
            }
        },

        /**
         * All the specified sources will be loaded and merged. If the same property key is
         * specified from more than one source, the one specified first will prevail.
         */
        MERGE {
            @Override
            Properties load(List<URI> uris, LoadersManager loaders) {
                Properties result = new Properties();
                for (URI uri : Util.reverse(uris))
                    try {
                        loaders.load(result, uri);
                    } catch (IOException ex) {
                        // happens when a file specified in the sources is not found or cannot be read.
                        Util.ignore();
                    }
                return result;
            }
        };

        abstract Properties load(List<URI> uris, LoadersManager loaders);
    }

    /**
     * Specifies simple <tt>{@link String}</tt> as separator to tokenize properties values specified as a
     * single string value, into elements for vectors and collections.
     * The value specified is used as per {@link String#split(String, int)} with int=-1, every element is also
     * trimmed out from spaces using {@link String#trim()}.
     *
     * Notice that {@link TokenizerClass} and {@link Separator} do conflict with each-other when they are both specified
     * together on the same level:
     * <ul>
     *     <li>
     *     You cannot specify {@link TokenizerClass} and {@link Separator} both together on the same method
     *     </li>
     *     <li>
     *     You cannot specify {@link TokenizerClass} and {@link Separator} both together on the same class
     *     </li>
     * </ul>
     * in the two above cases an {@link UnsupportedOperationException} will be thrown when the corresponding conversion
     * is executed.
     *
     * @since 1.0.4
     */
    @Retention(RUNTIME)
    @Target({METHOD, TYPE})
    @Documented
    @interface Separator {
        /**
         * @return the value specified is used as per {@link java.lang.String#split(String, int)} with int=-1
         */
        String value();
    }

    /**
     * Specifies a <tt>{@link Tokenizer}</tt> class to allow the user to define a custom logic to split
     * the property value into tokens to be used as single elements for vectors and collections.
     *
     * Notice that {@link TokenizerClass} and {@link Separator} do conflict with each-other when they are both specified
     * together on the same level:
     * <ul>
     *     <li>
     *     You cannot specify {@link TokenizerClass} and {@link Separator} both together on the same method
     *     </li>
     *     <li>
     *     You cannot specify {@link TokenizerClass} and {@link Separator} both together on the same class
     *     </li>
     * </ul>
     * in the two above cases an {@link UnsupportedOperationException} will be thrown when the corresponding conversion
     * is executed.
     *
     * @since 1.0.4
     */
    @Retention(RUNTIME)
    @Target({METHOD, TYPE})
    @Documented
    @interface TokenizerClass {
        Class<? extends Tokenizer> value();
    }

    /**
     * Specifies a <tt>{@link Converter}</tt> class to allow the user to define a custom conversion logic for the
     * type returned by the method. If the method returns a collection, the Converter is used to convert a single
     * element.
     */
    @Retention(RUNTIME)
    @Target(METHOD)
    @Documented
    @SuppressWarnings("rawtypes")
    @interface ConverterClass {
        Class<? extends Converter> value();
    }

}
