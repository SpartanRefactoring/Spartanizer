package il.org.spartan.athenizer;

import static il.org.spartan.spartanizer.plugin.Eclipse.openedTextEditors;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import fluent.ly.English;
import fluent.ly.as;
import fluent.ly.the;
import il.org.spartan.athenizer.SingleFlater.Operation;
import il.org.spartan.athenizer.collateral.Augmenter;
import il.org.spartan.spartanizer.plugin.Applicator;
import il.org.spartan.spartanizer.plugin.Eclipse;
import il.org.spartan.spartanizer.plugin.GUIApplicator;
import il.org.spartan.spartanizer.plugin.Selection;
import il.org.spartan.spartanizer.plugin.SpartanizationHandler;
import il.org.spartan.spartanizer.plugin.WrappedCompilationUnit;
import il.org.spartan.utils.Bool;

/** Handler for the Bloater project's feature (global Bloater). Uses
 * {@link BloaterGUIApplicator} as an {@link Applicator} and {@link Augmenter}
 * as an {@link Application}.
 * @author Ori Roth
 * @since Nov 25, 2016 */
public class InflateHandler extends AbstractHandler {
  private static final String TOGGLE_ACTIVITY = "il.org.spartan.AthensToggle";
  private static final English.Inflection OPERATION_ACTIVITY = English.Inflection.stem("Athenize");
  public static final Bool active = new Bool();
  private static final IPartListener pageListener = pageListener();

  @Override public Object execute(final ExecutionEvent ¢) throws ExecutionException {
    if (TOGGLE_ACTIVITY.equals(¢.getCommand().getId())) {
      HandlerUtil.toggleCommandState(¢.getCommand());
      return goWheelAction();
    }
    final Selection $ = Selection.Util.current().setUseBinding();
    return $.isTextSelection ? doSingle() : goAggressiveAction($);
  }
  
  public static Void doSingle() {
    final WrappedCompilationUnit wcu = the.firstOf(Selection.Util.current().inner).build();
    SingleFlater.commitChanges(
        SingleFlater.in(wcu.compilationUnit).from(new InflaterProvider()).limit(Selection.Util.current().textSelection),
        ASTRewrite.create(wcu.compilationUnit.getAST()), wcu, null, null, null, false);
    return null;
  }
  
