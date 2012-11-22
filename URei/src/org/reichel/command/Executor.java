package org.reichel.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.reichel.command.output.Output;
import org.reichel.command.output.SystemOutPrintOutputImpl;


public class Executor {

	private final String[] commands;

	private Boolean showLog = true;
	
	private final Output output;
	
	private Process process;
	
	public Executor(Output output, String... command){
		this(output, true, command);
	}
	
	public Executor(Output output, Boolean showLog, String... commands){
		if(output == null){
			throw new IllegalArgumentException("Parameter output cannot be null.");
		}
		if(showLog == null){
			throw new IllegalArgumentException("Parameter showLog cannot be null.");
		}

		if(commands == null || commands.length == 0){
			throw new IllegalArgumentException("Parameter command cannot be null or empty.");
		}

		this.output = output;
		this.showLog = showLog;
		this.commands = commands;
	}
	
	private Executor execute(){
		this.process = startProcess(configProcess());
		if(process != null){
			new Thread(new Runnable() {
				@Override
				public void run() {
					emptyBuffer(process);
				}
			}).start();
		}
		return this;
	}
	
	public void stop(){
		if(this.process != null){
			this.process.destroy();
		}
	}
	
	public int waitFor(){
		if(this.process != null){
			try {
				return this.process.waitFor();
			} catch (InterruptedException e) {
				this.output.output("Problemas ao esvaziar buffer: " + e.getClass().getName() + ":" + e.getMessage());
			}
		}
		return 1;
	}

	private void emptyBuffer(Process process) {
		//Read out dir output
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		if(showLog){
			this.output.output(String.format("Executando comando '%s'\n", this));
		}
		try {
			while ((line = br.readLine()) != null) {
				if(showLog){
					this.output.output(line + "\n");
				}
			}
		} catch (IOException e) {
			this.output.output("Problemas ao esvaziar buffer: " + e.getClass().getName() + ":" + e.getMessage());
		}
	}

	private ProcessBuilder configProcess() {
		ProcessBuilder pb = new ProcessBuilder(this.commands);
		pb.redirectErrorStream(true);
		return pb;
	}

	private Process startProcess(ProcessBuilder pb) {
		Process process = null;
		try {
			process = pb.start();
		} catch (IOException e) {
			this.output.output("Problemas ao iniciar processo: " + e.getClass().getName() + ":" + e.getMessage());
		}
		return process;
	}
	
	public static Executor execute(Output output, boolean showLog, String... args){
		return new Executor(output, showLog, args).execute();
	}
	
	public static Executor execute(Output output, String... args){
		return execute(output, true, args);
	}
	
	public static Executor execute(String... args){
		return execute(new SystemOutPrintOutputImpl(), true, args);
	}
	
	
	public String toString(){
		StringBuilder result = new StringBuilder();
		for(String command : commands){
			result.append(command).append(" ");
		}
		return result.toString();
	}
	
	public static void main(String[] args) {
		Executor execute = Executor.execute("svn","list","-R","http://s00win42.unimedfortaleza.com.br/svn/financas/trunk/");
//		execute.waitFor();
		System.out.println("bla");
		execute.stop();
	}
}
