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

package org.jboss.arquillian.extension.byteman.impl.dsl;

import java.io.OutputStream;

import org.jboss.arquillian.extension.byteman.api.dsl.BytemanScriptDescriptor;
import org.jboss.shrinkwrap.descriptor.api.DescriptorExportException;
import org.jboss.shrinkwrap.descriptor.api.DescriptorExporter;

/**
 * Default implementation of a {@link DescriptorExporter} for the {@link BytemanScriptDescriptor}.
 *
 * @author "Davide D'Alto"
 *
 */
public class BytemanScriptDescriptorExporter implements DescriptorExporter<BytemanScriptDescriptor> {

    /* (non-Javadoc)
     * @see org.jboss.shrinkwrap.descriptor.api.DescriptorExporter#to(org.jboss.shrinkwrap.descriptor.api.Descriptor, java.io.OutputStream)
     */
    @Override
    public void to(BytemanScriptDescriptor descriptor, OutputStream out) throws DescriptorExportException,
            IllegalArgumentException {
        descriptor.exportTo(out);
    }

}
