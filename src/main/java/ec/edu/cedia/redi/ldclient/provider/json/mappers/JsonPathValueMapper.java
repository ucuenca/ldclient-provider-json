/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.cedia.redi.ldclient.provider.json.mappers;

import com.jayway.jsonpath.Predicate;
import org.apache.marmotta.ldclient.api.provider.ValueMapper;

/**
 *
 * @author Xavier Sumba <xavier.sumba93@ucuenca.ec>
 */
public abstract class JsonPathValueMapper implements ValueMapper {

    private final String path;

    protected JsonPathValueMapper(String path, Predicate... filters) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
