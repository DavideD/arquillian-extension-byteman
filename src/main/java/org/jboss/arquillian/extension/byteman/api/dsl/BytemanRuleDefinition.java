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

package org.jboss.arquillian.extension.byteman.api.dsl;

/**
 * Fluent API representation of a single bytean rule.
 *
 * @author Davide D'Alto
 *
 */
public interface BytemanRuleDefinition extends BytemanScriptDescriptor {

    BytemanRuleDefinition disableStringParsing();

    BytemanRuleDefinition classMatch(String className);

    BytemanRuleDefinition methodMatch(String methodName);

    BytemanRuleDefinition interfaceMatch(String interfaceName);

    BytemanRuleDefinition overriding();

    BytemanRuleDefinition atEntry();

    BytemanRuleDefinition atExit();

    BytemanRuleDefinition atLine(int num);

    BytemanRuleDefinition atSynchronize();

    BytemanRuleDefinition atSynchronizeAll();

    BytemanRuleDefinition atSynchronize(int count);

    BytemanRuleDefinition atThrow();

    BytemanRuleDefinition atThrowAll();

    BytemanRuleDefinition atThrow(int count);

    BytemanRuleDefinition ifCondition(String condition);

    BytemanRuleDefinition ifNotCondition(String condition);

    BytemanRuleDefinition doAction(String action);

    BytemanRuleDefinition bind(String param, String binding);

    BytemanRuleDefinition atRead(String field);

    BytemanRuleDefinition atRead(String field, int i);

    BytemanRuleDefinition atReadAll(String field);

    BytemanRuleDefinition atWrite(String field);

    BytemanRuleDefinition atWrite(String field, int i);

    BytemanRuleDefinition atWriteAll(String field);

    BytemanRuleDefinition atInvoke(String field);

    BytemanRuleDefinition atInvoke(String field, int i);

    BytemanRuleDefinition atInvokeAll(String field);

    BytemanRuleDefinition afterInvoke(String field);

    BytemanRuleDefinition afterInvoke(String field, int i);

    BytemanRuleDefinition afterInvokeAll(String field);

    BytemanRuleDefinition afterSynchronize();

    BytemanRuleDefinition afterSynchronize(int i);

    BytemanRuleDefinition afterSynchronizeAll();

    BytemanRuleDefinition afterRead(String field);

    BytemanRuleDefinition afterRead(String field, int i);

    BytemanRuleDefinition afterReadAll(String field);

    BytemanRuleDefinition helper(String helper);

}
