package org.expense_bot.util;

import org.python.util.PythonInterpreter;
public class Calendar {
	public static void main(String[] args) {
	  try (PythonInterpreter pyInterp = new PythonInterpreter()) {
		pyInterp.exec("print('Hello Python World!')");
	  }
	}

}
