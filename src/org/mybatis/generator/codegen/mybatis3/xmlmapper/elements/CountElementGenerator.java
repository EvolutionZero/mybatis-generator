/*
 *  Copyright 2009 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class CountElementGenerator extends
        AbstractXmlElementGenerator {

    public CountElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", introspectedTable.getCount())); //$NON-NLS-1$

        FullyQualifiedJavaType parameterType = introspectedTable.getRules()
                .calculateAllFieldsClass();

        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterType.getFullyQualifiedName()));

        answer.addAttribute(new Attribute("resultType", "java.lang.Integer"));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("select "); //$NON-NLS-1$

        sb.append("count(1) ");
        answer.addElement(new TextElement(sb.toString()));

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append("<include refid=\"table\" />");
        answer.addElement(new TextElement(sb.toString()));

        XmlElement whereElement = new XmlElement("where"); //$NON-NLS-1$

//        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            XmlElement ifElement = new XmlElement("if"); //$NON-NLS-1$
            ifElement.addAttribute(new Attribute("test", introspectedColumn.getJavaProperty() + " != null"));
            ifElement.addElement(new TextElement("and " + introspectedColumn.getActualColumnName() + " = " + MyBatis3FormattingUtilities.getParameterClause(introspectedColumn)));

            whereElement.addElement(ifElement);
        }
        answer.addElement(whereElement);


//        if (context.getPlugins().sqlMapInsertSelectiveElementGenerated(
//                answer, introspectedTable)) {
            parentElement.addElement(answer);
//        }
    }
}
