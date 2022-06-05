package com.fundamentos.springboot.fundamentos.component;

import org.springframework.stereotype.Component;

@Component
public class ComponentTwoImplement implements ComponentDependency{
    @Override
    public void saludar(){
        System.out.println("hola desde mi 2do componente! :D");
    }
}