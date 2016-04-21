package il.org.spartan.refactoring.application;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;

import il.org.spartan.files.FilesGenerator;
import il.org.spartan.refactoring.utils.As;
import il.org.spartan.refactoring.utils.Funcs;
import il.org.spartan.utils.FileUtils;

public class TypeNamesCollector {
  private static Set<String> basket = new TreeSet<>();

  public static void main(final String[] where) {
    collect(where.length != 0 ? where : new String[] { "." });
    for (final String s : basket)
      System.out.println(s + " --> " + Funcs.shortName(s));
  }
  private static void collect(final String[] where) {
    for (final File f : new FilesGenerator(".java").from(where))
      collect(f);
  }
  private static void collect(final File f) {
    try {
      collect(FileUtils.read(f));
    } catch (final IOException e) {
      System.err.println(e.getMessage());
    }
  }
  private static void collect(final String javaCode) {
    collect((CompilationUnit) As.COMPILIATION_UNIT.ast(javaCode));
  }
  private static void collect(final CompilationUnit u) {
    u.accept(new ASTVisitor() {
      @SuppressWarnings("synthetic-access") @Override public boolean visit(final SimpleType t) {
        basket.add(last(t.getName()).toString());
        return true;
      }
      private SimpleName last(final Name n) {
        return n.isSimpleName() ? (SimpleName) n //
            : n.isQualifiedName() ? ((QualifiedName) n).getName() //
                : null;
      }
    });
  }
}
