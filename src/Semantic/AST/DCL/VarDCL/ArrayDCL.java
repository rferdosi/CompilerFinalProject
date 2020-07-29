package Semantic.AST.DCL.VarDCL;

import Semantic.AST.Expression.Expression;
import Semantic.AST.Expression.constant.IntegerConstExp;
import Semantic.SymbolTable.SymbolTable;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.ASTORE;

public class ArrayDCL extends VarDCL{
    private ArrayList<Expression> dimensionsExpression;
    private int dimensionNum;

    public ArrayDCL(String name, Type type, boolean global, int dimensionNum) {
        this.name = name;
        this.type = type;
        this.global = global;
        dimensionsExpression = new ArrayList<>(dimensionNum);
        this.dimensionNum = dimensionNum;
    }
    //injaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaasopjegfghir
    public ArrayDCL(String name, String stringType, boolean global, Integer dimensionNum, Type type, ArrayList<Expression> dimensionsExpression) {
        this.name = name;
        if (!stringType.equals("auto")) {
            if (!SymbolTable.getTypeFromVarName(stringType).equals(type))
                throw new RuntimeException("the types of array doesn't match");
        } else if (dimensionsExpression == null)
            throw new RuntimeException("auto variables must have been initialized");
        if (dimensionNum != null) {
            if (dimensionNum != dimensionsExpression.size())
                throw new RuntimeException("dimensions are't correct");
            this.dimensionNum = dimensionNum;
        }
        this.type = type;
        this.global = global;
        this.dimensionsExpression = dimensionsExpression;
    }

    @Override
    public void codegen(ClassWriter cw, MethodVisitor mv) {
        for (Expression dim : dimensionsExpression) {
            dim.codegen(cw, mv);
        }
        if (global){

            String repeatedArray = new String(new char[dimensionsExpression.size()]).replace("\0", "[");
            Type arrayType = Type.getType(repeatedArray + type.getDescriptor());
            cw.visitField(ACC_STATIC, name, arrayType.getDescriptor(), null, null).visitEnd();
        }
        else {
            if(dimensionsExpression.size() == 0){
                new IntegerConstExp(1000).codegen(cw, mv);
            }
            if (dimensionNum == 1) {
                if (type.getDescriptor().startsWith("L"))
                    mv.visitTypeInsn(ANEWARRAY, type.getDescriptor());
                else
                    mv.visitIntInsn(NEWARRAY, SymbolTable.getTType(type));
            } else{
                String t = "";
                for (int i = 0; i < dimensionNum; i++) {
                    t += "[";
                }
                t += type.getDescriptor();
                mv.visitMultiANewArrayInsn(t, dimensionsExpression.size());
            }
            mv.visitVarInsn(ASTORE, SymbolTable.getInstance().getIndex());
        }
    }
}
