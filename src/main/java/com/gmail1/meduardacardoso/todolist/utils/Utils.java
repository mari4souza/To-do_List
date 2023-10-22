package com.gmail1.meduardacardoso.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {
    //qnd tem static n precisa instanciar a classe         //de onde ele ta vindo e pra onde ele vai
    public static void copyNonNullPropertys(Object source, Object target ) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source)); //pegando tudo q eu tenho de propriedade nula e atribuindo para o bean q vai fazer a conersao pra mesclar
    }



    public static String[] getNullPropertyNames(Object source) {  // esse metodo vai permitir mesclar propriedades nao nulas de dois objetos
        final BeanWrapper src =  new BeanWrapperImpl(source); // interface q permite acessar as propriedades de um objeto
        
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();

        for(PropertyDescriptor pd: pds) {
            Object srcValue = src.getPropertyValue(pd.getName()); // para cada propertyvalue vou pegar o getname e obter a valor da propriedade
            if(srcValue == null) { //verificando se as propriedades sao nulas
                emptyNames.add(pd.getName()); // se for nula add no emptynames
            }
        }

        String[] result = new String [emptyNames.size()];//criando um array para armazenar o nome de todas as propriedades nulas
        return emptyNames.toArray(result); //converte o conjunto de nome das propriedades nulas em um array
    }
}
