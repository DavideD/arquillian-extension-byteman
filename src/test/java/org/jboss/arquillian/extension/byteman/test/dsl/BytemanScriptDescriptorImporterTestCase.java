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

package org.jboss.arquillian.extension.byteman.test.dsl;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.jboss.arquillian.extension.byteman.api.dsl.BytemanScriptDescriptor;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.junit.Test;

/**
 * Test the import of a byteman script from different sources.
 *
 * @author Davide D'Alto
 *
 */
public class BytemanScriptDescriptorImporterTestCase {

    @Test
    public void testImportRuleFromString() throws Exception {
        String script = "\nRULE importer rule\nINTERFACE BytemanClass\nMETHOD hullo\nAT EXIT\nIF TRUE\nENDRULE\n";
        BytemanScriptDescriptor imported = Descriptors.importAs(BytemanScriptDescriptor.class)
                .fromString(script);
        assertEquals(script, imported.exportAsString());
    }

    @Test
    public void testImportComplexRule() {
        String script =
                "\nRULE countdown at commit"
                + "\nCLASS com.arjuna.wst11.messaging.engines.CoordinatorEngine"
                + "\nMETHOD commit"
                + "\nAT READ state"
                + "\nBIND engine:CoordinatorEngine = $this;"
                + "\nrecovered:boolean = engine.isRecovered();"
                + "\nidentifier:String = engine.getId()"
                + "\nIF recovered"
                + "\nDO debug(\"returning early with failure\");"
                + "\nreturn false"
                + "\nENDRULE\n";

        BytemanScriptDescriptor imported = Descriptors.importAs(BytemanScriptDescriptor.class)
                .fromString(script);
        assertEquals(script, imported.exportAsString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImportNullString() throws Exception {
        String script = null;
        Descriptors.importAs(BytemanScriptDescriptor.class).fromString(script);
    }

    @Test
    public void testImportEmptyString() throws Exception {
        String script = "";
        BytemanScriptDescriptor imported = Descriptors.importAs(BytemanScriptDescriptor.class)
                .fromString(script);
        assertEquals(script, imported.exportAsString());
    }

    @Test
    public void testImportNullInputStream() throws Exception {
        InputStream script = Thread.currentThread().getContextClassLoader().getResourceAsStream("test-importer.bm");
        BytemanScriptDescriptor imported = Descriptors.importAs(BytemanScriptDescriptor.class)
                .fromStream(script);
        assertEquals("\nRULE Testing import\nCLASS WhateverWorks\nMETHOD whatMethod\nAT THROW\nIF TRUE\nENDRULE\n",
                imported.exportAsString());
    }
}
