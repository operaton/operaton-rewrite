package org.operaton.rewrite.upgrade;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

class OperatonNoopRecipeTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new OperatonNoopRecipe());
    }

    @Test
    void noChanges() {
        rewriteRun(
          java(
            """
            package org.operaton.example;

            class Example {
            }
            """
          )
        );
    }
}