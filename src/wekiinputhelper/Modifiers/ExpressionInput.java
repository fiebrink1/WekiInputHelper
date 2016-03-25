/*
 * Jut works for inputs right now.
 */
package wekiinputhelper.Modifiers;

import expr.Expr;
import expr.Parser;
import expr.Variable;
import java.util.HashMap;
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
public class ExpressionInput implements ModifiedInputSingle, UsesOnlyOriginalInputs {

    private final String name;
    private final Expr expression;
    private transient double[] lastInputs;
    private final Variable[] variables;
    private final IndexedVar[] indexedVariables;
    private final int[] indexes;
    private final transient double[] lastOutput;
    private final String originalString;
    private final CircularBuffer[] buffersForInputs;

    //Requires isValid
    public ExpressionInput(Expr expr, String name, String expressionString, WekiInputHelper w) {
        this.name = name;
        this.expression = expr;
        buffersForInputs = new CircularBuffer[w.getInputManager().getNumInputs()];
        HashSet<String> vars = expr.getVariablesUsed();
        variables = new Variable[vars.size()];
        indexedVariables = new IndexedVar[vars.size()];
        indexes = new int[vars.size()];
        int i = 0;

        HashMap<Integer, Integer> maxDelayLengthsForInputs = new HashMap<>();

        for (String v : vars) {
            variables[i] = Variable.make(v);
            if (isIndexed(v)) {
                indexedVariables[i] = new IndexedVar(v);
                indexes[i] = findIndex(indexedVariables[i].unindexedName, w.getInputManager().getInputNames());

                if (maxDelayLengthsForInputs.get(indexes[i]) != null) {
                    int currDelay = maxDelayLengthsForInputs.get(indexes[i]);
                    if (indexedVariables[i].delay > currDelay) {
                        maxDelayLengthsForInputs.put(indexes[i], indexedVariables[i].delay);
                    }
                } else {
                    maxDelayLengthsForInputs.put(indexes[i], indexedVariables[i].delay);
                }
            } else {
                indexes[i] = findIndex(v, w.getInputManager().getInputNames());

                indexedVariables[i] = IndexedVar.makeIndexedVariableWithoutDelay(v);
            }
            i++;
        }

        for (int id = 0; id < w.getInputManager().getNumInputs(); id++) {
            if (maxDelayLengthsForInputs.containsKey(id)) { // was i
                buffersForInputs[id] = new CircularBuffer(maxDelayLengthsForInputs.get(id) + 1);
            } else {
                buffersForInputs[id] = new CircularBuffer(1);
            }
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
        indexedVariables = new IndexedVar[vars.size()];

        indexes = new int[vars.size()];
        int i = 0;
        HashMap<Integer, Integer> maxDelayLengthsForInputs = new HashMap<>();

        for (String v : vars) {
            variables[i] = Variable.make(v);
            // indexes[i] = findIndex(v, inputNames);
            if (isIndexed(v)) {
                indexedVariables[i] = new IndexedVar(v);
                indexes[i] = findIndex(indexedVariables[i].unindexedName, inputNames);

                if (maxDelayLengthsForInputs.get(indexes[i]) != null) {
                    int currDelay = maxDelayLengthsForInputs.get(indexes[i]);
                    if (indexedVariables[i].delay > currDelay) {
                        maxDelayLengthsForInputs.put(indexes[i], indexedVariables[i].delay);
                    }
                } else {
                    maxDelayLengthsForInputs.put(indexes[i], indexedVariables[i].delay);
                }
            } else {
                indexes[i] = findIndex(v, inputNames);
                indexedVariables[i] = IndexedVar.makeIndexedVariableWithoutDelay(v);
            }
            i++;
        }

        buffersForInputs = new CircularBuffer[inputNames.length];

        for (int id = 0; id < inputNames.length; id++) {
            if (maxDelayLengthsForInputs.containsKey(id)) {
                buffersForInputs[id] = new CircularBuffer(maxDelayLengthsForInputs.get(id) + 1);
            } else {
                buffersForInputs[id] = new CircularBuffer(1);
            }
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
        /*if (w.getOutputManager().hasValidOutputGroup()) {
         outputNames = w.getOutputManager().getOutputNames();
         } else {
         outputNames = new String[0];
         } */

        for (String var : vars) {
            boolean isIndexed = isIndexed(var);
            IndexedVar v;

            if (isIndexed) {
                try {
                    v = new IndexedVar(var);
                } catch (IllegalArgumentException ex) {
                    Util.showPrettyErrorPane(null, "Improperly formatted indexed variable: Must be in form such as inputName[n-3]");
                    return false;
                }

                if (findIndex(v.unindexedName, inputNames) == -1) {
                    Util.showPrettyErrorPane(null, "Variable " + var + " is not a valid input name");
                    return false;
                }
            } else {
                if (findIndex(var, inputNames) == -1) {
                    Util.showPrettyErrorPane(null, "Variable " + var + " is not a valid input name");
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isIndexed(String var) {
        return (var.contains("["));
    }

    private static int findIndex(String s, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (s.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void updateForInputs(double[] inputs) {
        //lastInputs = inputs;
        for (int i = 0; i < inputs.length; i++) {
            buffersForInputs[i].addNext(inputs[i]);
        }
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public double getValue() {
        for (int i = 0; i < variables.length; i++) {
            // variables[i].setValue(lastInputs[indexes[i]]);
            int delay = indexedVariables[i].delay;
            int x = indexes[i];
           // System.out.println("X=" + x);
            double val = buffersForInputs[x].getDelayedSample(delay); //ERROR HERE
            variables[i].setValue(val);
        }
        return expression.value();
    }

    public static void main(String[] args) {
        try {
            Expr exp = Parser.parse("a_1[n] + a_1[n-1] - a_1[n-2]");
            String[] s = new String[]{"a_1", "b", "c"};
            ExpressionInput in = new ExpressionInput(exp, "newname", "tmp", s);

            in.updateForInputs(new double[]{5, 1, 5});
            double d = in.getValue();
            System.out.println("val is " + d);

            in.updateForInputs(new double[]{10, 1, 5});

            d = in.getValue();
            System.out.println("val is " + d);

            in.updateForInputs(new double[]{15, 1, 5});

            d = in.getValue();
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
