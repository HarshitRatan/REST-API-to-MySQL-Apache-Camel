package com.ratan.main.utils;

public class TaskStatus {
	private int userId;
	private int id;
	private String title;
	private boolean completed;
	
	@Override
	public String toString() {
		return "TaskStatus [userID=" + userId + ", id=" + id + ", title=" + title + ", completed=" + completed + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
