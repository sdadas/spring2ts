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
package com.sdadas.spring2ts.core.plugin.output.service.params;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import com.sdadas.spring2ts.core.utils.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author Sławomir Dadas
 */
public enum ServiceParamType {

    REQUEST_PARAM(RequestParam.class),

    REQUEST_HEADER(RequestHeader.class),

    PATH_VARIABLE(PathVariable.class),

    MATRIX_VARIABLE(MatrixVariable.class),

    REQUEST_BODY(RequestBody.class),

    REQUEST_PART(RequestPart.class),

    MODEL_ATTRIBUTE(ModelAttribute.class),

    HTTP_METHOD(HttpMethod.class),

    HTTP_ENTITY(HttpEntity.class),

    IGNORED(null) {
        @Override
        protected boolean matches(ParameterSource<?> param) {
            String name = param.getType().getQualifiedName();
            return IGNORED_TYPES.contains(name);
        }
    };

    private final static Set<String> IGNORED_TYPES = createIgnoredTypes();

    private final Class<?> type;

    ServiceParamType(Class<?> type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public ServiceParamProps createProps(ParameterSource<?> param) {
        if(Annotation.class.isAssignableFrom(type)) {
            Class<? extends Annotation> annotation = (Class<? extends Annotation>) this.type;
            Multimap<String, String> map = AnnotationUtils.getAnnotationAsMap(param, annotation);
            return new ServiceParamProps(map);
        }
        return new ServiceParamProps();
    }

    public static ServiceParamType resolve(ParameterSource<?> param) {
        for (ServiceParamType type : values()) {
            if(type.matches(param)) return type;
        }
        return MODEL_ATTRIBUTE;
    }

    @SuppressWarnings("unchecked")
    protected boolean matches(ParameterSource<?> param) {
        if(Annotation.class.isAssignableFrom(type)) {
            return param.getAnnotation((Class<? extends Annotation>) type) != null;
        } else {
            return param.getType().isType(type);
        }
    }

    public boolean isPayload() {
        return EnumSet.of(REQUEST_BODY, REQUEST_PART, HTTP_ENTITY).contains(this);
    }

    private static Set<String> createIgnoredTypes() {
        ImmutableSet.Builder<String> set = ImmutableSet.builder();
        set.add("org.springframework.web.context.request.WebRequest");
        set.add("org.springframework.web.context.request.NativeWebRequest");
        set.add("java.util.Locale");
        set.add("java.util.TimeZone", "java.time.ZoneId");
        set.add("java.io.InputStream", "java.io.Reader");
        set.add("java.io.OutputStream", "java.io.Writer");
        set.add("java.security.Principal");
        set.add("java.util.Map", "org.springframework.ui.Model", "org.springframework.ui.ModelMap");
        set.add("org.springframework.web.servlet.mvc.support.RedirectAttributes");
        set.add("org.springframework.validation.Errors", "org.springframework.validation.BindingResult");
        set.add("org.springframework.web.bind.support.SessionStatus");
        set.add("org.springframework.web.util.UriComponentsBuilder");
        return set.build();
    }
}
