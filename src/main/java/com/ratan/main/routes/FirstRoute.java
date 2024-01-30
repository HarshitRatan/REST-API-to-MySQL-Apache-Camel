package com.ratan.main.routes;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.ratan.main.processor.ConvertJsonToTaskStatus;
import com.ratan.main.utils.TaskStatus;

@Component
public class FirstRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("timer:start?period=500&repeatCount=1")
		.setHeader(Exchange.HTTP_METHOD, simple("GET"))
		.to("https://jsonplaceholder.typicode.com/todos")
		.process(new ConvertJsonToTaskStatus())
		.split().body().delimiter(",")
		.setProperty("originalBody", simple("${body}"))
		.to("mybatis:mybatis.ratan.requestSelectById?StatementType=SelectList")
		.choice()
				.when(simple("${body} == ''"))
					.log("Inserting New Row.....")
					.process(exchange->{
						TaskStatus task = (TaskStatus) exchange.getProperty("originalBody");
						exchange.getIn().setBody(task);
					})
					.doTry()
						.to("mybatis:mybatis.ratan.requestInsert?StatementType=InsertList")
						.log("Inserted Sucessfully. ${body}")
					.doCatch(Exception.class)
						.log("Exception Occurs !!!! ${exception.message}")
				.endChoice()
				.otherwise()
					.process(exchange->{
						TaskStatus originalTask = (TaskStatus) exchange.getProperty("originalBody");
						ArrayList<?> dbTaskList = (ArrayList<?>) exchange.getIn().getBody();
						TaskStatus dbTask = (TaskStatus) dbTaskList.get(0);
						if(originalTask.checkEqual(dbTask)) {
							exchange.getIn().setBody("");
						} else {
							exchange.getIn().setBody(originalTask);
						}
					})
					.choice()
						.when(simple("${body} != ''"))
							.log("Updating the Row.....")
							.doTry()
								.to("mybatis:mybatis.ratan.requestUpdate?StatementType=Update")
								.log("Updated Sucessfully. ${body}")
							.doCatch(Exception.class)
								.log("Exception Occurs !!!! ${exception.message}")
						.endChoice()
					.end()
				.endChoice()
		.end();
		System.out.println("All data are up to date :)");
	}
}
