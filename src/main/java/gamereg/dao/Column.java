package gamereg.dao;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
/**
 * This annotation should be used on top of the entity fields<br> 
 * to show that field name should be included in table and to specify that it's name should be changed
 * @author Erick
 *
 */
public @interface Column {
	public String name() default ""; //in the code if checked name = "" then field name will be used
	public boolean pk() default false;
	public boolean nullable() default true;
	public boolean unique() default false;
	public int size() default 10; // only used with string 
}
