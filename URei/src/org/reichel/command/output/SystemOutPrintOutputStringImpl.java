package org.reichel.command.output;

public class SystemOutPrintOutputStringImpl implements Output<String> {

	@Override
	public void output(String string) {
		System.out.print(string);
	}

}
