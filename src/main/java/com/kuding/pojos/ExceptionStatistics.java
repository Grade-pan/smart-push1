package com.kuding.pojos;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class ExceptionStatistics {

	/**
	 * 异常出现次数
	 */
	private AtomicLong showCount;

	/**
	 * 唯一id
	 */
	private String uid;

	/**
	 * 异常通知的时间
	 */
	private LocalDateTime noticeTime;

	/**
	 * 上一次通知时的次数
	 */
	private Long LastShowedCount;

	public ExceptionStatistics(String uid) {
		this.showCount = new AtomicLong(1);
		this.LastShowedCount = 1L;
		this.uid = uid;
		this.noticeTime = LocalDateTime.now();
	}

	public ExceptionStatistics() {
	}

	public Long plusOne() {
		return showCount.incrementAndGet();
	}

	/**
	 * @return the showCount
	 */
	public AtomicLong getShowCount() {
		return showCount;
	}

	/**
	 * @param showCount the showCount to set
	 */
	public void setShowCount(AtomicLong showCount) {
		this.showCount = showCount;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the noticeTime
	 */
	public LocalDateTime getNoticeTime() {
		return noticeTime;
	}

	/**
	 * @param noticeTime the noticeTime to set
	 */
	public void setNoticeTime(LocalDateTime noticeTime) {
		this.noticeTime = noticeTime;
	}

	public Long getLastShowedCount() {
		return LastShowedCount;
	}

	public void setLastShowedCount(Long lastShowedCount) {
		LastShowedCount = lastShowedCount;
	}

	@Override
	public String toString() {
		return "ExceptionStatistics [showCount=" + showCount + ", uid=" + uid + ", noticeTime=" + noticeTime + "]";
	}

}
