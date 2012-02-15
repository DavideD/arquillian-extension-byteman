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
package org.jboss.arquillian.extension.byteman.impl.dsl;

import java.io.OutputStream;

import org.jboss.arquillian.extension.byteman.api.dsl.BytemanRuleDefinition;
import org.jboss.arquillian.extension.byteman.api.dsl.BytemanScriptDescriptor;
import org.jboss.shrinkwrap.descriptor.api.DescriptorExportException;
import org.jboss.shrinkwrap.descriptor.api.DescriptorExporter;
import org.jboss.shrinkwrap.descriptor.spi.DescriptorImplBase;

/**
 * Default implementation of {@link BytemanScriptDescriptor}.
 *
 * @author Davide D'Alto
 */
public class BytemanScriptDsl extends DescriptorImplBase<BytemanScriptDescriptor> implements BytemanScriptDescriptor
{
   private final String descriptorName;

   private final BytemanScriptModel scriptModel;

   public BytemanScriptDsl(String descriptorName) {
     this(descriptorName, new BytemanScriptModel());
   }

   BytemanScriptDsl(String descriptorName, BytemanScriptModel scriptModel) {
     super(descriptorName);
     this.descriptorName = descriptorName;
     this.scriptModel = scriptModel;
   }

   @Override
   public void exportTo(OutputStream output) throws DescriptorExportException, IllegalArgumentException
   {
      scriptModel.exportRules(output);
   }

   @Override
   protected DescriptorExporter<BytemanScriptDescriptor> getExporter()
   {
      return new BytemanScriptDescriptorExporter();
   }

   @Override
   public String getDescriptorName()
   {
      return descriptorName;
   }

   @Override
   public BytemanRuleDefinition rule(String ruleName)
   {
      return new BytemanRuleDsl(descriptorName, scriptModel, ruleName);
   }

}
