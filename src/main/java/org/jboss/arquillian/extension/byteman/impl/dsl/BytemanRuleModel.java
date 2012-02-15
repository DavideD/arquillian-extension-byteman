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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.byteman.agent.Location;
import org.jboss.byteman.agent.LocationType;

/**
 * Represent a single byteman rule.
 *
 * @author Davide D'Alto
 */
public class BytemanRuleModel {

    private final String rule;

    private String targetClass;

    private boolean isInterface = false;

    private String methodName;

    private List<String> actions = new ArrayList<String>();

    private LocationType locationType;

    private String locationParams;

    private String ifCondition = "TRUE";

    private boolean overriding;

    private List<String> bindings = new ArrayList<String>();

    private String helper;

    private boolean stringParsingEnabled = true;

    public BytemanRuleModel(String ruleName) {
        validate(ruleName);
        this.rule = ruleName;
    }

    protected BytemanRuleModel(
            String ruleName,
            String targetClass,
            boolean isInterface,
            String methodName,
            List<String> actions,
            LocationType locationType,
            String locationParams,
            String ifCondition,
            boolean overriding,
            List<String> bindings,
            String helper,
            boolean stringParsingEnabled) {
        this(ruleName);
        this.targetClass = targetClass;
        this.isInterface = isInterface;
        this.methodName = methodName;
        this.actions = actions;
        this.locationType = locationType;
        this.locationParams = locationParams;
        this.ifCondition = ifCondition;
        this.overriding = overriding;
        this.bindings = bindings;
        this.helper = helper;
        this.stringParsingEnabled = stringParsingEnabled;
    }

    private void validate(String rule) {
        if (rule == null || rule.trim().isEmpty())
            throw new IllegalArgumentException("Rule field should contains at least a non white character");
    }

    public void setTargetClass(String className, boolean isInterface) {
        this.targetClass = className;
        this.isInterface = isInterface;
    }

    public void setMethod(String methodName) {
        this.methodName = methodName;
    }

    public void setIfCondition(String cond) {
        this.ifCondition = cond;
    }

    public void exportTo(OutputStream output) {
        try {
            exportRuleTo(output, stringParsingEnabled);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportRuleTo(OutputStream output, boolean stringParsingEnabled) throws IOException {
        StringBuilder builder = new StringBuilder();
        appendIfNotEmpty(builder, "\nRULE ", rule);
        String type = isInterface ? "\nINTERFACE " : "\nCLASS ";
        appendIfNotEmpty(builder, type, overriding(targetClass));
        appendIfNotEmpty(builder, "\nMETHOD ", methodName);
        appendIfNotEmpty(builder, "\nHELPER ", helper);
        appendIfNotEmpty(builder, "\n", location());
        appendIfNotNull(builder, "\nBIND ", bindings(stringParsingEnabled));
        appendIfNotNull(builder, "\nIF ", ifConditions(stringParsingEnabled));
        appendIfNotNull(builder, "\nDO ", actions(stringParsingEnabled));
        appendIfNotEmpty(builder, "\nENDRULE", "\n");
        output.write(builder.toString().getBytes());
    }

    private List<String> bindings(boolean stringParsingEnabled) {
        return parseIfNeeded(stringParsingEnabled, bindings);
    }

    private List<String> ifConditions(boolean stringParsingEnabled) {
        List<String> conditions = Arrays.asList(ifCondition);

        return parseIfNeeded(stringParsingEnabled, conditions);
    }

    private List<String> actions(boolean stringParsingEnabled) {
        return parseIfNeeded(stringParsingEnabled, actions);
    }

    private List<String> parseIfNeeded(boolean stringParsingEnabled, List<String> values) {
        if (stringParsingEnabled)
            return parse(values);

        return values;
    }

    private String location() {
        if (locationType == null)
            return null;

        String params = locationParams == null ? "" : locationParams;
        return Location.create(locationType, params).toString();
    }

    private List<String> parse(List<String> actions) {
        List<String> parsedActions = new ArrayList<String>();
        for (String action : actions) {
            parsedActions.add(parse(action));
        }
        return parsedActions;
    }

    private String parse(String action) {
        if (action == null)
            return null;

        String[] split = action.split("''");
        StringBuilder parsed = new StringBuilder();
        for (String part : split) {
            parsed.append(part.replaceAll("'", "\""));
            parsed.append("'");
        }

        if (action.endsWith("''"))
            return parsed.toString();

        return parsed.substring(0, parsed.length() - 1);
    }

    private String overriding(String name) {
        if (name == null)
            return null;

        if (overriding)
            return "^" + name;

        return name;
    }

    private void appendIfNotNull(StringBuilder builder, String string, List<String> list) {
        if (list.isEmpty())
            return;

        appendValues(builder, string, list);
    }

    private void appendValues(StringBuilder builder, String start, List<String> values) {
        StringBuilder valueBuilder = new StringBuilder();
        for (String value : values) {
            valueBuilder.append(";\n");
            valueBuilder.append(value);
        }

        if (valueBuilder.length() > 2) {
            builder.append(start);
            builder.append(valueBuilder.substring(2));
        }
    }

    private void appendIfNotEmpty(StringBuilder builder, String string, String param) {
        if (isNotEmpty(param)) {
            builder.append(string);
            builder.append(param);
        }
    }

    private boolean isNotEmpty(String param) {
        return !isEmpty(param);
    }

    private boolean isEmpty(String param) {
        return param == null ||  param.isEmpty();
    }

    public void addAction(String action) {
        this.actions.add(action);
    }

    public void bind(String binding) {
        if (isNotEmpty(binding))
            this.bindings.add(binding);
    }

    public void overridesTargetClass() {
        this.overriding = true;
    }

    public void setHelper(String helper) {
        this.helper = helper;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public void setLocationType(LocationType locationType, Object params) {
        setLocationType(locationType);
        this.locationParams = params.toString();
    }

    public void disableStringParsing() {
        this.stringParsingEnabled  = false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "BytemanRuleModel [" + (rule != null ? "rule=" + rule + ", " : "")
                + (targetClass != null ? "targetClass=" + targetClass + ", " : "") + "isInterface=" + isInterface + ", "
                + (methodName != null ? "methodName=" + methodName + ", " : "")
                + (actions != null ? "actions=" + actions + ", " : "")
                + (locationType != null ? "locationType=" + locationType + ", " : "")
                + (locationParams != null ? "locationParams=" + locationParams + ", " : "")
                + (ifCondition != null ? "ifCondition=" + ifCondition + ", " : "") + "overriding=" + overriding + ", "
                + (bindings != null ? "bindings=" + bindings + ", " : "") + (helper != null ? "helper=" + helper : "") + "]";
    }

}
