package org.reichel.command.output;

public class SystemOutPrintOutputImpl implements Output {

	@Override
	public void output(String string) {
		System.out.print(string);
	}

}
