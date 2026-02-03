package org.operaton.rewrite;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;

public class OperatonNoopRecipe extends Recipe {
    @Override
    public String getDisplayName() {
        return "Operaton placeholder recipe";
    }

    @Override
    public String getDescription() {
        return "Base placeholder recipe used to validate Operaton recipe wiring.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return TreeVisitor.noop();
    }
}