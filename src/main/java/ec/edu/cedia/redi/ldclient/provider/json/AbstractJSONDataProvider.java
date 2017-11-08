/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ec.edu.cedia.redi.ldclient.provider.json;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ReadContext;
import ec.edu.cedia.redi.ldclient.provider.json.mappers.JsonPathValueMapper;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.marmotta.ldclient.api.provider.DataProvider;
import org.apache.marmotta.ldclient.exception.DataRetrievalException;
import org.apache.marmotta.ldclient.services.provider.AbstractHttpProvider;
import org.openrdf.model.Model;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.RDF;

/**
 *
 * @author Xavier Sumba <xavier.sumba93@ucuenca.ec>
 */
public abstract class AbstractJSONDataProvider extends AbstractHttpProvider implements DataProvider {

    protected static final String CHARSET = "UTF-8";

    protected abstract List<String> getTypes(URI resource);

    protected abstract Map<String, JsonPathValueMapper> getMappings(String resource, String requestUrl);

    protected Configuration getConfiguration() {
        return Configuration.defaultConfiguration()
                .addOptions(Option.ALWAYS_RETURN_LIST)
                .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
                .addOptions(Option.SUPPRESS_EXCEPTIONS);
    }

    @Override
    protected List<String> parseResponse(String resource, String requestUrl, Model triples, InputStream in, String contentType) throws DataRetrievalException {
        ReadContext ctx = JsonPath.parse(in, getConfiguration());
        ValueFactory vf = ValueFactoryImpl.getInstance();

        URI subject = vf.createURI(resource);
        for (Map.Entry<String, JsonPathValueMapper> mapping : getMappings(resource, requestUrl).entrySet()) {
            URI predicate = vf.createURI(mapping.getKey());

            List a = ctx.read(mapping.getValue().getPath());
            for (Object o : a) {
                String value;
                if (o == null) {
                    continue;
                } else {
                    value = String.valueOf(o);
                }

                List<Value> objects = mapping.getValue().map(resource, value, vf);
                for (Value object : objects) {
                    triples.add(subject, predicate, object);
                }
            }
        }

        URI ptype = vf.createURI(RDF.NAMESPACE + "type");
        for (String typeUri : getTypes(subject)) {
            Resource type_resource = vf.createURI(typeUri);
            triples.add(vf.createStatement(subject, ptype, type_resource));
        }
        return Collections.emptyList();
    }

}
