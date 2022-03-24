/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
/**
 *
 * @author SENA
 */
public class addadopcionBeanValidation implements Validator {
    @Override
    public boolean supports(Class<?> type) {
       return adopcionBean.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
    adopcionBean Adopcion = (adopcionBean) o;
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fecha_adopcion","required.fecha_adopcion","El campo fecha es obligatorio");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id_mascotas","required.id_mascotas","El campo id de la mascota es obligatorio");  
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id_personas","required.id_personas","El campo id de la persona es obligatorio");    
    }
    
}
