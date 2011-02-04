/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.arquillian.framework.byteman.test;



import javax.ejb.EJB;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.framework.byteman.api.BMRule;
import org.jboss.arquillian.framework.byteman.api.BMRules;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * BytemanIntegrationTestCase
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
@BMRules(
      @BMRule(
            name = "Throw exception on success", 
            targetClass = "StatelessManagerBean", 
            targetMethod = "success",
            action = "throw new java.lang.RuntimeException()")
)
@RunWith(Arquillian.class)
public class BytemanIntegrationTestCase
{
   /* possible future
   @BMRule @DeploymentTarget
   public static String createRule()
   {
      return Byteman.rule("Throw exception on success")
               .target(StatelessTestBean.class)
               .target("success")
               .actionThrow(Exception.class);
   }
   */
   
   @Deployment
   public static Archive<?> createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
               .addClasses(StatelessManager.class, StatelessManagerBean.class);
   }
   
   @EJB
   private StatelessManager bean;
   
   @Test
   public void shouldBeAbleToInjectThrowRule() 
   {
      Assert.assertNotNull("Verify bean was injected", bean);
      
      try
      {
         bean.success();
         Assert.fail("A Exception should have been injected into target class");
      }
      catch (Exception e) 
      {
         // verify my exception? 
      }
   }
}
