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

import java.io.InputStream;

import org.jboss.arquillian.extension.byteman.api.dsl.BytemanScriptDescriptor;
import org.jboss.shrinkwrap.descriptor.api.DescriptorImportException;
import org.jboss.shrinkwrap.descriptor.api.DescriptorImporter;
import org.jboss.shrinkwrap.descriptor.spi.DescriptorImporterBase;

/**
 * Default implementation of {@link DescriptorImporter} for {@link BytemanScriptDescriptor}.
 *
 * @author Davide D'Alto
 */
public class BytemanScriptDescriptorImporter extends DescriptorImporterBase<BytemanScriptDescriptor> implements DescriptorImporter<BytemanScriptDescriptor>
{

   private static final BytemanScriptDescriptorImporterDelegate IMPORTER = new BytemanScriptDescriptorImporterDelegate();

   public BytemanScriptDescriptorImporter(Class<BytemanScriptDescriptor> endUserViewImplType, String descriptorName)
         throws IllegalArgumentException
   {
      super(endUserViewImplType, descriptorName);
   }

   /* (non-Javadoc)
    * @see org.jboss.shrinkwrap.descriptor.api.DescriptorImporter#fromStream(java.io.InputStream, boolean)
    */
   @Override
   public BytemanScriptDescriptor fromStream(InputStream source, boolean close) throws IllegalArgumentException,
           DescriptorImportException
   {
       if (source == null)
           throw new IllegalArgumentException("InputStream  must be specified");

      try {
         return IMPORTER.importStream(source, close);
      } catch (final Exception e) {
         throw new DescriptorImportException("Could not import Byteman script from stream", e);
      }
   }

}
