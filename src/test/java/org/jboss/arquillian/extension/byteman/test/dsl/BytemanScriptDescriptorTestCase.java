/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
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
package org.jboss.arquillian.extension.byteman.test.dsl;

import org.jboss.arquillian.extension.byteman.api.dsl.BytemanScriptDescriptor;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test that the script generated using the {@link BytemanScriptDescriptor} interface is syntactically correct.
 *
 * @author Davide D'Alto
 */
public class BytemanScriptDescriptorTestCase
{
   @Test
   public void testRule() throws Exception
   {
      String expected = "Rule 4 test";
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule(expected).exportAsString();

      Assert.assertEquals("\nRULE " + expected + "\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testScript() throws Exception
   {
      String expected = "Rule 4 test";
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule(expected).rule(expected).exportAsString();

      Assert.assertEquals("\nRULE " + expected + "\nIF TRUE\nENDRULE\n\nRULE " + expected + "\nIF TRUE\nENDRULE\n", actual);
   }
   @Test(expected = IllegalArgumentException.class)
   public void testRulNulleValidation() throws Exception
   {
      Descriptors.create(BytemanScriptDescriptor.class).rule(null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testRuleEmptyValidation() throws Exception
   {
      Descriptors.create(BytemanScriptDescriptor.class).rule("");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testRuleWhiteSpaceValidation() throws Exception
   {
      Descriptors.create(BytemanScriptDescriptor.class).rule("    ");
   }

   @Test
   public void testClass() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule class").classMatch("ClassName")
            .exportAsString();

      Assert.assertEquals("\nRULE Rule class\nCLASS ClassName\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testMethod() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule method").classMatch("ClassName")
            .methodMatch("toString()").exportAsString();

      Assert.assertEquals("\nRULE Rule method\nCLASS ClassName\nMETHOD toString()\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testInterface() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule interface").methodMatch("toString()")
            .interfaceMatch("InterfaceName").exportAsString();

      Assert.assertEquals("\nRULE Rule interface\nINTERFACE InterfaceName\nMETHOD toString()\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtEntry() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule at entry").methodMatch("toString()")
            .interfaceMatch("InterfaceName").atEntry().exportAsString();

      Assert.assertEquals("\nRULE Rule at entry\nINTERFACE InterfaceName\nMETHOD toString()\nAT ENTRY\nIF TRUE\nENDRULE\n",
            actual);
   }

   @Test
   public void testAtExit() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule at exit").atExit().exportAsString();

      Assert.assertEquals("\nRULE Rule at exit\nAT EXIT\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtLine() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule at line").atLine(50).exportAsString();

      Assert.assertEquals("\nRULE Rule at line\nAT LINE 50\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testIf() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule for if").ifCondition("condition")
            .exportAsString();

      Assert.assertEquals("\nRULE Rule for if\nIF condition\nENDRULE\n", actual);
   }

   @Test
   public void testIfNot() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule for if not").ifNotCondition("condition")
            .exportAsString();

      Assert.assertEquals("\nRULE Rule for if not\nIF NOT condition\nENDRULE\n", actual);
   }

   @Test
   public void testDoAction() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule for action")
            .doAction("System.out.println()").exportAsString();

      Assert.assertEquals("\nRULE Rule for action\nIF TRUE\nDO System.out.println()\nENDRULE\n", actual);
   }

   @Test
   public void testDoActionWithStringParsing() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule for action")
            .doAction("System.out.println('Hello D''Alto')").exportAsString();

      Assert.assertEquals("\nRULE Rule for action\nIF TRUE\nDO System.out.println(\"Hello D'Alto\")\nENDRULE\n", actual);
   }

   @Test
   public void testDoActionWithoutStringParsing() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule for action")
            .doAction("System.out.println(\"Hello D''''Alto\")").exportAsString();

