package com.sdadas.spring2ts.core.plugin.output.service.template;

import com.sdadas.spring2ts.core.plugin.output.TypeMapper;
import com.sdadas.spring2ts.core.plugin.output.service.ServiceClass;
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;

import java.io.IOException;

/**
 * @author Sławomir Dadas
 */
public interface TSServiceTemplate {

    void init(TypeMapper mapper) throws IOException;

    TSWritable serviceClass(ServiceClass clazz);
}
