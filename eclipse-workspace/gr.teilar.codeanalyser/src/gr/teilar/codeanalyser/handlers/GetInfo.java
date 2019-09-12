package gr.teilar.codeanalyser.handlers;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NodeFinder;

public class GetInfo extends AbstractHandler {
	private static List<MethodCA> methodsCA;
	private static List<CallerCA> callersCA;
	private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";
	HierarchyCallHelper hc = new HierarchyCallHelper();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		// Get all projects in the workspace
		IProject[] projects = root.getProjects();
		// Loop over all projects
		for (IProject project : projects) {
			if(project.isOpen()) {
				try {
					if (project.isNatureEnabled(JDT_NATURE)) {
						System.out.println("Analyzing project \"" + project.getName() + "\".");
						analyseMethods(project);
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
			else
				System.out.println("\"" + project.getName() + "\" is a closed project.");
		}
		return null;
	}

	private void analyseMethods(IProject project) throws JavaModelException {
		// reset arraylists if analyser is pressed again
		methodsCA = new ArrayList<MethodCA>();
		callersCA = new ArrayList<CallerCA>();
		IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();
		// parse(JavaCore.create(project));
		for (IPackageFragment mypackage : packages) {
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				createAST(mypackage, 0);
			}
		}

		methodsCA.forEach((MethodCA m) -> {
			System.out.println(m.toString());
		});
		System.out.println("=================================================================================");

		for (IPackageFragment mypackage : packages) {
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				createAST(mypackage, 1);
			}
		}
		
		callersCA.forEach((CallerCA m) -> {
			System.out.println(m.toString());
		});
		System.out.println("=================================================================================");

		createOutput(callersCA, methodsCA);
		
		hc.getCallHierarchy().setFilterEnabled(false);
	}

