package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Restructuring
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-21 */
public final class InitializerEmptyRemove extends RemovingTipper<Initializer>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 0x772C7DD36297FC64L;

  @Override protected boolean prerequisite(final Initializer ¢) {
    final Block $ = ¢.getBody();
    return ¢.getJavadoc() == null && ($ == null || statements($).isEmpty());
  }

  @Override public String description() {
    return "Remove empty initializer";
  }

  @Override public String description(final Initializer ¢) {
    return "Remove empty " + (iz.static¢(¢) ? "" : "non-") + "static initializer";
  }

  @Override public Example[] examples() {
    return new Example[] { //
        convert("class C {{}}") //
            .to("class C {}"), //
        convert("class C {static {}}") //
            .to("class C {}"), //
        Example.ignores("class C {/***/ {}}"), //
        Example.ignores("class C {/***/ static {}}") };
  }
}
