/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.dm.annotation.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotates a class that acts as a Factory Configuration Adapter Service. For each new <code>Config Admin</code> 
 * factory configuration matching the specified factoryPid, an instance of this service will be created.
 * The adapter will be registered with the specified interface, and with the specified adapter service properties.
 * Depending on the <code>propagate</code> parameter, every public factory configuration properties 
 * (which don't start with ".") will be propagated along with the adapter service properties. <p>
 * 
 * <h3>Usage Examples</h3>
 * Here, a "Dictionary" service instance is created for each existing "sample.DictionaryConfiguration" factory pids.
 * 
 * First, we declare our factory configuration metadata using standard bndtools metatatype annotations 
 * (see http://www.aqute.biz/Bnd/MetaType):
 * 
 * <blockquote>
 * <pre>
 * package sample;
 * import java.util.List;
 * import aQute.bnd.annotation.metatype.Meta.AD;
 * import aQute.bnd.annotation.metatype.Meta.OCD;
 *
 * &#64;OCD(factory = true, description = "Declare here some Dictionary instances.")
 * public interface DictionaryConfiguration {
 *   &#64;AD(description = "Describes the dictionary language.", deflt = "en")
 *   String lang();
 *
 *   &#64;AD(description = "Declare here the list of words supported by this dictionary.")
 *   List<String> words();
 * }
 * </pre>
 * </blockquote>
 *
 * And here is the Dictionary service:
 *
 * <blockquote>
 * <pre>
 * import java.util.List;
 * import aQute.bnd.annotation.metatype.Configurable;
 *
 * &#64;FactoryConfigurationAdapterService(factoryPidClass=DictionaryConfiguration.class)  
 * public class DictionaryImpl implements DictionaryService {
 *     protected void updated(Dictionary&#60;String, ?&#62; props) {
 *         // load configuration from the provided dictionary, or throw an exception of any configuration error.
 *         DictionaryConfiguration cnf = Configurable.createConfigurable(DictionaryConfiguration.class, props);
 * 
 *         m_lang = config.lang();
 *         m_words.clear();
 *         for (String word : conf.words()) {
 *             m_words.add(word);
 *         }
 *     }
 *     ...
 * }
 * </pre>
 * </blockquote>
 * 
 * @author <a href="mailto:dev@felix.apache.org">Felix Project Team</a>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface FactoryConfigurationAdapterService
{
    /**
     * The interface(s) to use when registering adapters. By default, directly implemented 
     * interfaces will be registered in the OSGi registry.
     */
    Class<?>[] provides() default {};

    /**
     * Adapter Service properties. Notice that public factory configuration is also registered in service properties,
     * (only if propagate is true). Public factory configuration properties are those which don't starts with a dot (".").
     */
    Property[] properties() default {};

    /**
     * Returns the factory pid whose configurations will instantiate the annotated service class. (By default, the pid is the 
     * service class name).
     */
    String factoryPid() default "";
    
    /**
     * Returns the factory pid from a class name. The full class name will be used as the configuration PID.
     * You can use this method when you use an interface annoted with standard bndtols metatype annotations.
     * (see http://www.aqute.biz/Bnd/MetaType).
     */
    Class<?> factoryPidClass() default Object.class;

    /**
     * The Update method to invoke (defaulting to "updated"), when a factory configuration is created or updated
     */
    String updated() default "updated";

    /**
     * Returns true if the configuration properties must be published along with the service. 
     * Any additional service properties specified directly are merged with these.
     * @return true if configuration must be published along with the service, false if not.
     */
    boolean propagate() default false;

    /**
     * The label used to display the tab name (or section) where the properties are displayed. Example: "Printer Service".
     * @return The label used to display the tab name where the properties are displayed.
     * @deprecated use standard bndtools metatype annotations instead (see http://www.aqute.biz/Bnd/MetaType)
     */
    String heading() default "";

    /**
     * A human readable description of the PID this annotation is associated with. Example: "Configuration for the PrinterService bundle".
     * @return A human readable description of the PID this annotation is associated with.
     * @deprecated use standard bndtools metatype annotations instead (see http://www.aqute.biz/Bnd/MetaType)
     */
    String description() default "";

    /**
     * The list of properties types used to expose properties in web console. 
     * @return The list of properties types used to expose properties in web console.
     * @deprecated use standard bndtools metatype annotations instead (see http://www.aqute.biz/Bnd/MetaType)
     */
    PropertyMetaData[] metadata() default {};
    
    /**
     * Sets the static method used to create the adapter instance.
     */
    String factoryMethod() default "";
}
