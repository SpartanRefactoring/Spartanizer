package org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PostfixExpression.Operator;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.spartan.refactoring.utils.Have;
import org.spartan.refactoring.utils.Subject;

/**
 * A {@link Wring} that converts, whenever possible, postfix increment/decrement
 * to prefix increment/decrement
 *
 * @author Yossi Gil
 * @since 2015-7-17
 */
public final class PostfixToPrefix extends Wring.Replacing<PostfixExpression> {
  @Override boolean scopeIncludes(@SuppressWarnings("unused") final PostfixExpression _) {
    return true;
  }
  @Override PrefixExpression replacement(final PostfixExpression e) {
    return Subject.operand(e.getOperand()).to(pre2post(e.getOperator()));
  }
  private static PrefixExpression.Operator pre2post(final PostfixExpression.Operator o) {
    return o == PostfixExpression.Operator.DECREMENT ? PrefixExpression.Operator.DECREMENT : PrefixExpression.Operator.INCREMENT;
  }
  @Override protected boolean eligible(final PostfixExpression e) {
    return !(e.getParent() instanceof Expression || Have.ancestorOf(ASTNode.VARIABLE_DECLARATION_STATEMENT, e));
  }
  @Override String description(final PostfixExpression e) {
    return "Convert post-" + description(e.getOperator()) + " of " + e.getOperand() + " to pre-" + description(e.getOperator());
  }
  private static String description(final Operator o) {
    return o == PostfixExpression.Operator.DECREMENT ? "decrement" : "increment";
  }
}
