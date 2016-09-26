package com.markliu.study.cglib;

public /*final*/ class SampleClass {

    private String name;
    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SampleClass() {
        System.out.println("SampleClass()...");
    }

    public SampleClass(String name) {
        System.out.println("SampleClass()..." + name);
    }

    public String test(String input) {
        System.out.println("test: " + input);
        return "Hello " + input;
    }

    public String test2(String input) {

        return "input: " + input;
    }

    @Override
    public String toString() {
        return "SampleClass{}";
    }

    @Override
    public int hashCode() {
        System.out.println("hashCode");
        return super.hashCode();
    }
}