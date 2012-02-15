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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.extension.byteman.api.dsl.BytemanRuleDefinition;
import org.jboss.arquillian.extension.byteman.api.dsl.BytemanScriptDescriptor;
import org.jboss.byteman.agent.Location;
import org.jboss.byteman.agent.LocationType;
import org.jboss.byteman.agent.RuleScript;
import org.jboss.byteman.agent.ScriptRepository;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;

/**
 * Create an instance of {@link BytemanScriptDescriptor} from an {@link InputStream}.
 *
 * @author Davide D'Alto
 */
public class BytemanScriptDescriptorImporterDelegate {

    private final Logger log = Logger.getLogger(BytemanScriptDescriptorImporter.class.getName());

    public BytemanScriptDescriptor importStream(InputStream source, boolean close) throws Exception
    {
        try {
            return importStream(source);
        } finally {
            if (close && source != null) {
                close(source);
            }
        }
    }

    private BytemanScriptDescriptor importStream(InputStream source) throws Exception
    {
        ScriptRepository repository = new ScriptRepository(false);
        List<RuleScript> rules = repository.processScripts(convertStreamToString(source), "");
        BytemanScriptDescriptor scriptDescriptor = Descriptors.create(BytemanScriptDescriptor.class);
        for (RuleScript rule : rules) {
            createRuleDefinition(scriptDescriptor, rule);
        }
        return scriptDescriptor;
    }

    private void createRuleDefinition(BytemanScriptDescriptor descriptor, RuleScript rule) {
        BytemanRuleDefinition ruleDef = descriptor.rule(rule.getName());
        if (rule.isInterface()) {
            ruleDef.interfaceMatch(rule.getTargetClass());
        } else {
            ruleDef.classMatch(rule.getTargetClass());
        }
        if (rule.isOverride())
            ruleDef.overriding();
        ruleDef.helper(rule.getTargetHelper());
        ruleDef.methodMatch(rule.getTargetMethod());

        parseRuleText(ruleDef, rule.getRuleText());
        location(ruleDef, rule);
    }

    private void parseRuleText(BytemanRuleDefinition ruleDef, String ruleText) {
        if (ruleText != null) {
            String source = ruleText;
            if (source.startsWith("BIND")) {
                source = parseBind(ruleDef, source);
            }
            if (source.startsWith("IF")) {
                source = parseIf(ruleDef, source);
            }
            if (source.startsWith("DO")) {
                source = parseDo(ruleDef, source);
            }
        }
    }

    private String parseDo(BytemanRuleDefinition ruleDef, String ruleText) {
        String startString = "DO ";
        int startIndex = ruleText.indexOf(startString);
        String actions = ruleText.substring(startIndex + startString.length(), ruleText.length());
        ruleDef.doAction(actions);
        return "";
    }

    private String parseBind(BytemanRuleDefinition ruleDef, String ruleText) {
        String startString = "BIND ";
        int endIndex = ruleText.indexOf("\nIF ");
        String bindings = ruleText.substring(startString.length(), endIndex);
        int equalIndex = bindings.indexOf("=");
        if (equalIndex > -1) {
            String param = bindings.substring(0, equalIndex).trim();
            String binding = bindings.substring(equalIndex + 1, bindings.length()).trim();
            ruleDef.bind(param, binding);
        }
        return ruleText.substring(endIndex + 1);
    }

    private String parseIf(BytemanRuleDefinition ruleDef, String ruleText) {
        String startString = "IF ";
        int startIndex = ruleText.indexOf(startString);
        int endIndex = ruleText.indexOf("\nDO ");
        String result;
        if (endIndex == -1) {
            endIndex = ruleText.length();
            result = ruleText.substring(endIndex);
        } else {
            result = ruleText.substring(endIndex + 1);
        }
        String ifCondition = ruleText.substring(startIndex + startString.length(), endIndex);
        ruleDef.ifCondition(ifCondition);
        return result;
    }

    private String convertStreamToString(java.io.InputStream is) {
        Scanner scanner = new java.util.Scanner(is).useDelimiter("\\A");
        if (scanner.hasNext())
            return scanner.next();

        return "";
    }

    private void location(BytemanRuleDefinition ruleDef, RuleScript rule) {
        Location location = rule.getTargetLocation();
        String parameterText = LocationType.parameterText(location.toString());
        switch (location.getLocationType()) {
            case ENTRY:
                ruleDef.atEntry();
                break;
            case EXIT:
                ruleDef.atExit();
                break;
            case INVOKE:
                ruleDef.atInvoke(parameterText);
                break;
            case INVOKE_COMPLETED:
                ruleDef.afterInvoke(parameterText);
                break;
            case LINE:
                ruleDef.atLine(Integer.valueOf(parameterText));
                break;
            case READ:
                ruleDef.atRead(parameterText);
                break;
            case READ_COMPLETED:
                ruleDef.afterRead(parameterText);
                break;
            case SYNCHRONIZE:
                ruleDef.atSynchronize();
                break;
            case SYNCHRONIZE_COMPLETED:
                ruleDef.afterSynchronize();
                break;
            case THROW:
                ruleDef.atThrow();
                break;
            case WRITE:
                ruleDef.atWrite(parameterText);
                break;
            case WRITE_COMPLETED:
                ruleDef.atWrite(parameterText);
                break;
            default:
                throw new UnsupportedOperationException("Location not recognized during import: " + location.getLocationType());
        }
    }

    private void close(InputStream stream) {
        try {
            stream.close();
        } catch (final IOException i) {
            log.log(Level.WARNING, "Unclosable stream specified to be closed: {0}", stream);
        }
    }

}
