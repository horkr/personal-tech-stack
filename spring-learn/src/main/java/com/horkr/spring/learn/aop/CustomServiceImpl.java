package com.horkr.spring.learn.aop;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CustomServiceImpl implements CustomService{
	@Resource
	private A a;
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
