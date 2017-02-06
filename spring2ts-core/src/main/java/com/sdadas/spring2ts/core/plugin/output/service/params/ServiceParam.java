package com.sdadas.spring2ts.core.plugin.output.service.params;

import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.source.ParameterSource;

/**
 * // TODO: Zastanowić się, czy możliwe jest obsłużenie propertiesów systemowych ${...} w path
 *
 * @author Sławomir Dadas
 */
public class ServiceParam {

    private final ParameterSource<?> param;

    private final ServiceParamType paramType;

    private final ServiceParamProps props;

    public ServiceParam(ParameterSource<?> param) {
        this.param = param;
        this.paramType = ServiceParamType.resolve(param);
        this.props = paramType.createProps(param);
    }

    public String getName() {
        return param.getName();
    }

    public ServiceParamType getParamType() {
        return paramType;
    }

    public Type<?> getInputType() {
        return param.getType();
    }

    public boolean isIgnored() {
        return paramType.equals(ServiceParamType.IGNORED);
    }

    public boolean isPayload() {
        return paramType.isPayload();
    }

    public ServiceParamProps getProps() {
        return props;
    }
}
