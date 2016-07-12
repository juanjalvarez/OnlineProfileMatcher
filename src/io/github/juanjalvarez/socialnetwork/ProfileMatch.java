package io.github.juanjalvarez.socialnetwork;

public class ProfileMatch {

	private Profile a;
	private Profile b;
	private double relation;

	public ProfileMatch(Profile a, Profile b, double relation) {
		this.a = a;
		this.b = b;
		this.relation = relation;
	}

	public Profile getA() {
		return a;
	}

	public Profile getB() {
		return b;
	}

	public double getRelation() {
		return relation;
	}

	@Override
	public String toString() {
		return String.format("'%s ~ %s' + '%s ~ %s' = %.2f", a.getField("username").getValue(),
				a.getField("realname").getValue(), b.getField("username").getValue(), b.getField("realname").getValue(),
				relation);
	}
}
