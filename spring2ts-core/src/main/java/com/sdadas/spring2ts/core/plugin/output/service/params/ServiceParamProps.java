package com.sdadas.spring2ts.core.plugin.output.service.params;

import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Sławomir Dadas
 */
public class ServiceParamProps implements Serializable {

    private String name;

    private Boolean required;

    private String pathVar;

    private String defaultValue;

    public ServiceParamProps() {
    }

    public ServiceParamProps(Multimap<String, String> map) {
        this.name = StringUtils.stripToNull(get(map, "name"));
        if(StringUtils.isBlank(this.name)) this.name = StringUtils.stripToNull(get(map, "value"));
        this.required = getBooleanValue(get(map, "required"));
        this.pathVar = StringUtils.stripToNull(get(map, "pathVar"));
        this.defaultValue = StringUtils.stripToNull(get(map, "defaultValue"));
    }

    private String get(Multimap<String, String> map, String key) {
        Collection<String> values = map.get(key);
        if(values.isEmpty()) return null;
        return values.iterator().next();
    }

    private Boolean getBooleanValue(String value) {
        if(StringUtils.isBlank(value)) return null;
        if("true".equalsIgnoreCase(value)) {
            return true;
        } else if("false".equalsIgnoreCase(value)) {
            return false;
        } else {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public Boolean getRequired() {
        return required;
    }

    public String getPathVar() {
        return pathVar;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
