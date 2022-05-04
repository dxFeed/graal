package com.oracle.truffle.dsl.processor.operations.instructions;

import com.oracle.truffle.dsl.processor.java.model.CodeTree;
import com.oracle.truffle.dsl.processor.java.model.CodeTreeBuilder;
import com.oracle.truffle.dsl.processor.operations.Operation.BuilderVariables;

public class ReturnInstruction extends Instruction {

    public ReturnInstruction(int id) {
        super("return", id, ResultType.RETURN, InputType.STACK_VALUE);
    }

    @Override
    public boolean standardPrologue() {
        return false;
    }

    @Override
    public CodeTree createExecuteCode(ExecutionVariables vars) {
        CodeTreeBuilder b = CodeTreeBuilder.createBuilder();

        b.startAssign(vars.returnValue).startCall(vars.frame, "getObject");
        b.startGroup().variable(vars.sp).string(" - 1").end();
        b.end(2);

        return b.build();
    }

    @Override
    public CodeTree createCustomEmitCode(BuilderVariables vars, CodeTree[] arguments) {
        CodeTreeBuilder b = CodeTreeBuilder.createBuilder();

        b.startStatement().startCall("calculateLeaves");
        b.startGroup().variable(vars.operationData).end();
        b.end(2);

        return b.build();
    }

    @Override
    public BoxingEliminationBehaviour boxingEliminationBehaviour() {
        return BoxingEliminationBehaviour.DO_NOTHING;
    }

    @Override
    public CodeTree createPrepareAOT(ExecutionVariables vars, CodeTree language, CodeTree root) {
        return null;
    }

    @Override
    public CodeTree[] createTracingArguments(ExecutionVariables vars) {
        return new CodeTree[]{
                        CodeTreeBuilder.singleString("ExecutionTracer.INSTRUCTION_TYPE_OTHER"),
        };
    }

}
