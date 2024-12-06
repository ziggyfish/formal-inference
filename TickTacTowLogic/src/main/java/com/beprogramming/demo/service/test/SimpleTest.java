package com.beprogramming.demo.service.test;

import com.beprogramming.demo.processor.ValidateAnnotation;

@ValidateAnnotation
public class SimpleTest {
    int simpleTestTest = 1;
    int[] simpleTestTest2 = {4};

    /*@ requires x > 0 && y > 0;
      @ ensures \result == x + y;
      @*/
    public static int add(int x, int y) {
        return x + y;
    }

    public int testAdd(int z, int r) {
        return add(z, r);
    }

    public void setSimpleTestTest(int newTest){
        int a = newTest+1;
        simpleTestTest = a + 1;
    }

    public int testConstWithAdd() {

        return 1+2;
    }

    public int testWithMultiply() {
        return 2*3;
    }

    public int testWithConstant(int x, int y){
        return x+y;
    }

    public int testWithMath(int x, int y){
        return x + y + 1;
    }

    public int testAddWithConstant(int x, int y){
        return add(1, 2);
    }

    public int testAddWithConstantAssignment(int x, int y){
        int z = 1;
        return add(z, 2);
    }

    public int testAddWithConstantAssignmentIncrement(int x, int y){
        int z = 1;
        return add(z + 1, y);
    }

    public int testAddWithConstantAssignmentIncrementWithIntermediateVariable(int x, int y){
        int z = 1;
        int a = z + 2;
        return add(a, z);
    }

    public int testAddIncrement(int x, int y) {
        return add(x + 1, y);
    }

    public int testAddVariable(int x, int y) {
        int z = x;
        return add(z, y);
    }

    public int testAddVariableIncrement(int x, int y) {
        int z = x + 1;
        return add(z, y);
    }

    public int testAddVariableIncrementWithIntermediateVariable(int x, int y) {

        int z = x + 1;
        int a = z + 2;
        return add(a, z);

    }

    public int testAddVariableWithY(int x, int y) {
        int z = x + y;
        return add(z, y);
    }

    public int testAddWithIf(int x, int y) {
        if (x == 1) {
            return add(x, y);
        }
        return 0;
    }

    public int testAddWithElse(int x, int y) {
        int z = 0;
        if (x == 1) {
            z = 2;
        } else {
            return add(x, y);
        }
        return 0;
    }

    public int testAddWithElseIf(int x, int y) {
        int z = 0;
        if (x == 1) {
            z = 2;
        } else if (x == 2) {
            return add(x, y);
        }
        return 0;
    }

    public int testAddWithElseIfElse(int x, int y) {
        int z = 0;
        if (x == 1) {
            z = 1;
        } else if (x == 2) {
            z = 2;
        } else {
            return add(x, y);
        }
        return 0;
    }

    public int testAddWithCallIf(int x, int y) {
        int z = 0;
        if (add(x, y) > 0) {
            return x;
        }
        return 0;
    }

    public int testAddWithCallIfAndSubsitution(int x, int y) {
        int z = 0;
        if (add(z, y) > 0) {
            return 0;
        }
        return 0;
    }

    public int testAddWithForLoop(int x) {
        int z = 0;
        for (int i = 0; i < x; i++) {
            z = add(z, 1);
        }
        return z;
    }

}
