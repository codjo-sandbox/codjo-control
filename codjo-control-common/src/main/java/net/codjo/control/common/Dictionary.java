/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.control.common;
import net.codjo.variable.TemplateInterpreter;
import net.codjo.variable.UnknownVariableException;
import java.util.HashMap;
import java.util.Map;
/**
 * Description of the Class
 *
 * @author $Author: nadaud $
 * @version $Revision: 1.7 $
 */
public class Dictionary implements Cloneable {
    private TemplateInterpreter interpreter = new TemplateInterpreter();
    private Map<String, Variable> variablesForName = new HashMap<String, Variable>();
    private java.sql.Timestamp now;
    private Dictionary parent;


    public Dictionary() {
    }


    /**
     * Clone ce dictionnaire : les champs now et parent ne sont pas clon�s, mais les variables sont clon�es.
     *
     * @return
     */
    @Override
    public Object clone() {
        // Cr�ation d'une nouvelle instance
        Dictionary newDico = new Dictionary();

        // On garde les m�mes r�f�rences pour now et parent
        newDico.now = this.now;
        newDico.parent = this.parent;

        // On remplit la nouvelle Map en clonant les valeurs
        for (String variableName : this.variablesForName.keySet()) {
            Variable variable = this.variablesForName.get(variableName);
            newDico.variablesForName.put(variableName, (Variable)variable.clone());
        }
        return newDico;
    }


    public void setNow(java.sql.Timestamp now) {
        this.now = now;
        addVariable("today", new java.sql.Date(now.getTime()).toString());
    }

//    public void setVariables(java.util.Collection variables) {
//        interpreter.clear();
//        this.variables = variables;
//        interpreter.addAll((List)variables);


    //    }
    public java.sql.Timestamp getNow() {
        return now;
    }


    public java.util.Collection getVariables() {
        return variablesForName.values();
    }


    public Variable getVariable(String variableName) {
        return variablesForName.get(variableName);
    }


    public void addVariable(Variable variable) {
        variablesForName.put(variable.getName(), variable);
    }


    public void addVariable(String name, String value) {
        Variable variable = new Variable(name, value);
        addVariable(variable);
    }


    public String replaceVariables(String pattern) {
        try {
            interpreter.clear();

            // On ajoute d'abord les variables du dictionnaire p�re (s'il y en a un),
            // le dictionnaire fils pouvant �craser certaines valeurs.
            if (parent != null) {
                interpreter.addAll(parent.variablesForName.values());
            }
            interpreter.addAll(variablesForName.values());
            return interpreter.evaluate(pattern);
        }
        catch (UnknownVariableException error) {
            throw new IllegalArgumentException(error.getMessage());
        }
    }


    /**
     * Ajoute les variables d'un dictionnaire � ce dictionnaire ; note : les variables ajout�es ne sont pas
     * clon�es.
     *
     * @param dico
     */
    public void addDictionary(Dictionary dico) {
        for (Variable variable : dico.variablesForName.values()) {
            addVariable(variable);
        }
    }


    public Dictionary getParent() {
        return parent;
    }


    public void setParent(Dictionary parent) {
        this.parent = parent;
    }


    @Override
    public String toString() {
        return "(now=" + getNow() + " vars= " + variablesForName.values() + ")";
    }
}
