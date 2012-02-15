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

import org.jboss.arquillian.extension.byteman.api.dsl.BytemanRuleDefinition;
import org.jboss.byteman.agent.LocationType;

/**
 *
 * Define a rule inside a byteman script.
 *
 * @author Davide D'Alto
 */
public class BytemanRuleDsl extends BytemanScriptDsl implements BytemanRuleDefinition {

    private final BytemanRuleModel model;

    public BytemanRuleDsl(String descriptorName, BytemanScriptModel scriptModel, String ruleName) {
        super(descriptorName, scriptModel);
        this.model = new BytemanRuleModel(ruleName);
        scriptModel.addRule(this.model);
    }

    @Override
    public BytemanRuleDefinition classMatch(String className) {
        model.setTargetClass(className, false);
        return this;
    }

    @Override
    public BytemanRuleDefinition interfaceMatch(String interfaceName) {
        model.setTargetClass(interfaceName, true);
        return this;
    }

    @Override
    public BytemanRuleDefinition methodMatch(String methodName) {
        model.setMethod(methodName);
        return this;
    }

    @Override
    public BytemanRuleDefinition overriding() {
        model.overridesTargetClass();
        return this;
    }

    @Override
    public BytemanRuleDefinition atEntry() {
        model.setLocationType(LocationType.ENTRY);
        return this;
    }

    @Override
    public BytemanRuleDefinition atExit() {
        model.setLocationType(LocationType.EXIT);
        return this;
    }

    @Override
    public BytemanRuleDefinition atLine(int num) {
        model.setLocationType(LocationType.LINE, num);
        return this;
    }

    @Override
    public BytemanRuleDefinition ifCondition(String string) {
        model.setIfCondition(string);
        return this;
    }

    @Override
    public BytemanRuleDefinition ifNotCondition(String condition) {
        model.setIfCondition("NOT " + condition);
        return this;
    }

    @Override
    public BytemanRuleDefinition doAction(String action) {
        model.addAction(action);
        return this;
    }

    @Override
    public BytemanRuleDefinition bind(String param, String binding) {
        model.bind(param + " = " +  binding);
        return this;
    }

    @Override
    public BytemanRuleDefinition atRead(String field) {
        model.setLocationType(LocationType.READ, field);
        return this;
    }

    @Override
    public BytemanRuleDefinition atRead(String field, int i) {
        return atRead(withCount(field, i));
    }

    @Override
    public BytemanRuleDefinition atReadAll(String field) {
        return atRead(all(field));
    }

    @Override
    public BytemanRuleDefinition atWrite(String field) {
        model.setLocationType(LocationType.WRITE, field);
        return this;
    }

    @Override
    public BytemanRuleDefinition atWrite(String field, int i) {
        return atWrite(withCount(field, i));
    }

    @Override
    public BytemanRuleDefinition atWriteAll(String field) {
        return atWrite(all(field));
    }

    @Override
    public BytemanRuleDefinition atInvoke(String field) {
        model.setLocationType(LocationType.INVOKE, field);
        return this;
    }

    @Override
    public BytemanRuleDefinition atInvoke(String field, int i) {
        return atInvoke(withCount(field, i));
    }

    @Override
    public BytemanRuleDefinition atInvokeAll(String field) {
        return atInvoke(all(field));
    }

    @Override
    public BytemanRuleDefinition afterInvoke(String field) {
        model.setLocationType(LocationType.INVOKE_COMPLETED, field);
        return this;
    }

    @Override
    public BytemanRuleDefinition afterInvoke(String field, int i) {
        return afterInvoke(withCount(field, i));
    }

    @Override
    public BytemanRuleDefinition afterInvokeAll(String field) {
        return afterInvoke(all(field));
    }

    @Override
    public BytemanRuleDefinition afterSynchronize() {
        model.setLocationType(LocationType.SYNCHRONIZE_COMPLETED);
        return this;
    }

    @Override
    public BytemanRuleDefinition afterSynchronize(int i) {
        model.setLocationType(LocationType.SYNCHRONIZE_COMPLETED, String.valueOf(i));
        return this;
    }

    @Override
    public BytemanRuleDefinition afterSynchronizeAll() {
        model.setLocationType(LocationType.SYNCHRONIZE_COMPLETED, all());
        return this;
    }

    @Override
    public BytemanRuleDefinition helper(String helper) {
        model.setHelper(helper);
        return this;
    }

    @Override
    public BytemanRuleDefinition afterRead(String field) {
        model.setLocationType(LocationType.READ_COMPLETED, field);
        return this;
    }

    @Override
    public BytemanRuleDefinition afterRead(String field, int i) {
        return afterRead(withCount(field, i));
    }

    @Override
    public BytemanRuleDefinition afterReadAll(String field) {
        return afterRead(all(field));
    }

    @Override
    public BytemanRuleDefinition atSynchronize() {
        model.setLocationType(LocationType.SYNCHRONIZE);
        return this;
    }

    @Override
    public BytemanRuleDefinition atSynchronizeAll() {
        model.setLocationType(LocationType.SYNCHRONIZE, all());
        return this;
    }

    @Override
    public BytemanRuleDefinition atSynchronize(int count) {
        model.setLocationType(LocationType.SYNCHRONIZE, count);
        return this;
    }

    @Override
    public BytemanRuleDefinition atThrow() {
        model.setLocationType(LocationType.THROW);
        return this;
    }

    @Override
    public BytemanRuleDefinition atThrowAll() {
        model.setLocationType(LocationType.THROW, all());
        return this;
    }

    @Override
    public BytemanRuleDefinition atThrow(int count) {
        model.setLocationType(LocationType.THROW, count);
        return this;
    }

    @Override
    public BytemanRuleDefinition disableStringParsing() {
        model.disableStringParsing();
        return this;
    }

    private String all() {
        return all("").trim();
    }

    private String all(String field) {
        return field + " ALL";
    }

    private String withCount(String field, int i) {
        return field + " " + i;
    }
}
