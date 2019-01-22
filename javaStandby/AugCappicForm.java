package edu.mass.memorialhall.sof.eval;

package museum.history.deerfield.centuries.oralhistory;

/**
 * custom object for cappics that holds xml>html text in addition to the regular fields 
 * 
 */
public class AugCappicForm extends ActionForm {

    private String shortName_;
    private String tab_;

    public String getShortName()   {return shortName_;} 
    public String getTab()         {return tab_;} 

    public void setShortName       ( String shortName )   {shortName_ = shortName;}    
    public void setTab             ( String tab )         {tab_       = tab; }    
}