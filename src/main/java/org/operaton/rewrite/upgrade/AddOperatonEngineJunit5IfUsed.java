/*
 * Copyright 2026 the Operaton contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.operaton.rewrite.upgrade;

import org.openrewrite.ExecutionContext;
import org.openrewrite.ScanningRecipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.maven.MavenIsoVisitor;
import org.openrewrite.xml.AddToTagVisitor;
import org.openrewrite.xml.XPathMatcher;
import org.openrewrite.xml.tree.Xml;

import java.util.concurrent.atomic.AtomicBoolean;

public class AddOperatonEngineJunit5IfUsed extends ScanningRecipe<AtomicBoolean> {

    private static final String GROUP_ID = "org.operaton.bpm";
    private static final String ARTIFACT_ID = "operaton-engine";
    private static final String CLASSIFIER = "junit5";
    private static final String SCOPE = "test";
    private static final String CLASS_PACKAGE = "org.operaton.bpm.engine.test.junit5.";

    private static final XPathMatcher DEPENDENCIES_MATCHER =
            new XPathMatcher("/project/dependencies");

    @Override
    public String getDisplayName() {
        return "Add operaton-engine junit5 classifier dependency if relocated classes are used";
    }

    @Override
    public String getDescription() {
        return "Adds org.operaton.bpm:operaton-engine with the junit5 classifier and test scope " +
               "when any of the relocated junit5 extension classes " +
               "(ParameterizedTestExtension, ProcessEngineLoggingExtension, WatchLogger) are used.";
    }

    @Override
    public AtomicBoolean getInitialValue(ExecutionContext ctx) {
        return new AtomicBoolean(false);
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getScanner(AtomicBoolean acc) {
        return new JavaIsoVisitor<ExecutionContext>() {
            @Override
            public J.Import visitImport(J.Import import_, ExecutionContext ctx) {
                if (import_.getTypeName().startsWith(CLASS_PACKAGE)) {
                    acc.set(true);
                }
                return super.visitImport(import_, ctx);
            }
        };
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor(AtomicBoolean acc) {
        return new MavenIsoVisitor<ExecutionContext>() {
            @Override
            public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext ctx) {
                if (acc.get()) {
                    if (DEPENDENCIES_MATCHER.matches(getCursor())) {
                        if (!hasNewDependency(tag)) {
                            doAfterVisit(new AddToTagVisitor<>(tag, buildDependencyTag()));
                        }
                    } else if (isProjectTag() && tag.getChild("dependencies").isEmpty()) {
                        doAfterVisit(new AddToTagVisitor<>(tag, buildDependenciesTag()));
                    }
                }
                return super.visitTag(tag, ctx);
            }
        };
    }

    private static boolean hasNewDependency(Xml.Tag dependenciesTag) {
        return dependenciesTag.getChildren().stream()
                .anyMatch(dep ->
                        GROUP_ID.equals(dep.getChildValue("groupId").orElse(""))
                        && ARTIFACT_ID.equals(dep.getChildValue("artifactId").orElse(""))
                        && CLASSIFIER.equals(dep.getChildValue("classifier").orElse("")));
    }

    private static Xml.Tag buildDependencyTag() {
        return Xml.Tag.build(
                "<dependency>\n" +
                "    <groupId>" + GROUP_ID + "</groupId>\n" +
                "    <artifactId>" + ARTIFACT_ID + "</artifactId>\n" +
                "    <classifier>" + CLASSIFIER + "</classifier>\n" +
                "    <scope>" + SCOPE + "</scope>\n" +
                "</dependency>"
        );
    }

    private static Xml.Tag buildDependenciesTag() {
        return Xml.Tag.build(
                "<dependencies>\n" +
                "    <dependency>\n" +
                "        <groupId>" + GROUP_ID + "</groupId>\n" +
                "        <artifactId>" + ARTIFACT_ID + "</artifactId>\n" +
                "        <classifier>" + CLASSIFIER + "</classifier>\n" +
                "        <scope>" + SCOPE + "</scope>\n" +
                "    </dependency>\n" +
                "</dependencies>"
        );
    }
}
