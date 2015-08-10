/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.gui;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import wekiinputhelper.Modifiers.AverageWindowOperation;
import wekiinputhelper.Modifiers.BufferedInput;
import wekiinputhelper.Modifiers.BufferedInputEditor;
import wekiinputhelper.Modifiers.ConditionalBufferInputEditor;
import wekiinputhelper.Modifiers.ConditionalBufferedInput;
import wekiinputhelper.Modifiers.ExpressionInput;
import wekiinputhelper.Modifiers.ExpressionInputEditor;
import wekiinputhelper.Modifiers.FirstOrderDifference;
import wekiinputhelper.Modifiers.FirstOrderDifferenceEditor;
import wekiinputhelper.Modifiers.MinWindowOperation;
import wekiinputhelper.Modifiers.Min1stWindowOperation;
import wekiinputhelper.Modifiers.Min2ndWindowOperation;
import wekiinputhelper.Modifiers.Max1stWindowOperation;
import wekiinputhelper.Modifiers.Max2ndWindowOperation;
import wekiinputhelper.Modifiers.MaxWindowOperation;
import wekiinputhelper.Modifiers.ModifiedInput;
import wekiinputhelper.Modifiers.SecondOrderDifference;
import wekiinputhelper.Modifiers.SecondOrderDifferenceEditor;
import wekiinputhelper.Modifiers.StdDevWindowOperation;
import wekiinputhelper.Modifiers.WindowedOperation;
import wekiinputhelper.Modifiers.WindowedOperation.Operation;
import wekiinputhelper.Modifiers.WindowedOperationEditor;
import wekiinputhelper.WekiInputHelper;

/**
 *
 * @author rebecca
 */
public class ModifierConfigRow extends InputModifierBuilderPanel {
    private final WekiInputHelper w;
    /**
     * Creates new form ModifierConfigRow
     */
    public static int BUFFERED_INDEX = 0;
    public static int FIRST_ORDER_INDEX = 1;
    public static int SECOND_ORDER_INDEX = 2;
    public static int AVERAGE_INDEX = 3;
    public static int STDEV_INDEX = 4;
    public static int MIN_INDEX = 5;
    public static int MAX_INDEX = 6;
    public static int MIN_FIRST_INDEX = 7;
    public static int MAX_FIRST_INDEX = 8;
    public static int MIN_SECOND_INDEX = 9;
    public static int MAX_SECOND_INDEX = 10;
    public static int BUFFERED_CONDITION_INDEX= 11;
    public static int EXPRESSION_INDEX = 12;
    private static final Logger logger = Logger.getLogger(ModifierConfigRow.class.getName());
    
    public ModifierConfigRow() {
        initComponents();
         w = null;
    }
    
    public ModifierConfigRow(WekiInputHelper w) {
        initComponents();
        this.w = w;
        chooseFirstCard();
    }
    
    public ModifierConfigRow(WekiInputHelper w, ModifiedInput o) {
        initComponents();
        this.w = w;
        initFromExisting(o);
    }
    
    private void initFromExisting(ModifiedInput o) {

        if (o instanceof BufferedInput) {
            comboModifierType.setSelectedIndex(BUFFERED_INDEX);
        } else if (o instanceof FirstOrderDifference) {
            comboModifierType.setSelectedIndex(FIRST_ORDER_INDEX);
        } else if (o instanceof SecondOrderDifference) {
            comboModifierType.setSelectedIndex(SECOND_ORDER_INDEX);
        } else if (o instanceof ConditionalBufferedInput) {
                comboModifierType.setSelectedIndex(BUFFERED_CONDITION_INDEX);
        }  else if (o instanceof ExpressionInput) {
                comboModifierType.setSelectedIndex(EXPRESSION_INDEX);
        } else if (o instanceof WindowedOperation) {
            Operation op = ((WindowedOperation)o).getOp();
            if (op instanceof AverageWindowOperation) {
                comboModifierType.setSelectedIndex(AVERAGE_INDEX);
            } else if (op instanceof StdDevWindowOperation) {
                comboModifierType.setSelectedIndex(STDEV_INDEX);
            } else if (op instanceof MinWindowOperation) {
                comboModifierType.setSelectedIndex(MIN_INDEX);
            } else if (op instanceof MaxWindowOperation) {
                comboModifierType.setSelectedIndex(MAX_INDEX);
            } else if (op instanceof Min1stWindowOperation) {
                comboModifierType.setSelectedIndex(MIN_FIRST_INDEX);
            } else if (op instanceof Max1stWindowOperation) {
                comboModifierType.setSelectedIndex(MAX_FIRST_INDEX);
            } else if (op instanceof Min2ndWindowOperation) {
                comboModifierType.setSelectedIndex(MIN_SECOND_INDEX);
            }else if (op instanceof Max2ndWindowOperation) {
                comboModifierType.setSelectedIndex(MAX_SECOND_INDEX);
            } else {
                logger.log(Level.WARNING, "Unknown op type {0}", op.shortName());
            }
        } else {
            logger.log(Level.WARNING, "Unknown modifier type {0}", o.getClass());
        }
        InputModifierBuilderPanel b = o.getBuildPanel(w);
        setInnerPanel(b);
    }

