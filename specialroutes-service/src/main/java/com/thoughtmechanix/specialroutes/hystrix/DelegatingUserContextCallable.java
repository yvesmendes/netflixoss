package com.thoughtmechanix.specialroutes.hystrix;

import java.util.concurrent.Callable;

import org.springframework.util.Assert;

import com.thoughtmechanix.specialroutes.utils.UserContext;
import com.thoughtmechanix.specialroutes.utils.UserContextHolder;

public final class DelegatingUserContextCallable<V> implements Callable<V> {
	private final Callable<V> delegate;

	private UserContext originalUserContext;

	public DelegatingUserContextCallable(Callable<V> delegate, UserContext userContext) {
		Assert.notNull(delegate, "delegate cannot be null");
		Assert.notNull(userContext, "userContext cannot be null");
		this.delegate = delegate;
		this.originalUserContext = userContext;
	}

	public DelegatingUserContextCallable(Callable<V> delegate) {
		this(delegate, UserContextHolder.getContext());
	}

	public V call() throws Exception {
		UserContextHolder.setContext(originalUserContext);
		try {
			return delegate.call();
		} finally {
			this.originalUserContext = null;
		}
	}

	public String toString() {
		return delegate.toString();
	}

	public static <V> Callable<V> create(Callable<V> delegate, UserContext userContext) {
		return new DelegatingUserContextCallable<V>(delegate, userContext);
	}
}