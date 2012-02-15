/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.arquillian.extension.byteman.test.impl.common;

import org.junit.Assert;

import org.jboss.arquillian.extension.byteman.api.BMRule;
import org.jboss.arquillian.extension.byteman.api.BMRules;
import org.jboss.arquillian.extension.byteman.impl.common.GenerateScriptUtil;
import org.junit.Test;

/**
 * @author Davide D'Alto
 *
 */
public class GenerateScriptUtilTest {

    @Test
    public void testBMRuleAnnotation() throws Exception {
        BMRule annotation = HelloClass.class.getAnnotation(BMRule.class);
        String generatedScript = GenerateScriptUtil.constructScriptText(annotation);
        String expected = "\nRULE Simple rule\nCLASS SimpleClass\nMETHOD println\nHELPER helper\nAT INVOKE ALL\nIF TRUE\nDO System.out.println(\"Hello\")\nENDRULE\n";
        Assert.assertEquals(expected, generatedScript);
    }

    @BMRule(name = "Simple rule", targetClass = "SimpleClass", helper = "helper", targetLocation = "AT INVOKE ALL", targetMethod = "println", action = "System.out.println('Hello')")
    private static class HelloClass {
    }

    @Test
    public void testBMRulesAnnotation() throws Exception {
        BMRules annotation = ThrowClass.class.getAnnotation(BMRules.class);
        String generatedScript = GenerateScriptUtil.constructScriptText(annotation.value());
        String expected = "\nRULE Throw exception on success class\nCLASS StatelessManagerBean\nMETHOD forcedClassLevelFailure\nIF TRUE\nDO throw new java.lang.RuntimeException()\nENDRULE\n";
        Assert.assertEquals(expected, generatedScript);
    }

    @BMRules(@BMRule(name = "Throw exception on success class", targetClass = "StatelessManagerBean", targetMethod = "forcedClassLevelFailure", action = "throw new java.lang.RuntimeException()"))
    private static class ThrowClass {
    }

    @Test
    public void testScriptParsingBMRulesAnnotation() throws Exception {
        BMRules annotation = ScriptParsingClass.class.getAnnotation(BMRules.class);
        String generatedScript = GenerateScriptUtil.constructScriptText(annotation.value());
        String expectedEnabled = "\nRULE Enabled\nCLASS EnabledClass\nMETHOD enableIt\nIF TRUE\nDO System.out.println(\"Hello D'Alto\")\nENDRULE\n";
        String expectedDisabled = "\nRULE Disabled\nCLASS DisabledClass\nMETHOD disableIt\nIF TRUE\nDO System.out.println('Hello D''Alto')\nENDRULE\n";
        Assert.assertEquals(expectedEnabled + expectedDisabled, generatedScript);
    }

    @BMRules({
        @BMRule(name = "Enabled", targetClass = "EnabledClass", targetMethod = "enableIt", action = "System.out.println('Hello D''Alto')", stringParsingEnabled= true),
        @BMRule(name = "Disabled", targetClass = "DisabledClass", targetMethod = "disableIt", action = "System.out.println('Hello D''Alto')", stringParsingEnabled = false)
    })
    private static class ScriptParsingClass {
    }

}
