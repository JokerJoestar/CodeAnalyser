package gr.teilar.codeanalyser.handlers;

import java.util.ArrayList;
import java.util.List;

public class MethodCA {
	private String packageCA;
	private String classCA;
	private String name;
	private String identifier;
	private String returnType;
	private List<String> parameters = new ArrayList<>();

	public MethodCA() { }
	
	public MethodCA(String packageCA, String classCA, String name, String identifier, String returnType, List<String> parameters) {
		this.packageCA = packageCA;
		this.classCA = classCA;
		this.name = name;
		this.identifier = identifier;
		this.returnType = returnType;
		this.parameters = parameters;
	}

	public String getPackageCA() {
		return packageCA;
	}

	public void setPackageCA(String packageCA) {
		this.packageCA = packageCA;
	}

	public String getClassCA() {
		return classCA;
	}

	public void setClassCA(String classCA) {
		this.classCA = classCA;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void addParameter(String parameter) {
		parameters.add(parameter);
	}
	
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public String toString() {
		return "MethodCA [packageCA=" + packageCA + ", classCA=" + classCA + ", name=" + name + ", identifier="
				+ identifier + ", returnType=" + returnType + ", parameters=" + parameters + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classCA == null) ? 0 : classCA.hashCode());
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((packageCA == null) ? 0 : packageCA.hashCode());
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodCA other = (MethodCA) obj;
		if (classCA == null) {
			if (other.classCA != null)
				return false;
		} else if (!classCA.equals(other.classCA))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (packageCA == null) {
			if (other.packageCA != null)
				return false;
		} else if (!packageCA.equals(other.packageCA))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		return true;
	}
}
