package main.app.utils;

import java.lang.reflect.Field;


import org.springframework.stereotype.Component;


@Component
public class GenericObjectPatcher {
	/** Replaces the fields of an object with the field of other
	 * 
	 * @param <T> Class of the object
	 * @param incompleteObject object with fields to insert into other object
	 * @param CompleteObject object with fields to be replaced
	 */
	public static<T> void patch(T incompleteObject,T CompleteObject) {

		//GET THE COMPILED VERSION OF THE CLASS
        Class<?> completeObjectClass= CompleteObject.getClass();
        Field[] fields=completeObjectClass.getDeclaredFields();
        
        for(Field field : fields){
            //CANT ACCESS IF THE FIELD IS PRIVATE
            field.setAccessible(true);

            //CHECK IF THE VALUE OF THE FIELD IS NOT NULL, IF NOT UPDATE EXISTING INTERN
            try {
            	Object value=field.get(incompleteObject);
            	if(value!=null){
            		field.set(CompleteObject,value);
            	}            	
            }catch(Exception e) {
            	
            }
            //MAKE THE FIELD PRIVATE AGAIN
            field.setAccessible(false);
        }

    }

}