      Assert.assertEquals("\nRULE Rule for action\nIF TRUE\nDO System.out.println(\"Hello D''Alto\")\nENDRULE\n", actual);
   }

   @Test
   public void testAtThrow() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atThrow().exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT THROW\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtThrowAll() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atThrowAll().exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT THROW ALL\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtThrowCount() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atThrow(10).exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT THROW 10\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtSynchronizeCount() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atThrow(10).exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT THROW 10\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtSynchronize() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atSynchronize().exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT SYNCHRONIZE\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtSynchronizeAll() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atSynchronize(10).exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT SYNCHRONIZE 10\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testBindings() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").bind("$3", "buffer")
            .bind("$4", "size").exportAsString();

      Assert.assertEquals("\nRULE Rule\nBIND $3 = buffer;\n$4 = size\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testOverridingClass() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").classMatch("pkg.ClassName")
            .overriding().exportAsString();

      Assert.assertEquals("\nRULE Rule\nCLASS ^pkg.ClassName\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testOverridingInterface() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").interfaceMatch("pkg.InterfaceName")
            .overriding().exportAsString();

      Assert.assertEquals("\nRULE Rule\nINTERFACE ^pkg.InterfaceName\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtReadAll() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atReadAll("state").exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT READ state ALL\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtRead() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atRead("state").exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT READ state\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtReadCount() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atRead("state", 10).exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT READ state 10\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtWriteAll() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atWriteAll("state").exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT WRITE state ALL\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtWrite() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atWrite("state").exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT WRITE state\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtWriteCount() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atWrite("state", 10)
            .exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT WRITE state 10\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtInvokeAll() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atInvokeAll("state")
            .exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT INVOKE state ALL\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtInvoke() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atInvoke("state").exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT INVOKE state\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAtInvokeCount() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").atInvoke("state", 10)
            .exportAsString();

      Assert.assertEquals("\nRULE Rule\nAT INVOKE state 10\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAfterInvokeAll() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").afterInvokeAll("state")
            .exportAsString();

      Assert.assertEquals("\nRULE Rule\nAFTER INVOKE state ALL\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAfterInvoke() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").afterInvoke("state")
            .exportAsString();

      Assert.assertEquals("\nRULE Rule\nAFTER INVOKE state\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAfterInvokeCount() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").afterInvoke("state", 10)
            .exportAsString();

      Assert.assertEquals("\nRULE Rule\nAFTER INVOKE state 10\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAfterSynchronizeAll() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").afterSynchronizeAll()
            .exportAsString();

      Assert.assertEquals("\nRULE Rule\nAFTER SYNCHRONIZE ALL\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAfterSynchronize() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").afterSynchronize().exportAsString();

      Assert.assertEquals("\nRULE Rule\nAFTER SYNCHRONIZE\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAfterSynchronizeCount() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").afterSynchronize(10)
            .exportAsString();

      Assert.assertEquals("\nRULE Rule\nAFTER SYNCHRONIZE 10\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAfterReadAll() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").afterReadAll("state")
            .exportAsString();

      Assert.assertEquals("\nRULE Rule\nAFTER READ state ALL\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAfterRead() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").afterRead("state").exportAsString();

      Assert.assertEquals("\nRULE Rule\nAFTER READ state\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testAfterReadCount() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").afterRead("state", 10)
            .exportAsString();

      Assert.assertEquals("\nRULE Rule\nAFTER READ state 10\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testHelper() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class).rule("Rule").helper("pkg.HelperSub")
            .exportAsString();

      Assert.assertEquals("\nRULE Rule\nHELPER pkg.HelperSub\nIF TRUE\nENDRULE\n", actual);
   }

   @Test
   public void testParsingStringEscapeChars() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class)
            .rule("countdown at commit")
                .classMatch("Parser")
                .methodMatch("commit")
                .atRead("state")
                .bind("engine:CoordinatorEngine", "''\"''")
                .ifCondition("''\"'' == ''\"''")
                .doAction("debug(''\"'')")
                .doAction("return false")
            .exportAsString();

      String expected =
            "\nRULE countdown at commit"
            + "\nCLASS Parser"
            + "\nMETHOD commit"
            + "\nAT READ state"
            + "\nBIND engine:CoordinatorEngine = '\"'"
            + "\nIF '\"' == '\"'"
            + "\nDO debug('\"');"
            + "\nreturn false"
            + "\nENDRULE\n";

      Assert.assertEquals(expected, actual);
   }

   @Test
   public void testParsingStringEnabled() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class)
            .rule("countdown at commit")
                .classMatch("Parser")
                .methodMatch("commit")
                .atRead("state")
                .bind("engine:CoordinatorEngine", "'testing'")
                .ifCondition("'a' == 'a'")
                .doAction("debug('returning early with failure')")
                .doAction("return false")
            .exportAsString();

      String expected =
            "\nRULE countdown at commit"
            + "\nCLASS Parser"
            + "\nMETHOD commit"
            + "\nAT READ state"
            + "\nBIND engine:CoordinatorEngine = \"testing\""
            + "\nIF \"a\" == \"a\""
            + "\nDO debug(\"returning early with failure\");"
            + "\nreturn false"
            + "\nENDRULE\n";

      Assert.assertEquals(expected, actual);
   }

   @Test
   public void testParsingStringDisabled() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class)
            .rule("countdown at commit")
                .classMatch("Parser")
                .methodMatch("commit")
                .atRead("state")
                .bind("engine:CoordinatorEngine", "'testing'")
                .ifCondition("'a' == 'a'")
                .doAction("debug('returning early with failure')")
                .doAction("return false")
                .disableStringParsing()
            .exportAsString();

      String expected =
            "\nRULE countdown at commit"
            + "\nCLASS Parser"
            + "\nMETHOD commit"
            + "\nAT READ state"
            + "\nBIND engine:CoordinatorEngine = 'testing'"
            + "\nIF 'a' == 'a'"
            + "\nDO debug('returning early with failure');"
            + "\nreturn false"
            + "\nENDRULE\n";

      Assert.assertEquals(expected, actual);
   }

   @Test
   public void testComplexScenario() throws Exception
   {
      String actual = Descriptors.create(BytemanScriptDescriptor.class)
            .rule("countdown at commit")
                .classMatch("com.arjuna.wst11.messaging.engines.CoordinatorEngine")
                .methodMatch("commit")
                .atRead("state")
                .bind("engine:BINDEngine", "$this")
                .bind("recovered:boolean", "engine.isRecovered()")
                .bind("identifier:String", "'identifier'")
                .ifCondition("IFrecovered")
                .doAction("debug('DO ')")
                .doAction("return false")
            .exportAsString();

      String expected =
            "\nRULE countdown at commit"
            + "\nCLASS com.arjuna.wst11.messaging.engines.CoordinatorEngine"
            + "\nMETHOD commit"
            + "\nAT READ state"
            + "\nBIND engine:BINDEngine = $this;"
            + "\nrecovered:boolean = engine.isRecovered();"
            + "\nidentifier:String = \"identifier\""
            + "\nIF IFrecovered"
            + "\nDO debug(\"DO \");"
            + "\nreturn false"
            + "\nENDRULE\n";

      Assert.assertEquals(expected, actual);
   }


}
