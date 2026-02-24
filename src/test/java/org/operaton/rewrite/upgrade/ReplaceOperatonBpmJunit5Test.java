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

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;
import static org.openrewrite.maven.Assertions.pomXml;

class ReplaceOperatonBpmJunit5Test implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.upgrade.ReplaceOperatonBpmJunit5");
    }

    @Test
    void removesDependencyAndAddsClassifiedDependencyWhenParameterizedTestExtensionIsUsed() {
        rewriteRun(
          java(
            """
            import org.operaton.bpm.engine.test.junit5.ParameterizedTestExtension;

            class MyTest {
            }
            """
          ),
          pomXml(
            """
            <project>
                <modelVersion>4.0.0</modelVersion>
                <groupId>com.example</groupId>
                <artifactId>example</artifactId>
                <version>1.0.0</version>
                <dependencies>
                    <dependency>
                        <groupId>org.operaton.bpm</groupId>
                        <artifactId>operaton-bpm-junit5</artifactId>
                        <version>1.0.0</version>
                        <scope>test</scope>
                    </dependency>
                </dependencies>
            </project>
            """,
            """
            <project>
                <modelVersion>4.0.0</modelVersion>
                <groupId>com.example</groupId>
                <artifactId>example</artifactId>
                <version>1.0.0</version>
                <dependencies>
                    <dependency>
                        <groupId>org.operaton.bpm</groupId>
                        <artifactId>operaton-engine</artifactId>
                        <classifier>junit5</classifier>
                        <scope>test</scope>
                    </dependency>
                </dependencies>
            </project>
            """
          )
        );
    }

    @Test
    void removesDependencyAndAddsClassifiedDependencyWhenProcessEngineLoggingExtensionIsUsed() {
        rewriteRun(
          java(
            """
            import org.operaton.bpm.engine.test.junit5.ProcessEngineLoggingExtension;

            class MyTest {
            }
            """
          ),
          pomXml(
            """
            <project>
                <modelVersion>4.0.0</modelVersion>
                <groupId>com.example</groupId>
                <artifactId>example</artifactId>
                <version>1.0.0</version>
                <dependencies>
                    <dependency>
                        <groupId>org.operaton.bpm</groupId>
                        <artifactId>operaton-bpm-junit5</artifactId>
                        <version>1.0.0</version>
                        <scope>test</scope>
                    </dependency>
                </dependencies>
            </project>
            """,
            """
            <project>
                <modelVersion>4.0.0</modelVersion>
                <groupId>com.example</groupId>
                <artifactId>example</artifactId>
                <version>1.0.0</version>
                <dependencies>
                    <dependency>
                        <groupId>org.operaton.bpm</groupId>
                        <artifactId>operaton-engine</artifactId>
                        <classifier>junit5</classifier>
                        <scope>test</scope>
                    </dependency>
                </dependencies>
            </project>
            """
          )
        );
    }

    @Test
    void removesDependencyAndAddsClassifiedDependencyWhenWatchLoggerIsUsed() {
        rewriteRun(
          java(
            """
            import org.operaton.bpm.engine.test.junit5.WatchLogger;

            class MyTest {
            }
            """
          ),
          pomXml(
            """
            <project>
                <modelVersion>4.0.0</modelVersion>
                <groupId>com.example</groupId>
                <artifactId>example</artifactId>
                <version>1.0.0</version>
                <dependencies>
                    <dependency>
                        <groupId>org.operaton.bpm</groupId>
                        <artifactId>operaton-bpm-junit5</artifactId>
                        <version>1.0.0</version>
                        <scope>test</scope>
                    </dependency>
                </dependencies>
            </project>
            """,
            """
            <project>
                <modelVersion>4.0.0</modelVersion>
                <groupId>com.example</groupId>
                <artifactId>example</artifactId>
                <version>1.0.0</version>
                <dependencies>
                    <dependency>
                        <groupId>org.operaton.bpm</groupId>
                        <artifactId>operaton-engine</artifactId>
                        <classifier>junit5</classifier>
                        <scope>test</scope>
                    </dependency>
                </dependencies>
            </project>
            """
          )
        );
    }

    @Test
    void removesDependencyWithoutAddingWhenClassesNotUsed() {
        rewriteRun(
          pomXml(
            """
            <project>
                <modelVersion>4.0.0</modelVersion>
                <groupId>com.example</groupId>
                <artifactId>example</artifactId>
                <version>1.0.0</version>
                <dependencies>
                    <dependency>
                        <groupId>org.operaton.bpm</groupId>
                        <artifactId>operaton-bpm-junit5</artifactId>
                        <version>1.0.0</version>
                        <scope>test</scope>
                    </dependency>
                    <dependency>
                        <groupId>org.junit.jupiter</groupId>
                        <artifactId>junit-jupiter-api</artifactId>
                        <version>6.0.2</version>
                        <scope>test</scope>
                    </dependency>
                </dependencies>
            </project>
            """,
            """
            <project>
                <modelVersion>4.0.0</modelVersion>
                <groupId>com.example</groupId>
                <artifactId>example</artifactId>
                <version>1.0.0</version>
                <dependencies>
                    <dependency>
                        <groupId>org.junit.jupiter</groupId>
                        <artifactId>junit-jupiter-api</artifactId>
                        <version>6.0.2</version>
                        <scope>test</scope>
                    </dependency>
                </dependencies>
            </project>
            """
          )
        );
    }
}
