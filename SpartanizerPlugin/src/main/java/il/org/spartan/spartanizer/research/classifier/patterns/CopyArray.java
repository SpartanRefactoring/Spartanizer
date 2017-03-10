package il.org.spartan.spartanizer.research.classifier.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class CopyArray extends NanoPatternTipper<ForStatement> {
  private static final long serialVersionUID = -6362859252282909862L;
  private final Collection<UserDefinedTipper<ForStatement>> tippers = new HashSet<UserDefinedTipper<ForStatement>>() {
    @SuppressWarnings("hiding") static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("for (int $N0 = 0; $N0 < $N1; ++$N0)  $N2[$N0] = $N3[$N0];", "copy();", "copy"));
    }
  };

  @Override public boolean interesting(final ForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "Init array: conevrt to fluent API";
  }

  @Override public Tip pattern(final ForStatement ¢) {
    return firstTip(tippers, ¢);
  }
}
