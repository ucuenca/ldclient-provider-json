/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.cedia.redi.ldclient.provider.json.mappers;

import com.jayway.jsonpath.Predicate;
import java.util.Collections;
import java.util.List;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 *
 * @author Xavier Sumba <xavier.sumba93@ucuenca.ec>
 */
public class JsonPathLiteralMapper extends JsonPathValueMapper {

    protected String datatype;

    public JsonPathLiteralMapper(String path, String datatype, Predicate... filters) {
        super(path, filters);
        this.datatype = datatype;
    }

    public JsonPathLiteralMapper(String path, Predicate... filters) {
        super(path, filters);
    }

    @Override
    public List<Value> map(String resourceUri, String selectedValue, ValueFactory factory) {
        Value value;
        if (datatype != null) {
            value = factory.createLiteral(selectedValue.trim(), factory.createURI(XMLSchema.NAMESPACE, datatype));
        } else {
            value = factory.createLiteral(selectedValue.trim());
        }
        return Collections.singletonList(value);
    }

}
