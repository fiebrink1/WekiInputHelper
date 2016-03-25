/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.Modifiers;

import java.util.ArrayList;
import java.util.List;
import wekiinputhelper.Criterion;
import wekiinputhelper.Criterion.CriterionType;
import wekiinputhelper.UsesOnlyOriginalInputs;
import wekiinputhelper.WekiInputHelper;
import wekiinputhelper.gui.InputModifierBuilderPanel;

/**
 *
 * @author rebecca
 */
public class ConditionalBufferedInput implements ModifiedInputVector, UsesOnlyOriginalInputs {

    private final String[] names;
    private final int index;
    private final int bufferSize;
    private transient final List<Double> history;
    private final Criterion startCriterion;
    private final Criterion stopCriterion;
    private transient double[] returnValues;
    private transient boolean isRecording = false;

    public Criterion getStartCriterion() {
        return startCriterion;
    }

    public Criterion getStopCriterion() {
        return stopCriterion;
    }

    public int getIndex() {
        return index;
    }

    public ConditionalBufferedInput(String originalName, int index, int bufferSize, Criterion c1, Criterion c2, int increment) {
        names = new String[bufferSize];

        if (increment == 1) {
            names[bufferSize - 1] = originalName + "[n]";
            for (int i = 2; i <= bufferSize; i++) {
                names[bufferSize - i] = originalName + "[n-" + (i - 1) + "]";
            }
        } else {
            names[bufferSize - 1] = originalName + "[n](" + increment + ")";
            for (int i = 2; i <= bufferSize; i++) {
                names[bufferSize - i] = originalName + "[n-" + (i - 1) + "](" + increment + ")";
            }
        }

        this.index = index;
        this.bufferSize = bufferSize;
        history = new ArrayList<>();
        returnValues = new double[bufferSize];
        this.startCriterion = c1;
        this.stopCriterion = c2;
    }

    @Override
    public String[] getNames() {
        return names;
    }

    @Override
    public void updateForInputs(double[] inputs) {
        if (isRecording) {
            boolean done = checkIfStop(inputs);
            if (done) {
                updateOutputBuffer();
                isRecording = false;
                System.out.println("Stopped recording for" + inputs[0]);

            } else {
                history.add(inputs[index]);
            }
        } else {
            isRecording = checkIfStart(inputs);
            if (isRecording) {
                history.add(inputs[index]);
                System.out.println("Started recording for" + inputs[0]);
            }
        }
    }

    private boolean checkIfStop(double[] inputs) {
        return stopCriterion.shouldTrigger(inputs, null);
    }

    private boolean checkIfStart(double[] inputs) {
        return startCriterion.shouldTrigger(inputs, null);
    }

    private void updateOutputBuffer() {
        //need to put list into array of size bufferSize
        if (history.size() == bufferSize) {
            int i = 0;
            for (Double d : history) {
                returnValues[i++] = d;
            }
        } else if (history.size() > bufferSize) {
            //VERY hacky way: not even smoothing!
            int hopSize = (int) (history.size() / bufferSize);
            for (int i = 0; i < bufferSize; i++) {
                returnValues[i] = history.get(i * hopSize);
            }
        } else {
            //VERY Hacky
            int numRepeats = (int) (bufferSize / history.size());
            for (int i = 0; i < bufferSize; i++) {
                if (i / numRepeats < history.size()) {
                    returnValues[i] = history.get(i / numRepeats);
                } else {
                    returnValues[i] = history.get(history.size() - 1);
                }
            }
        }

        history.clear();
    }

    @Override
    public int getSize() {
        return bufferSize;
    }

    @Override
    public double[] getValues() {
        return returnValues;
    }

    public static void main(String[] args) {
        Criterion start = new Criterion(CriterionType.GREATER_THAN, Criterion.HowLong.REPEAT, 0, Criterion.AppliesTo.INPUT, 0);
        Criterion stop = new Criterion(CriterionType.LESS_OR_EQUAL, Criterion.HowLong.REPEAT, 0, Criterion.AppliesTo.INPUT, 0);

        ConditionalBufferedInput bi = new ConditionalBufferedInput("f1", 0, 2, start, stop, 1);

        for (int i = -5; i < 2; i++) {
            System.out.print(i + ": ");
            bi.updateForInputs(new double[]{i});
            double[] d = bi.getValues();
            for (int j = 0; j < bi.getSize(); j++) {
                System.out.print(d[j] + " ");
            }
            System.out.println("");
        }
        for (int i = 5; i > -4; i--) {
            System.out.print(i + ": ");
            bi.updateForInputs(new double[]{i});
            double[] d = bi.getValues();
            for (int j = 0; j < bi.getSize(); j++) {
                System.out.print(d[j] + " ");
            }
            System.out.println("");
        }
    }

    @Override
    public InputModifierBuilderPanel getBuildPanel(WekiInputHelper w) {
        return new ConditionalBufferInputEditor(w, this);
    }
}
