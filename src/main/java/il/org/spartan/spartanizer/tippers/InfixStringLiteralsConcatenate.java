package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** concate same line string literal
 * @author Doron Mehsulam {@code doronmmm@hotmail.com}
 * @author Niv Shalmon {@code shalmon.niv@gmail.com}
 * @since 2017-03-22 */
public class InfixStringLiteralsConcatenate extends ReplaceCurrentNode<InfixExpression> //
    implements TipperCategory.NOP {
  private static final long serialVersionUID = -4282740939895750794L;

  @Override public ASTNode replacement(@NotNull final InfixExpression x) {
    @Nullable final List<Expression> es = hop.operands(x);
    Expression prev = copy.of(lisp.first(es));
    @Nullable final CompilationUnit u = az.compilationUnit(x.getRoot());
    @NotNull final List<Expression> es2 = new LinkedList<>();
    for (@NotNull final Expression e : lisp.rest(es))
      if (u.getLineNumber(prev.getStartPosition()) != u.getLineNumber(e.getStartPosition()) || !iz.stringLiteral(prev) || !iz.stringLiteral(e)) {
        es2.add(prev);
        prev = copy.of(e);
      } else {
        @Nullable final StringLiteral l = az.stringLiteral(prev);
        l.setLiteralValue(l.getLiteralValue() + az.stringLiteral(e).getLiteralValue());
      }
    es2.add(prev);
    if (es2.size() >= 2)
      return subject.operands(es2).to(wizard.PLUS2);
    final StringLiteral $ = x.getAST().newStringLiteral();
    $.setLiteralValue(az.stringLiteral(lisp.first(es2)).getLiteralValue());
    return $;
  }

  @Override protected boolean prerequisite(@NotNull final InfixExpression x) {
    if (operator(x) != wizard.PLUS2)
      return false;
    @Nullable final List<Expression> es = hop.operands(x);
    Expression prev = lisp.first(es);
    if (!iz.compilationUnit(x.getRoot()))
      return false;
    @Nullable final CompilationUnit u = az.compilationUnit(x.getRoot());
    for (@NotNull final Expression ¢ : lisp.rest(es)) {
      if (u.getLineNumber(prev.getStartPosition()) == u.getLineNumber(¢.getStartPosition()) && iz.stringLiteral(prev) && iz.stringLiteral(¢))
        return true;
      prev = ¢;
    }
    return false;
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "concate same line string literal";
  }
}
