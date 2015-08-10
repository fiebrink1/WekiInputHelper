/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekiinputhelper.Modifiers;

import expr.Variable;

/**
 *
 * @author rebecca
 */
  public class IndexedVar {
        public String originalVariable;
        public String unindexedName;
        public int delay;
        
        public static IndexedVar makeIndexedVariableWithoutDelay(String var) {
            return new IndexedVar(var, 0);
        }

        private IndexedVar(String unindexedName, int delay) {
            originalVariable = unindexedName;
            this.unindexedName = unindexedName;
            this.delay = delay;
        } 
                
        public IndexedVar(String full) {
            originalVariable = full;
            int firstBracket = full.indexOf('[');
            int secondBracket = full.indexOf(']');
            if (firstBracket == -1 || secondBracket == -1) {
                throw new IllegalArgumentException("Improperly formatted");
            }
            String varName = full.substring(0, firstBracket);
            String index = full.substring(firstBracket, secondBracket);
            if (varName.length() == 0 || index.length() < 2) {
                throw new IllegalArgumentException("Improperly formatted");
            }
            if (index.charAt(1) != 'n') {
                throw new IllegalArgumentException("Improperly formatted");
            }
            if (index.length() == 2) {
                unindexedName = varName;
                delay = 0;
                return;
            }
            if (index.charAt(2) != '-') {
                throw new IllegalArgumentException("Improperly formatted indexed variable: Must be in form e.g. inputName[n-3]");
            }
            String offset = index.substring(3);
            int d;
            try {
                d = Integer.parseInt(offset);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Improperly formatted indexed variable: Must be in form e.g. inputName[n-3]");
            }
            if (d < 0) {
                throw new IllegalArgumentException("Improperly formatted indexed variable: Must be in form e.g. inputName[n-3]");
            }
            unindexedName = varName;
            delay = d;
        }

    }
