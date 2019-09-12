package gr.teilar.codeanalyser.handlers;

public class CallerCA {
	private MethodCA caller;
	private MethodCA callee;

	public CallerCA(MethodCA caller, MethodCA callee) {
		this.caller = caller;
		this.callee = callee;
	}

	public MethodCA getCaller() {
		return caller;
	}
	
	public void setCaller(MethodCA caller) {
		this.caller = caller;
	}
	
	public MethodCA getCallee() {
		return callee;
	}

	public void setCallee(MethodCA callee) {
		this.callee = callee;
	}
	
	@Override
	public String toString() {
		return "CallerCA [caller=" + caller + ", callee=" + callee + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((callee == null) ? 0 : callee.hashCode());
		result = prime * result + ((caller == null) ? 0 : caller.hashCode());
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
		CallerCA other = (CallerCA) obj;
		if (callee == null) {
			if (other.callee != null)
				return false;
		} else if (!callee.equals(other.callee))
			return false;
		if (caller == null) {
			if (other.caller != null)
				return false;
		} else if (!caller.equals(other.caller))
			return false;
		return true;
	}
}
