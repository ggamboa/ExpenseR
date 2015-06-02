package com.ggg.et3.global;

import com.ggg.et3.jpa.service.DCTransService;

public class DCTransServiceThreadLocal {
	
	public static final ThreadLocal<DCTransService> threadLocal = new ThreadLocal<DCTransService>();

    public static void set(DCTransService svc) {
        threadLocal.set(svc);
    }

    public static void unset() {
        threadLocal.remove();
    }

    public static DCTransService get() {
        return threadLocal.get();
    }
}
