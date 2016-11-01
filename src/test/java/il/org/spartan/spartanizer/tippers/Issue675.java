package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** @author Aviad Cohen & Noam Yefet
 *  @since 16-11-1
 */
public class Issue675 {
  @Test public void statements_test0() {
    enumerate.statements(null);
    assert true;
  }
  
  @Test public void statements_test1() {
    assertEquals(enumerate.statements(null), 0);
  }
  
  @Test public void statements_test2() {
    assertEquals(enumerate.statements(wizard.ast("return 0;")), 1);
  }
}
