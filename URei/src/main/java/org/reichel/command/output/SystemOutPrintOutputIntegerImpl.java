package org.reichel.command.output;

public class SystemOutPrintOutputIntegerImpl implements Output<Integer>{

	@Override
	public void output(Integer output) {
		System.out.print(output);
	}

}
