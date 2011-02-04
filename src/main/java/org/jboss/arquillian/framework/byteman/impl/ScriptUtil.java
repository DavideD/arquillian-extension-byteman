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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.jboss.arquillian.framework.byteman.api.BMRule;

/**
 * ScriptUtil
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public final class ScriptUtil
{
   private ScriptUtil() {}
   
   public static String constructScriptText(BMRule... bmRules) {
      StringBuilder builder = new StringBuilder();
      builder.append("# BMUnit autogenerated script ");
      for (BMRule bmRule : bmRules) {
          builder.append("\nRULE ");
          builder.append(bmRule.name());
          if (bmRule.isInterface()) {
              builder.append("\nINTERFACE ");
          } else {
              builder.append("\nCLASS ");
          }
          if (bmRule.isOverriding()) {
              builder.append("^");
          }
          builder.append(bmRule.targetClass());
          builder.append("\nMETHOD ");
          builder.append(bmRule.targetMethod());
          String location = bmRule.targetLocation();
          if (location  !=  null && location.length() > 0) {
              builder.append("\nAT ");
              builder.append(location);
          }
          String helper = bmRule.helper();
          if (helper  !=  null && helper.length() > 0) {
              builder.append("\nHELPER ");
              builder.append(helper);
          }
          builder.append("\nIF ");
          builder.append(bmRule.condition());
          builder.append("\nDO ");
          builder.append(bmRule.action());
          builder.append("\nENDRULE\n");
      }
      return builder.toString();
  }

   public static String toString(InputStream input)
   {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      copy(input, output);
      return output.toString();
   }

   public static void copy(InputStream input, OutputStream output)
   {
      try
      {
         final byte[] buffer = new byte[4096];
         int read = 0;
         while ((read = input.read(buffer)) != -1)
         {
            output.write(buffer, 0, read);
         }

         output.flush();
      }
      catch (Exception e) 
      {
         throw new RuntimeException("Could not read script file", e);
      }
      finally
      {
         try 
         {
            input.close();
         } catch (Exception e) {  }

         try 
         {
            output.close();
         } catch (Exception e) {  }
      }
   }
}
