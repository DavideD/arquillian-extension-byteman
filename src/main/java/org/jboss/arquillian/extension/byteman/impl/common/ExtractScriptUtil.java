/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.extension.byteman.impl.common;

import java.lang.reflect.Method;

import org.jboss.arquillian.extension.byteman.api.BMRule;
import org.jboss.arquillian.extension.byteman.api.BMRules;
import org.jboss.arquillian.test.spi.event.suite.ClassLifecycleEvent;
import org.jboss.arquillian.test.spi.event.suite.TestLifecycleEvent;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;

/**
 * ExtractScriptUtil
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public final class ExtractScriptUtil
{
    public static String extract(ClassLifecycleEvent event)
    {
        String fromAnnotations = rulesFromAnnotations(event);
        String fromMethods = rulesFromMethods(event.getTestClass().getMethods(BMRule.class));
        String allRules = fromMethods + fromAnnotations;

        if (allRules.isEmpty())
            return null;

        return allRules;
    }

    private static String rulesFromAnnotations(ClassLifecycleEvent event)
    {
        BMRule rule = event.getTestClass().getAnnotation(BMRule.class);
        BMRules rules = event.getTestClass().getAnnotation(BMRules.class);
        String createRules = createRules(rule, rules);
        if (createRules == null)
            return "";

        return createRules;
    }

    private static String rulesFromMethods(Method... methods)
    {
        StringBuilder ruleBuilder = new StringBuilder();
        if (methods.length == 0)
            return ruleBuilder.toString();

        for (Method method : methods)
        {
           try
           {
               Class<?> returnType = method.getReturnType();
               if (Descriptor.class.isAssignableFrom(returnType))
               {
                   ruleBuilder.append("\n");
                   Descriptor descriptor = (Descriptor) method.invoke(null);
                   ruleBuilder.append(descriptor.exportAsString());
               }
           }
           catch (Exception e)
           {
               e.getMessage();
           }
        }
        if (ruleBuilder.length() == 0)
            return ruleBuilder.toString();

        return ruleBuilder.substring(1);
    }

    public static String extract(TestLifecycleEvent event)
    {
        BMRule rule = event.getTestMethod().getAnnotation(BMRule.class);
        BMRules rules = event.getTestMethod().getAnnotation(BMRules.class);

        return createRules(rule, rules);
    }

    private static String createRules(BMRule rule, BMRules rules)
    {
        if(rule != null || rules != null)
        {
           return GenerateScriptUtil.constructScriptText(toRuleArray(rule, rules));
        }
        return null;
    }

    private static BMRule[] toRuleArray(BMRule rule, BMRules rules)
    {
       if(rule != null)
       {
          return new BMRule[] {rule};
       }
       return rules.value();
    }
}
