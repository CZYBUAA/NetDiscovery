package com.cv4j.netdiscovery.core.parser;

import com.cv4j.netdiscovery.core.domain.Page;
import com.cv4j.netdiscovery.core.domain.ResultItems;
import com.cv4j.netdiscovery.core.parser.annotation.ExtractBy;
import com.safframework.tony.common.utils.Preconditions;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by tony on 2018/2/4.
 */
@Slf4j
public class AnnotationParser implements Parser {

    protected ResultItems resultItems = null;

    @Override
    public void process(Page page) {

        resultItems = page.getResultItems();

        Class clazz = this.getClass();

        Field[] fields = clazz.getDeclaredFields();

        if (Preconditions.isNotBlank(fields)) {

            Arrays.asList(fields)
                    .forEach(field -> {

                        //设置字段可见性
                        field.setAccessible(true);

                        ExtractBy.XPath xpath = field.getAnnotation(ExtractBy.XPath.class);

                        if (xpath != null) {

                            if (field.getType().isAssignableFrom(Collection.class)) {
                                resultItems.put(field.getName(), page.getHtml().xpath(xpath.value()));
                            } else {
                                resultItems.put(field.getName(), page.getHtml().xpath(xpath.value()).all());
                            }
                        }
                    });
        }
    }
}
