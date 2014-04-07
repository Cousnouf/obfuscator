package common;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * This class allows to manage, sequence and launch threads. It has probably 
 * been already done several times and in a better way.
 * 
 */


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ThreadInterface {
	private Method[] methodToInvoke;
	private Object[] objectToInvokeFrom;
	private Object[][] argsToInvokeWith;

	public ThreadInterface(int methodNumber) {
		methodToInvoke = new Method[methodNumber];
		objectToInvokeFrom = new Object[methodNumber];
		argsToInvokeWith = new Object[methodNumber][];
	}
	
	public void setMethod(int order, Object objectFrom,
			String methodName, Class<Object>[] parameterTypes,
			Object[] args) {
		try {
			objectToInvokeFrom[order] = objectFrom;
			methodToInvoke[order] = objectFrom.getClass().getMethod(methodName, parameterTypes);
			argsToInvokeWith[order] = args;
		} catch (SecurityException | NoSuchMethodException e) {
		}
    }
	
	public void runMethods (String threadName) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					for (int i = 0; i < methodToInvoke.length; i++) {
						methodToInvoke[i].invoke(objectToInvokeFrom[i], argsToInvokeWith[i]);					
					}
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				}
            }
		}, threadName);
		thread.start();
	}
}