    private void chooseFirstCard() {
        //TODO: Improve this.
        BufferedInputEditor p = new BufferedInputEditor(w);
        setInnerPanel(p);
        
    }
    
    private void setInnerPanel(InputModifierBuilderPanel b) {
        panelInputEditor.removeAll();
        panelInputEditor.add(b);
        setDimensionality(b.getDimensionality());
        b.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(InputModifierBuilderPanel.PROP_DIMENSIONALITY)) {
                    updateDim((Integer)evt.getNewValue());
                }
            }
        });
        panelInputEditor.repaint();
        panelInputEditor.revalidate();
    }
    
    private void updateDim(int newDim) {
        setDimensionality(newDim);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comboModifierType = new javax.swing.JComboBox();
        panelInputEditor = new javax.swing.JPanel();
        firstOrderDifferenceEditor1 = new wekiinputhelper.Modifiers.FirstOrderDifferenceEditor();

        setBackground(new java.awt.Color(255, 255, 255));

        comboModifierType.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        comboModifierType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Buffer of past values", "First-order difference (\"velocity\")", "Second-order difference (\"acceleration\")", "Average over time window", "Standard deviation over window", "Minimum over window", "Maximum over window", "Minimum first-order (\"velocity\") over  window", "Maximum first-order (\"velocity\") over  window", "Minimum second-order (\"acceleration\") over window", "Maximum second-order (\"acceleration\") over window", "Buffer of past values satisfying conditions", "Mathematical expression" }));
        comboModifierType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboModifierTypeActionPerformed(evt);
            }
        });

        panelInputEditor.setBackground(new java.awt.Color(255, 255, 255));
        panelInputEditor.setLayout(new javax.swing.BoxLayout(panelInputEditor, javax.swing.BoxLayout.LINE_AXIS));
        panelInputEditor.add(firstOrderDifferenceEditor1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelInputEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(comboModifierType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(comboModifierType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelInputEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void comboModifierTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboModifierTypeActionPerformed
        //TODO: Update
       /* FirstOrderDifferenceEditor p = new FirstOrderDifferenceEditor(w);
         *
        */
        InputModifierBuilderPanel p;
        int i = comboModifierType.getSelectedIndex();
        if (i == BUFFERED_INDEX) {
            p = new BufferedInputEditor(w);
        } else if (i == FIRST_ORDER_INDEX) {
            p = new FirstOrderDifferenceEditor(w);
        } else if (i == SECOND_ORDER_INDEX) {
            p = new SecondOrderDifferenceEditor(w);
        } else if (i == AVERAGE_INDEX) {
            p = new WindowedOperationEditor(w, new AverageWindowOperation());
        } else if (i == STDEV_INDEX) {
            p = new WindowedOperationEditor(w, new StdDevWindowOperation());
        } else if (i == MIN_INDEX) {
            p = new WindowedOperationEditor(w, new MinWindowOperation());
        } else if (i == MAX_INDEX) {
            p = new WindowedOperationEditor(w, new MaxWindowOperation());
        } else if (i == MIN_FIRST_INDEX) {
            p = new WindowedOperationEditor(w, new Min1stWindowOperation());
        } else if (i == MAX_FIRST_INDEX) {
            p = new WindowedOperationEditor(w, new Max1stWindowOperation());
        } else if (i == MIN_SECOND_INDEX) {
            p = new WindowedOperationEditor(w, new Min2ndWindowOperation());
        } else if (i == MAX_SECOND_INDEX) {
            p = new WindowedOperationEditor(w, new Max2ndWindowOperation());
        } else if (i == BUFFERED_CONDITION_INDEX) {
            p = new ConditionalBufferInputEditor(w);
        } else if (i == EXPRESSION_INDEX) {
            p = new ExpressionInputEditor(w);
        } else {
            p = new WindowedOperationEditor(w, new AverageWindowOperation());
            logger.log(Level.WARNING, "Unknown modifier index "+ i);
        }
        setInnerPanel(p);
        /*panelInputEditor.removeAll();
        panelInputEditor.add(p);
        panelInputEditor.repaint();
        panelInputEditor.revalidate();
        this.repaint(); */
        
    }//GEN-LAST:event_comboModifierTypeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboModifierType;
    private wekiinputhelper.Modifiers.FirstOrderDifferenceEditor firstOrderDifferenceEditor1;
    private javax.swing.JPanel panelInputEditor;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean validateForm() {
        Component p = panelInputEditor.getComponent(0);
        if (p == null) {
            return false;
        } else if (p instanceof InputModifierBuilderPanel) {
            return ((InputModifierBuilderPanel)p).validateForm();
        } else return false;
    }

    @Override
    public ModifiedInput getModifiedInput() {
        Component p = panelInputEditor.getComponent(0);
        if (p == null) {
            return null;
        } else if (p instanceof InputModifierBuilderPanel) {
            return ((InputModifierBuilderPanel)p).getModifiedInput();
        } else return null;
    }
}