  public static Void goWheelAction() {
    final IPartService s = getPartService();
    if (s == null)
      return null;
    if (active.get()) {
      active.clear();
      removePageListener(s);
    } else {
      active.set();
      openedTextEditors().forEach(InflateHandler::addListener);
      s.addPartListener(pageListener);
    }
    return null;
  }
  public static Void goAggressiveAction(final Selection ¢) {
    applicator().selection(¢).setPasses(SpartanizationHandler.PASSES).go();
    return null;
  }
  private static List<Listener> getListeners(final StyledText t) {
    final List<Listener> $ = an.empty.list();
    if (t == null)
      return $;
    final List<Listener> ls = as.list(t.getListeners(SWT.KeyDown));
    if (ls == null)
      return $;
    $.addAll(
        ls.stream().filter(λ -> λ instanceof TypedListener && ((TypedListener) λ).getEventListener() instanceof InflaterListener).collect(toList()));
    return $;
  }
  private static StyledText getText(final ITextEditor ¢) {
    if (¢ == null)
      return null;
    final Control $ = ¢.getAdapter(Control.class);
    return !($ instanceof StyledText) ? null : (StyledText) $;
  }
  public static GUIApplicator applicator() {
    return (GUIApplicator) SpartanizationHandler.applicator(OPERATION_ACTIVITY).setRunAction(
        ¢ -> Integer.valueOf(as.bit(SingleFlater.commitChanges(SingleFlater.in(¢.buildWithBinding().compilationUnit).from(new InflaterProvider() {
          @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
            return λ -> λ;
          }
        }), ASTRewrite.create(¢.compilationUnit.getAST()), ¢, null, null, null, false)))).name(OPERATION_ACTIVITY.getIng())
        .operationName(OPERATION_ACTIVITY);
  }
  // TODO Roth: multiple windows support
  private static IPartService getPartService() {
    final IWorkbench w = PlatformUI.getWorkbench();
    if (w == null)
      return null;
    final IWorkbenchWindow $ = w.getActiveWorkbenchWindow();
    final IWorkbenchWindow[] wds = w.getWorkbenchWindows();
    return $ != null ? $.getPartService() : wds != null && wds.length != 0 ? wds[0].getPartService() : null;
  }
  @SuppressWarnings("unused") private static IPartListener pageListener() {
    return new IPartListener() {
      @Override public void partActivated(final IWorkbenchPart __) {
        //
      }
      @Override public void partBroughtToTop(final IWorkbenchPart __) {
        //
      }
      @Override public void partClosed(final IWorkbenchPart __) {
        //
      }
      @Override public void partDeactivated(final IWorkbenchPart __) {
        //
      }
      @Override public void partOpened(final IWorkbenchPart ¢) {
        addListener(¢);
      }
    };
  }
  private static void removePageListener(final IPartService ¢) {
    ¢.removePartListener(pageListener);
    openedTextEditors().forEach(InflateHandler::removeListener);
  }
  static void addListener(final IWorkbenchPart ¢) {
    if (¢ instanceof ITextEditor)
      addListener((ITextEditor) ¢);
  }
  @SuppressWarnings("unused") private static void addListener(final ITextEditor ¢) {
    final StyledText text = getText(¢);
    if (text == null)
      return;
    final IEditorInput i = ¢.getEditorInput();
    if (!(i instanceof FileEditorInput))
      return;
    final IFile f = ((IFileEditorInput) i).getFile();
    if (f != null && "java".equals(f.getFileExtension()))
      Eclipse.runAsynchronouslyInUIThread(() -> {
        final InflaterListener l = new InflaterListener(text, ¢, Selection.of(JavaCore.createCompilationUnitFrom(f)).setUseBinding());
        text.getDisplay().addFilter(SWT.MouseWheel, l);
        text.getDisplay().addFilter(SWT.KeyDown, l);
        text.getDisplay().addFilter(SWT.KeyUp, l);
        text.addKeyListener(l);
        text.addDisposeListener(__ -> {
          text.getDisplay().removeFilter(SWT.MouseWheel, l);
          text.getDisplay().removeFilter(SWT.KeyDown, l);
          text.getDisplay().removeFilter(SWT.KeyUp, l);
          text.removeKeyListener(l);
        });
        text.addFocusListener(new FocusListener() {
          @Override public void focusLost(final FocusEvent __) {
            l.finilize();
          }
          @Override public void focusGained(final FocusEvent __) {/**/}
        });
      });
  }
  private static void removeListener(final ITextEditor e) {
    final StyledText text = getText(e);
    if (text == null)
      return;
    final List<Listener> ls = getListeners(text);
    ls.stream().filter(λ -> λ instanceof TypedListener && ((TypedListener) λ).getEventListener() instanceof InflaterListener).findFirst()
        .ifPresent(λ -> ((InflaterListener) ((TypedListener) λ).getEventListener()).finilize());
    ls.forEach(λ -> text.getDisplay().removeFilter(SWT.MouseWheel, (Listener) ((TypedListener) λ).getEventListener()));
    ls.forEach(λ -> text.getDisplay().removeFilter(SWT.KeyDown, (Listener) ((TypedListener) λ).getEventListener()));
    ls.forEach(λ -> text.getDisplay().removeFilter(SWT.KeyUp, (Listener) ((TypedListener) λ).getEventListener()));
    ls.forEach(λ -> text.removeKeyListener((KeyListener) ((TypedListener) λ).getEventListener()));
  }
}
