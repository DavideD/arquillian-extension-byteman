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
package org.jboss.arquillian.framework.byteman.impl;

import java.util.Arrays;
import java.util.Collection;

import org.jboss.arquillian.framework.byteman.impl.container.AgentInstaller;
import org.jboss.arquillian.framework.byteman.impl.container.ScriptInstaller;
import org.jboss.arquillian.framework.byteman.impl.container.ScriptUnInstaller;
import org.jboss.arquillian.spi.Profile;

/**
 * BytemanProfile
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class BytemanProfile implements Profile
{
   //@SuppressWarnings("unchecked")
   public Collection<Class<?>> getClientProfile()
   {
      return Arrays.asList
      (
//            ScriptInstaller.class,
//            ScriptUnInstaller.class
      );
   }

   @SuppressWarnings("unchecked")
   public Collection<Class<?>> getContainerProfile()
   {
      return Arrays.asList
      (
            AgentInstaller.class,
            ScriptInstaller.class,
            ScriptUnInstaller.class
      );
   }
}
