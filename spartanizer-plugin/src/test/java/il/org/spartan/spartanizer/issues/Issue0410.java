package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.is;
import static il.org.spartan.spartanizer.ast.navigate.GuessedContext.EXPRESSION_LOOK_ALIKE;
import static il.org.spartan.spartanizer.ast.navigate.GuessedContext.find;

import org.junit.Ignore;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.navigate.GuessedContext;
import il.org.spartan.spartanizer.engine.nominal.Trivia;

/** Test class for {@link GuessedContext} .
 * @since 2016 */
@Ignore
@SuppressWarnings("static-method")
public class Issue0410 {
  @Test public void dealWithBothKindsOfComment() {
    similar("if (b) {\n", "if (b) {;} { throw new Exception(); }");
  }
  @Test public void findVariable() {
    azzert.that(find("i"), is(EXPRESSION_LOOK_ALIKE));
  }
  @Test public void removeCommentsTest() {
    similar(Trivia.removeComments("if (b) {\n"), "if (b) {} else { throw new Exception(); }");
  }
  private void similar(final String s1, final String s2) {
    azzert.that(Trivia.essence(s2), is(Trivia.essence(s1)));
  }
}
