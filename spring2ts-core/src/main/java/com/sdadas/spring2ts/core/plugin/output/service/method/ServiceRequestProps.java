/*
 * To oprogramowanie jest własnością
 *
 * OPI - Ośrodek Przetwarzania Informacji,
 * Al. Niepodległości 188B, 00-608 Warszawa
 * Numer KRS: 0000127372
 * Sąd Rejonowy dla m. st. Warszawy w Warszawie XII Wydział
 * Gospodarczy KRS
 * REGON: 006746090
 * NIP: 525-000-91-40
 * Wszystkie prawa zastrzeżone. To oprogramowanie może być używane tylko
 * zgodnie z przeznaczeniem. OPI nie odpowiada za ewentualne wadliwe
 * działanie kodu.
 */
package com.sdadas.spring2ts.core.plugin.output.service.method;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO: obsługa parametrów headers i params.
 *
 * @author Sławomir Dadas
 */
public class ServiceRequestProps {

    private List<ServicePath> paths = Collections.emptyList();

    private Set<RequestMethod> methods = EnumSet.allOf(RequestMethod.class);

    private Set<String> consumes = Collections.emptySet();

    private Set<String> produces = Collections.emptySet();

    private boolean responseBody = false;

    public ServiceRequestProps(ServiceRequestProps other) {
        this.paths = other.paths;
        this.methods = other.methods;
        this.consumes = other.consumes;
        this.produces = other.produces;
        this.responseBody = other.responseBody;
    }

    public ServiceRequestProps(Multimap<String, String> map) {
        if(map == null) return;
        paths = createPaths(map);
        methods = createMethods(map);
        if(methods.isEmpty()) methods = EnumSet.allOf(RequestMethod.class);
        consumes = Sets.newLinkedHashSet(get(map, "consumes"));
        produces = Sets.newLinkedHashSet(get(map, "produces"));
    }

    private List<ServicePath> createPaths(Multimap<String, String> map) {
        Collection<String> values = get(map, "path");
        if(values.isEmpty()) values = get(map, "value");
        return values.stream().map(ServicePath::new).collect(Collectors.toList());
    }

    private EnumSet<RequestMethod> createMethods(Multimap<String, String> map) {
        EnumSet<RequestMethod> result = EnumSet.noneOf(RequestMethod.class);
        Collection<String> values = get(map, "method");
        for (String value : values) {
            result.add(RequestMethod.valueOf(enumValue(value)));
        }
        return result;
    }

    private String enumValue(String input) {
        if(StringUtils.contains(input, '.')) {
            return StringUtils.substringAfterLast(input, ".");
        }
        return input;
    }

    public ServiceRequestProps merged(ServiceRequestProps parent) {
        ServiceRequestProps result = new ServiceRequestProps(this);
        result.paths = mergePaths(result.paths, parent.paths);
        result.methods = Sets.intersection(result.methods, parent.methods);
        if(result.consumes.isEmpty()) result.consumes = parent.consumes;
        if(result.produces.isEmpty()) result.produces = parent.produces;
        if(!result.responseBody) result.responseBody = parent.responseBody;
        return result;
    }

    private List<ServicePath> mergePaths(List<ServicePath> methodPaths, List<ServicePath> parentPaths) {
        if(methodPaths.isEmpty()) {
            return parentPaths;
        }

        List<ServicePath> results = Lists.newArrayList();
        for (ServicePath methodPath : methodPaths) {
            String childPath = methodPath.getPath();
            if(!StringUtils.startsWith(childPath, "/")) childPath = "/" + childPath;
            for (ServicePath parentPath : parentPaths) {
                results.add(new ServicePath(parentPath.getPath() + childPath));
            }
        }
        return results;
    }

    private Collection<String> get(Multimap<String, String> map, String key) {
        return map.get(key);
    }

    public List<ServicePath> getPaths() {
        return paths;
    }

    public Set<RequestMethod> getMethods() {
        return methods;
    }

    public void setMethods(Set<RequestMethod> methods) {
        this.methods = methods;
    }

    public void setPaths(List<ServicePath> paths) {
        this.paths = paths;
    }

    public void setConsumes(Set<String> consumes) {
        this.consumes = consumes;
    }

    public void setProduces(Set<String> produces) {
        this.produces = produces;
    }

    public Set<String> getConsumes() {
        return consumes;
    }

    public Set<String> getProduces() {
        return produces;
    }

    public boolean isResponseBody() {
        return responseBody;
    }

    public void setResponseBody(boolean responseBody) {
        this.responseBody = responseBody;
    }

    public boolean supportsAllHttpMethods() {
        return methods.size() == RequestMethod.values().length;
    }

    public boolean supportsManyHttpMethods() {
        return methods.size() > 1;
    }

    public boolean supportsNoHttpMethods() {
        return methods.size() == 0;
    }

    public boolean supportsManyPaths() {
        return paths.size() > 1;
    }

    public boolean supportsManyConsumes() {
        return consumes.size() > 1;
    }
}
