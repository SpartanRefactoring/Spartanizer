package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

/** Same as ReturnTernaryExpander just for "throw"
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2016-12-26 */
public class ThrowTernaryExpander extends ReplaceCurrentNode<ThrowStatement> implements TipperCategory.Expander {
  private static ASTNode innerThrowReplacement(final Expression x, final Statement s) {
    ConditionalExpression ¢;
    if (!(x instanceof ParenthesizedExpression))
      ¢ = az.conditionalExpression(x);
    else {
      final Expression unpar = expression(az.parenthesizedExpression(x));
      if (!(unpar instanceof ConditionalExpression))
        return null;
      ¢ = az.conditionalExpression(unpar);
      if (¢ == null)
        return null;
    }
    final IfStatement $ = s.getAST().newIfStatement();
    $.setExpression(duplicate.of(expression(¢)));
    final ThrowStatement then = ¢.getAST().newThrowStatement();
    then.setExpression(duplicate.of(¢.getThenExpression()));
    $.setThenStatement(duplicate.of(az.statement(then)));
    final ThrowStatement elze = ¢.getAST().newThrowStatement();
    elze.setExpression(duplicate.of(¢.getElseExpression()));
    $.setElseStatement(duplicate.of(az.statement(elze)));
    return $;
  }

  private static ASTNode replaceReturn(final Statement ¢) {
    final ThrowStatement $ = az.throwStatement(¢);
    return $ == null || !(expression($) instanceof ConditionalExpression) && !(expression($) instanceof ParenthesizedExpression) ? null
        : innerThrowReplacement(expression($), ¢);
  }

  @Override public ASTNode replacement(final ThrowStatement ¢) {
    return replaceReturn(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final ThrowStatement __) {
    return "expanding a ternary operator to a full if-else statement";
  }
}
