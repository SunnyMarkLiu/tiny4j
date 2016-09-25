package com.markliu.study.cglib;

public /*final*/ class SampleClass {

    public SampleClass() {
        System.out.println("SampleClass()...");
    }

    public SampleClass(String name) {
        System.out.println("SampleClass()..." + name);
    }

    public String test(String input) {

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