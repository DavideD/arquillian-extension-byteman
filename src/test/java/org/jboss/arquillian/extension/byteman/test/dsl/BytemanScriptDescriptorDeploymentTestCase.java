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

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.extension.byteman.api.BMRule;
import org.jboss.arquillian.extension.byteman.api.dsl.BytemanScriptDescriptor;
import org.jboss.arquillian.extension.byteman.test.model.StatelessManager;
import org.jboss.arquillian.extension.byteman.test.model.StatelessManagerBean;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test the rules deployment using {@link BMRule} annotation.
 *
 * @version $Revision: $
 */
@RunWith(Arquillian.class)
public class BytemanScriptDescriptorDeploymentTestCase
{

   @BMRule
   public static BytemanScriptDescriptor createExceptionRule()
   {
      return Descriptors.create(BytemanScriptDescriptor.class)

              .rule("Throw exception on success")
                  .classMatch("StatelessManagerBean")
                  .methodMatch("forcedMethodLevelFailure")
                  .doAction("throw new java.lang.RuntimeException('BYTEMAN')")

               .rule("Return a message on success")
                  .interfaceMatch("StatelessManager")
                  .methodMatch("readNothing")
                  .atEntry()
                  .doAction("debug('BYTEMAN was here')")
                  .doAction("return 'BYTEMAN was here'")
               ;
   }

   @Deployment
   @OverProtocol("Servlet 3.0")
   public static Archive<?> createDeployment()
   {
      return ShrinkWrap.create(WebArchive.class).addClasses(StatelessManager.class, StatelessManagerBean.class);
   }

   @EJB(mappedName = "java:module/StatelessManagerBean")
   private StatelessManager bean;

   @Test
   public void shouldBeAbleToInjectMethodLevelReturnRule()
   {
      Assert.assertEquals("BYTEMAN was here", bean.readNothing());
   }

   @Test
   public void shouldBeAbleToInjectMethodLevelThrowRule()
   {
      try {
          bean.forcedMethodLevelFailure();
          Assert.fail("Byteman rule not fired");
      } catch (Exception e) {
          Assert.assertEquals("java.lang.RuntimeException: BYTEMAN", e.getMessage());
      }
   }
}