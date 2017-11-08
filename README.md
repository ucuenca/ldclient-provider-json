# LDClient JSON Module

This module supports [LDClient](http://marmotta.apache.org/ldclient/index.html) module to process JSON data. The JSON module is part of the LDClient Core. It offers support for retrieving JSON resources (of any type) using the [Jayway Json Path](https://github.com/json-path/JsonPath) library.

Implementing a data provider that uses JSON to retrieve resource data requires subclassing the class `AbstractJSONDataProvider`. This class implements the `retrieveResource(...)` method of `DataProvider`, but requires subclasses to implement the following classes:

- `Map<String, JsonPathValueMapper> getMappings(String resource, String requestUrl)`: a mapping table, mapping from RDF properties to JSON Path Value Mappers. Each entry in the map is evaluated in turn; in case the JSON Path expression yields a result, the property is added to the processed resource. _See the [Json Path Jayway](https://github.com/json-path/JsonPath) notation_
- `List<String> getTypes(URI resource)`: should return a list of URIs that are added as RDF types (using `rdf:type`) to each retrieved resource
- `List<String> buildRequestUrl(String resourceUri, Endpoint endpoint)`: build the URLs used for accessing the actual resource data through a web service; in the most simple case, this can simply be the same as the resource URI, but in most real-world scenarios you will need to do some sort of rewriting.

## Getting started

For Maven users, you can add the dependency in your POM file in the following way:

```xml

    <dependency>
        <groupId>ec.edu.cedia.redi</groupId>
        <artifactId>ldclient-provider-json</artifactId>
        <version>1.0.0</version>
    </dependency>

    <repositories>
        <repository>
            <id>ldclient-provider-json</id>
            <name>LDClient Provider: JSON Resource Access</name>
            <url>https://raw.github.com/ucuenca/ldclient-provider-json/repository/</url>
        </repository>
    </repositories>
```