	private void createAST(IPackageFragment mypackage, int usage) throws JavaModelException {
		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			// now create the AST for the ICompilationUnits
			CompilationUnit parse = parse(unit);
			MethodDeclarationFinder visitor = new MethodDeclarationFinder();

			parse.accept(visitor);

			for (MethodDeclaration method : visitor.getMethods()) {
				IMethodBinding mBinding = method.resolveBinding();

				List<String> paramsCallee = new ArrayList<>();
				ITypeBinding[] bindCallee = mBinding.getParameterTypes();
				String returnTypeCallee = "";

				for (ITypeBinding param : bindCallee)
					paramsCallee.add(param.getName());

				int i = method.getModifiers();

				String identifierCallee = "";
				if (Modifier.isPrivate(i))
					identifierCallee = "private";
				if (Modifier.isProtected(i))
					identifierCallee = "protected";
				if (Modifier.isPublic(i))
					identifierCallee = "public";

				if (method.getReturnType2() != null)
					returnTypeCallee = method.getReturnType2().toString();

				if (usage == 0 && !mBinding.getDeclaringClass().getName().toString().equals("") 
						&& !mBinding.getDeclaringClass().getName().equals("Null")) {
					MethodCA m = new MethodCA(mypackage.getElementName(),
							mBinding.getDeclaringClass().getName().toString(), method.getName().toString(),
							identifierCallee, returnTypeCallee, paramsCallee);

					methodsCA.add(m);
				} else {
					IMethod p = (IMethod) mBinding.getJavaElement();

					for(IMethod o : hc.getCallersOf(p)) {
						MethodDeclaration md = getMDNode(o);
						List<String> paramsCaller = new ArrayList<>();
						ITypeBinding[] bindCaller = md.resolveBinding().getParameterTypes();

						String returnTypeCaller = "";

						for (ITypeBinding param : bindCaller)
							paramsCaller.add(param.getName());

						if (md.getReturnType2() != null)
							returnTypeCaller = md.getReturnType2().toString();

						if (getMethodFromList(methodsCA, md, paramsCaller, returnTypeCaller) != null) {
							MethodCA caller = getMethodFromList(methodsCA, md, paramsCaller, returnTypeCaller);

							if (getMethodFromList(methodsCA, method, paramsCallee, returnTypeCallee) != null) {
								MethodCA callee = getMethodFromList(methodsCA, method, paramsCallee, returnTypeCallee);

								CallerCA call = new CallerCA(caller, callee);

								callersCA.add(call);
							}
						}
					}
				}
			}
		}
	}
	
	private void createOutput(List<CallerCA> callers, List<MethodCA> methods) {
		List<String> packageList = new ArrayList<>();
		List<String> remClassList = new ArrayList<>();
		
		for(CallerCA o : callers) {
			String callerPackage = o.getCaller().getPackageCA();
			String calleePackage = o.getCallee().getPackageCA();
			
			if(!packageList.contains(callerPackage))
				packageList.add(callerPackage);
			
			if(!packageList.contains(calleePackage))
				packageList.add(calleePackage);
		}
		
		int[] packageSize = new int[packageList.size()];
		
		for(MethodCA p : methods) {
			for(int i = 0; i < packageSize.length; i++) {
				if(p.getPackageCA().equals(packageList.toArray()[i])) {
					if(!remClassList.contains(p.getClassCA())) {
						packageSize[i]++;
						remClassList.add(p.getClassCA());
					}
				}
			}
		}
		remClassList.clear();

		String fileContent[] = new String[packageList.size()+1];
		fileContent[0] = "";
		
		for(int i = 0; i < packageList.toArray().length; i++) {
			fileContent[0] += packageList.toArray()[i].toString();
			
			if(i != (packageList.toArray().length - 1))
				fileContent[0] += " ";
		}

		System.out.println("Package \tNumber of Classes");
		for(int i=0; i < packageList.toArray().length; i++) {
			System.out.println(packageList.toArray()[i] + "\t" + packageSize[i]);
		}
		
		double array[][] = new double[packageList.size()][packageList.size()];
		
		System.out.println("=================================================================================");
		System.out.println("Similarity matrix got saved.");
		for(int i = 0; i < array.length; i++) {
			int temp = i+1;
			fileContent[temp] = "";
			
			for(int j = 0; j < array[i].length; j++) {
				int counter = 0;
				
				for(CallerCA o : callers) {
					if(!packageList.toArray()[i].equals(packageList.toArray()[j])) {
						if(o.getCaller().getPackageCA().equals(packageList.toArray()[i]) && o.getCallee().getPackageCA().equals(packageList.toArray()[j])) {
							if(!remClassList.contains(o.getCallee().getClassCA())) {
								counter++;
								remClassList.add(o.getCallee().getClassCA());
							}
						}
					}
				}
				
				remClassList.clear();
				
				if(i != j)
					array[i][j] = (counter / (double) packageSize[j]);
				
				fileContent[temp] += array[i][j];
				
				if(j != (array[i].length - 1))
					fileContent[temp] += " ";
			}
		}
		
		String directory = System.getProperty("user.home");  
		Path path = Paths.get(directory, "output.txt");
		
		List<String> output = Arrays.asList(fileContent);
		  
		try
	    {
			Files.write(path, output, StandardOpenOption.CREATE);
	    }
	    catch(IOException ex)
	    {
	        ex.printStackTrace();
	    }
	}

	public static MethodDeclaration getMDNode(IMethod method) throws JavaModelException {
		ICompilationUnit methodCompilationUnit = method.getCompilationUnit();
		CompilationUnit unit = parse(methodCompilationUnit); // returns ASTNode

		ASTNode currentNode = NodeFinder.perform(unit, method.getSourceRange());

		return (MethodDeclaration) currentNode;
	}

	private static MethodCA getMethodFromList(List<MethodCA> m, MethodDeclaration md, 
			List<String> mdParams, String mdRetTypeCaller) {
		
		MethodCA ret = null;
		String mdPackageName = md.getRoot().toString().split("package ")[1].split(";")[0];

		for (MethodCA o : m) {
			if (o.getName().equals(md.getName().toString()) && o.getReturnType().equals(mdRetTypeCaller)) {
				if (o.getParameters().equals(mdParams) && o.getPackageCA().equals(mdPackageName)) {
					if (o.getClassCA().equals(md.resolveBinding().getDeclaringClass().getName().toString())) {
						ret = o;

						break;
					}
				}
			}
		}

		return ret;
	}

	private static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS10);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null);
	}
}