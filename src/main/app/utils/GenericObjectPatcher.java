package main.app.utils;

import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;


@Component
public class GenericObjectPatcher {
	/** Replaces the fields of an object with the field of other
	 * 
	 * @param <T> Class of the object
	 * @param incompleteObject object with fields to insert into other object
	 * @param CompleteObject object with fields to be replaced
	 */
	public static<T> void patch(T providerObject,T recieverObject) {

		//GET THE COMPILED VERSION OF THE CLASS
        Class<?> recieverObjectClass= recieverObject.getClass();
        Field[] fields=recieverObjectClass.getDeclaredFields();
        
        for(Field field : fields){
            //CANT ACCESS IF THE FIELD IS PRIVATE
            field.setAccessible(true);

            //CHECK IF THE VALUE OF THE FIELD IS NOT NULL, IF NOT UPDATE EXISTING INTERN
            try {
            	Object value=field.get(providerObject);
            	if(value!=null){
            		field.set(recieverObject,value);
            	}            	
            }catch(Exception e) {
            	
            }
            //MAKE THE FIELD PRIVATE AGAIN
            field.setAccessible(false);
        }

    }

}


