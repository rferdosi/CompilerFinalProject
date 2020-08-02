package Semantic.AST.Statement.assignment;

import Semantic.AST.Expression.Expression;
import Semantic.AST.Expression.binary.arithmetic.Mult;
import Semantic.AST.Expression.binary.arithmetic.Plus;
import Semantic.AST.Expression.variable.Variable;
import Semantic.SymbolTable.DSCP.DSCP;
import Semantic.SymbolTable.DSCP.DynamicLocalDSCP;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class MultAssign extends Assignment {
    public MultAssign(Variable variable, Expression expression) {
        super(variable, expression);
    }

    @Override
    public void codegen(ClassWriter cw, MethodVisitor mv) {
        new Assign(variable, new Mult(variable, expression)).codegen(cw, mv);
    }
}
