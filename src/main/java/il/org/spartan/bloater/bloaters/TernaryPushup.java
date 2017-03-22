package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** convert {@code
 * a + (cond ? b : c)
} to * {@code
 * cond ? a + b : a + c
 *
 * } Test case is {@link Issue1049}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-18 */
public class TernaryPushup extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = -3885811005387698058L;

  @Override public ASTNode replacement(final InfixExpression x) {
    @Nullable final Expression l = extract.core(left(x)), r = extract.core(right(x));
    if (iz.conditionalExpression(r)) {
      @Nullable final ConditionalExpression $ = az.conditionalExpression(r);
      return subject.pair(subject.pair(l, then($)).to(operator(x)), subject.pair(l, elze($)).to(operator(x))).toCondition(expression($));
    }
    @Nullable final ConditionalExpression ll = az.conditionalExpression(l);
    return subject.pair(subject.pair(then(ll), r).to(operator(x)), subject.pair(elze(ll), r).to(operator(x))).toCondition(expression(ll));
  }

  @Override protected boolean prerequisite(@Nullable final InfixExpression x) {
    if (x == null)
      return false;
    @Nullable final Expression $ = extract.core(left(x)), r = extract.core(right(x));
    return iz.conditionalExpression(r) && !haz.sideEffects(expression(az.conditionalExpression(r)))
        || iz.conditionalExpression($) && !haz.sideEffects(expression(az.conditionalExpression($)));
  }

  @NotNull @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "";
  }
}
