package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** Find if(X == null) return null; <br>
 * Find if(null == X) return null; <br>
 * @author Ori Marcovitch
 * @year 2016 */
public class AssertNotNull extends NanoPatternTipper<IfStatement> {
  private static final String description = "replace with azzert.notNull($X)";
  private static final PreconditionNotNull rival = new PreconditionNotNull();
  private static final UserDefinedTipper<Expression> expression = patternTipper("$X == null", "", "");
  private static final UserDefinedTipper<Expression> infix = patternTipper("$X1 == null || $X2", "", "");
  private static final Set<UserDefinedTipper<Statement>> statements = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("return;", "", ""));
      add(patternTipper("return null;", "", ""));
    }
  };

  @Override public boolean canTip(final IfStatement ¢) {
    return nullCheck(expression(¢))//
        && returns(¢) //
        && rival.cantTip(¢)//
    ;
  }

  private static boolean returns(final IfStatement ¢) {
    return anyTips(statements, then(¢));
  }

  protected static boolean nullCheck(final Expression ¢) {
    return expression.canTip(¢) || infix.canTip(¢) && nullCheck(right(az.infixExpression(¢)));
  }

  static List<String> checkees(final IfStatement ¢) {
    Expression e = expression(¢);
    final List<String> $ = new ArrayList<>();
    while (infix.canTip(e)) {
      $.add(left(az.infixExpression(left(az.infixExpression(e)))) + "");
      e = right(az.infixExpression(e));
    }
    $.add(left(az.infixExpression(e)) + "");
    return $;
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(¢, extract.singleStatement(ast("azzert.notNull(" + separate.these(checkees(¢)).by(",") + ");")), g);
      }
    };
  }

  @Override public Category category() {
    return Category.Return;
  }

  @Override public String description() {
    return description;
  }

  @Override public String[] technicalName() {
    return new String[] { "if($X == null) return;", "if($X == null) return null;", "if($X1 == null || $X2 == null) return;" };
  }
}
