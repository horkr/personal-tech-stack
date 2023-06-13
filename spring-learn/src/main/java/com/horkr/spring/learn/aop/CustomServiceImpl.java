package com.horkr.spring.learn.aop;

import org.springframework.stereotype.Service;

@Service
public class CustomServiceImpl implements CustomService{
	@Override
	public void add() {
		System.err.println("add() invoke");
	}

	@Override
	public void reduce() {
		System.err.println("reduce() invoke");
	}

	@Override
	public int get() {
		System.err.println("get() invoke");
		int a = 1;
		if(a==1){
			throw new IllegalStateException("");
		}
		return 1;
	}
}
