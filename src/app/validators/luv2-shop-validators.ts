import { FormControl, ValidationErrors } from "@angular/forms";

export class Luv2ShopValidators {

    // whitespace validation
    static notOnlyWhitespace(control: FormControl): ValidationErrors{
        
        //check if only has whitespaces
        if ((control.value != null) && (control.value.trim().length === 0)) {

            // invalid so return error onbject
            return {'notOnlyWhitespace': true};
        }
        else{
            return null;
        }
        

    }
}
