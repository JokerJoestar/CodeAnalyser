package gr.teilar.codeanalyser.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import org.eclipse.jdt.core.dom.ASTVisitor;
//import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public final class MethodDeclarationFinder extends ASTVisitor {
	private final List<MethodDeclaration> methods = new ArrayList<>();

	@Override
	public boolean visit(final MethodDeclaration method) {
		methods.add(method);
		return super.visit(method);
	}
	
	public List<MethodDeclaration> getMethods() {
		return Collections.unmodifiableList(methods);
	}
}