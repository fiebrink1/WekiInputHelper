/*
 * Jut works for inputs right now.
 */
package wekiinputhelper.Modifiers;

import expr.Expr;
import expr.Parser;
import expr.SyntaxException;
import expr.Variable;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import wekiinputhelper.UsesOnlyOriginalInputs;
import wekiinputhelper.WekiInputHelper;
import wekiinputhelper.gui.InputModifierBuilderPanel;
import wekiinputhelper.util.Util;

/**
 *
 * @author rebecca
 */
public class ExpressionInput implements ModifiedInputSingle, UsesOnlyOriginalInputs{
    private final String name;
    private final Expr expression;
    private transient double[] lastInputs;
    private final Variable[] variables;
    private final int[] indexes;
    private final transient double[] lastOutput;
    private final String originalString;
    
    //Requires isValid
    public ExpressionInput(Expr expr, String name, String expressionString, WekiInputHelper w) {
        this.name = name;
        this.expression = expr;
        HashSet<String> vars = expr.getVariablesUsed();
        variables = new Variable[vars.size()];
        indexes = new int[vars.size()];
        int i = 0;
        for (String v : vars) {
            variables[i] = Variable.make(v);
            indexes[i] = findIndex(v, w.getInputManager().getInputNames());
            i++;
        }
        lastOutput = new double[1];
        this.originalString = expressionString;
        expr.toString();
    }

    public String getOriginalString() {
        return originalString;
    }
    
    
    
    //For testing
    private ExpressionInput(Expr expr, String name, String s, String[] inputNames) {
        this.name = name;
        this.expression = expr;
        HashSet<String> vars = expr.getVariablesUsed();
        variables = new Variable[vars.size()];
        indexes = new int[vars.size()];
        int i = 0;
        for (String v : vars) {
            variables[i] = Variable.make(v);
            indexes[i] = findIndex(v, inputNames);
            i++;
        }
        lastOutput = new double[1];
        this.originalString = s;
    }
    
    
    public static boolean isValid(Expr expr, WekiInputHelper w) {
        HashSet<String> vars = expr.getVariablesUsed();
        String[] inputNames;
        String[] outputNames;
        if (w.getInputManager().hasValidInputs()) {
            inputNames = w.getInputManager().getInputNames();
        } else {
            inputNames = new String[0];
        }
        if (w.getOutputManager().hasValidOutputGroup()) {
            outputNames = w.getOutputManager().getOutputNames();
        } else {
            outputNames = new String[0];
        }
            
        for (String var : vars) {
            if (findIndex(var, inputNames) == -1) {
                Util.showPrettyErrorPane(null, "Variable " + var + " is not a valid input name");
                return false;
            }
        }
        return true;
    }
    
    private static int findIndex(String s, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (s.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }
    
    public String getName() {
        return name;
    }

    @Override
    public void updateForInputs(double[] inputs) {
        lastInputs = inputs;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public double getValue() {
        for (int i = 0; i < variables.length; i++) {
            variables[i].setValue(lastInputs[indexes[i]]);
        }
        return expression.value();
    }
    
    public static void main(String[] args) {
        try {
            Expr exp = Parser.parse("a + b - c");
            String[] s = new String[] {"b", "c", "a"};
            ExpressionInput in = new ExpressionInput(exp, "newname", "tmp",s);
            
            in.updateForInputs(new double[] {2, 1, 5});
            double d= in.getValue();
            System.out.println("val is " + d);
            
        } catch (Exception ex) {
            Logger.getLogger(ExpressionInput.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public InputModifierBuilderPanel getBuildPanel(WekiInputHelper w) {
        return new ExpressionInputEditor(w, this);
    }
}
