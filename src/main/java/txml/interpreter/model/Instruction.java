/* 
 * Copyright 2016 Tomas Kunovsky.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package txml.interpreter.model;

public class Instruction {
    final private String name;
    private String operand1 = null;
    private String operand2 = null;
    private String operand3 = null;
    private String result = null;
    
    public Instruction(String name, String operand1, String operand2, String operand3, String result) {
        this.name = name;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operand3 = operand3;
        this.result = result;
    }

    public String getName() {
        return name;
    }
    
    public String getOperand1() {
        return operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    public String getOperand3() {
        return operand3;
    }
    
    public String getResult() {
        return result;
    }

    public void setOperand1(String operand1) {
        this.operand1 = operand1;
    }

    public void setOperand2(String operand2) {
        this.operand2 = operand2;
    }

    public void setOperand3(String operand3) {
        this.operand3 = operand3;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    @Override 
    public String toString() {
        return "(" + name + ", " + this.operand1 + ", " + this.operand2 + ", " + this.operand3 + ", " + result + ")";
    }
}
