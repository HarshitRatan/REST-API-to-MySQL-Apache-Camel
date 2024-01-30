package com.ratan.main.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.ratan.main.processor.ConvertJsonToTaskStatus;

@Component
public class FirstRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("timer:start?period=500&repeatCount=1")
		.setHeader(Exchange.HTTP_METHOD, simple("GET"))
		.to("https://jsonplaceholder.typicode.com/todos")
		.process(new ConvertJsonToTaskStatus())
		.split().body().delimiter(",")
		.to("mybatis:mybatis.ratan.requestInsert?StatementType=InsertList")
		.log("Completed Update");
		
	}
}
