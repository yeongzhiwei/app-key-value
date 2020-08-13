package api.singtel.appkeyrecord.api.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Size(min = 1, max = 256)
@Pattern(regexp = "^[\\p{Alnum}]*$")
@Constraint(validatedBy = { })
public @interface AlnumSizePattern {

    @OverridesAttribute(constraint = Size.class, name="max")
    int max() default 256;

    @OverridesAttribute(constraint = Size.class, name="message")
    @OverridesAttribute(constraint = Pattern.class, name="message")
    String message() default "must be alphanumeric and up to 256 characters";

    Class<?>[] groups() default { };
    
    Class<? extends Payload>[] payload() default { };
    
}