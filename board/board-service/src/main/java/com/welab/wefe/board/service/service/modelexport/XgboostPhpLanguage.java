/**
 * Copyright 2021 The WeFe Authors. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.welab.wefe.board.service.service.modelexport;

import java.util.List;

/**
 * PHP
 *
 * @author aaron.li
 **/
public class XgboostPhpLanguage extends BaseXgboostLanguage {

    @Override
    protected String preBuild2ClassificationsMethodSignNameCode(int treeNum, String initScore) {
        StringBuilder preMethodCode = new StringBuilder();
        preMethodCode.append("<?php")
                .append("\n")
                .append("function score(array $input) {")
                .append("\n")
                .append(METHOD_BODY_PLACEHOLDER)
                .append("\n")
                .append("}");

        return preMethodCode.toString();
    }

    @Override
    protected String build2ClassificationsResultLogicCode(int treeNum, String initScore) {
        StringBuilder methodCalcCode = new StringBuilder();
        String summaryVar = "s1";
        methodCalcCode.append(indentationByNodeLayer(1, false));
        methodCalcCode.append("$" + generateVarDef(summaryVar))
                .append("\n")
                .append(indentationByNodeLayer(1, false))
                .append("$")
                .append(summaryVar)
                .append(" = 1 / (1 + exp(0 - (")
                .append(generateTreeSum(treeNum, initScore))
                .append(")))")
                .append(lineEndSymbol())
                .append("\n")
                .append(build2ClassificationsReturnCode(summaryVar, initScore));

        return methodCalcCode.toString();
    }

    @Override
    protected String build2ClassificationsReturnCode(String varName, String initScore) {
        StringBuilder methodResultCode = new StringBuilder();
        methodResultCode.append(indentationByNodeLayer(1, false))
                .append("return array(1 - $" + varName + ", $" + varName + ")" + lineEndSymbol());

        return methodResultCode.toString();
    }

    @Override
    protected String buildExpFunction(List<String> treeVarNameList, String initScore) {
        StringBuilder code = new StringBuilder();
        code.append("exp(0 - (")
                .append(generateTreeSum(treeVarNameList, initScore))
                .append("))");

        return code.toString();
    }

    @Override
    protected String buildMultipleClassificationsReturnCode(int classificationsNum) {
        StringBuilder code = new StringBuilder();
        code.append(resultIndentationNum(2))
                .append("return array(");
        code.append(generateCodeByClassificationsNum(classificationsNum));
        code.append(")").append(lineEndSymbol());
        return code.toString();
    }


    @Override
    protected String resultIndentationNum(int num) {
        return super.resultIndentationNum(1);
    }


    @Override
    protected String indentationByNodeLayer(Node node, boolean initIndentation) {
        return super.indentationByNodeLayer(node, false);
    }

    @Override
    protected String indentationByNodeLayer(int layer, boolean initIndentation) {
        return super.indentationByNodeLayer(layer, false);
    }


    @Override
    protected String generateVarDef(String varName) {
        return varName + " = null;";
    }

    @Override
    protected String generateCompareVarName(String index) {
        return "$input[" + index + "]";
    }

    @Override
    protected String generateVarName(int treeIndex) {
        return "$var" + treeIndex;
    }

    @Override
    protected String generateResultVarName(int treeIndex) {
        return "$s" + treeIndex;
    }

}
