package museum.history.deerfield.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.validator.DynaValidatorForm; 
import org.apache.struts.validator.Resources; 
import org.apache.commons.validator.ValidatorAction; 
import org.apache.commons.validator.Field; 
import org.apache.commons.validator.GenericValidator; 

public final class Validators {

  public static boolean validateTwoFields( 
    Object             bean, 
    ValidatorAction    va, 
    Field              field, 
    ActionErrors       errors, 
    HttpServletRequest request ) {

    // Declare the dynabean  
    DynaValidatorForm form = (DynaValidatorForm) bean;

    // Get the names of the fields we want to compare.
    String name1 = field.getProperty();
    String name2 = field.getVarValue( "secondProperty" );
    
    // Get the data in the fields
    String value1 = (String)form.get( name1 );
    String value2 = (String)form.get( name2 );

    if (!GenericValidator.isBlankOrNull( value1 )) {
      try {
        if (!value1.equals( value2 )) {
          errors.add( field.getKey(), Resources.getActionError( request, va, field ) );
          return false;
        }
      } catch (Exception e) {
        errors.add( field.getKey(), Resources.getActionError( request, va, field ) );
        return false;
      }
    }

    return true;
  }
}