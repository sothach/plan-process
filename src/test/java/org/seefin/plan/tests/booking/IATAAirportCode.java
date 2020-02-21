package org.seefin.plan.tests.booking;

public enum IATAAirportCode
{
	ORK("Cork Airport"),
	BHD("Belfast City (George Best)"),
	BFS("Belfast International"),
	DUB("Dublin Airport"),
	GWY("Galway Airport"),
	KIR("Kerry County Airport"),
	NOC("Knock Airport"),
	SNN("Shannon (Limerick) Airport"),
	SXL("Sligo Airport"),
	WAT("Waterford");
	
	private String name;

	private IATAAirportCode ( String name)
	{
		this.name = name;
	}
	
	public String getAirportName() { return name; }
	
}
